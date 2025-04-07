package com.ivangarzab.data.course

import com.ivangarzab.data.network.NetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * The purpose of this repository is to manage the [Course] data, providing a unified interface
 * for accessing and updating course data from both remote and local data sources.
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
     * Consume a [StateFlow] of [Course] data, which may contain null values if no data is available.
     */
    fun listenForCourseData(): StateFlow<Course?> {
        return _courseData.asStateFlow()
    }
}