package com.ivangarzab.data.course

import com.google.gson.Gson
import com.ivangarzab.data.util.JsonLoader

/**
 * The purpose of this class is to handle remote data source operations for [Course].
 *
 * NOTE: The idea would be for this class to grab the data from the internet or some kind of endpoint;
 * however, and due to the nature of this project, we are currently using local JSON files for demonstration purposes.
 */
class CourseRemoteDataSource(
    private val jsonLoader: JsonLoader
) {

    /**
     * Grab the [Course] data from our remote data source.
     *
     * For this project, this will be loaded from a local JSON file named [FILENAME].
     */
    suspend fun getCourse(): Course {
        val jsonData: String = jsonLoader.loadJsonFromResources(FILENAME)
        return Gson().fromJson(jsonData, Course::class.java)
    }

    companion object {
        private const val FILENAME = "course.json"
    }
}