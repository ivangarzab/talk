package com.ivangarzab.data.course

/**
 * The purpose of this class is to handle the local data source operations for [Course].
 *
 * NOTE: The idea would be for this class to grab the data from a local database, such as SQLite;
 * however, and due to the nature of this project, we will leave this class merely as a stub.
 */
class CourseLocalDataSource {

    /**
     * Grab the [Course] data from our local data source.
     *
     * For this project, this will simply leave this function stubbed.
     */
    suspend fun getCourse(): Course {
        // stubbed
        return Course(
            id = "",
            info = Info("", "", "", ""),
            units = listOf()
        )
    }
}