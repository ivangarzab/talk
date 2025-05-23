package com.ivangarzab.data.audio

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivangarzab.data.util.JsonLoader
import com.ivangarzab.websocket.models.AudioChunk

/**
 * The purpose of this class is to handle remote data source operations for [AudioChunk].
 *
 * NOTE: The idea would be for this class to grab the data from the internet or some kind of endpoint;
 * however, and due to the nature of this project, we are currently using local JSON files for demonstration purposes.
 */
class AudioChunksRemoteDataSource(
    private val jsonLoader: JsonLoader
) {

    /**
     * Grab the [AudioChunk] data from our remote data source.
     *
     * For this project, this will be loaded from a local JSON file named [FILENAME].
     */
    suspend fun getAudioChunks(): List<AudioChunk> {
        val jsonData: String = jsonLoader.loadJsonFromResources(FILENAME)
        val audioChunkListType = object : TypeToken<List<AudioChunk>>() {}.type
        return Gson().fromJson(jsonData, audioChunkListType)
    }

    companion object {
        private const val FILENAME = "asr-stream-audio-chunks.json"
    }
}