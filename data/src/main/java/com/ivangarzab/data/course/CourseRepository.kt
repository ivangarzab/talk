package com.ivangarzab.data.course

import com.google.gson.Gson
import com.ivangarzab.data.util.JsonLoader
import com.ivangarzab.data.network.NetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * TODO:
 */
class CourseRepository(
    private val remoteDataSource: CourseRemoteDataSource,
    private val localDataSource: CourseLocalDataSource,
    private val networkRepository: NetworkRepository,
    coroutineScope: CoroutineScope
) {

    private val _courseData = MutableStateFlow<Course?>(null)

    init {
        Timber.v("Initializing CourseRepository")
        coroutineScope.launch {
            networkRepository.listenForNetworkAvailability().collect { isNetworkAvailable ->
                Timber.v("Network availability changed: $isNetworkAvailable")
                when (isNetworkAvailable) {
                    true -> _courseData.value = remoteDataSource.getCourse()
                    false -> _courseData.value = localDataSource.getCourse()
                }
            }
        }
    }

    /**
     * TODO:
     */
    fun listenForCourseData(): StateFlow<Course?> {
        return _courseData.asStateFlow()
    }
}

/**
 * TODO:
 */
class CourseRemoteDataSource(
    private val jsonLoader: JsonLoader
) {

    /**
     * TODO:
     */
    suspend fun getCourse(): Course {
        val jsonData: String = jsonLoader.loadJsonFromResources(FILENAME)
        return Gson().fromJson(jsonData, Course::class.java)
    }

    companion object {
        private const val FILENAME = "course.json"
    }
}

/**
 * TODO:
 */
class CourseLocalDataSource {

    /**
     * TODO:
     */
    suspend fun getCourse(): Course {
        // stubbed
        return Course("", Info("", "", "", ""), listOf())
    }
}