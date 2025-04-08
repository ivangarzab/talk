package com.ivangarzab.data.util

import android.content.Context
import timber.log.Timber
import java.io.IOException

/**
 * The purpose of this class is to grab raw JSON file from the data/main/resources directory,
 * and return them as a [String] for further manipulation.
 */
class JsonLoader(private val context: Context) {

    /**
     * Provided a file name as a [String], access a JSON file from the assets directory,
     * and return it as a [String].
     *
     * Alternatively, return an empty string if the file does not exist or cannot be read.
     */
    fun loadJsonFromResources(fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            Timber.e(ioException, "Error loading JSON file: $fileName")
            "" // return empty String
        }
    }
}