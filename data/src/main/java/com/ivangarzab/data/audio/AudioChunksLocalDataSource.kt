package com.ivangarzab.data.audio

/**
 * The purpose of this class is to handle the local data source operations for [AudioChunk].
 *
 * NOTE: The idea would be for this class to grab the data from a local database, such as SQLite;
 * however, and due to the nature of this project, we will leave this class merely as a stub.
 */
class AudioChunksLocalDataSource {

    /**
     * Grab the [AudioChunk] data from our local data source.
     *
     * For this project, this will simply leave this function stubbed.
     */
    suspend fun getAudioChunks(): List<com.ivangarzab.websocket.models.AudioChunk> {
        // stubbed
        return listOf()
    }
}