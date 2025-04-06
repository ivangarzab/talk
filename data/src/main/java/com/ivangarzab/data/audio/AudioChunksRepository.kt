package com.ivangarzab.data.audio

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
class AudioChunksRepository(
    private val remoteDataSource: AudioChunksRemoteDataSource,
    private val localDataSource: AudioChunksLocalDataSource,
    private val networkRepository: NetworkRepository,
    coroutineScope: CoroutineScope
) {

    private val _audioChunks = MutableStateFlow<List<AudioChunk>>(listOf())

    init {
        Timber.v("Initializing AudioChunksRepository")
        coroutineScope.launch {
            networkRepository.listenForNetworkAvailability().collect { isNetworkAvailable ->
                Timber.v("Network availability changed: $isNetworkAvailable")
                when (isNetworkAvailable) {
                    true -> _audioChunks.value = remoteDataSource.getAudioChunks()
                    false -> _audioChunks.value = localDataSource.getAudioChunks()
                }
            }
        }
    }

    /**
     * TODO:
     */
    fun listenForAudioChunks(): StateFlow<List<AudioChunk>> {
        return _audioChunks.asStateFlow()
    }
}

/**
 * TODO:
 */
class AudioChunksRemoteDataSource(
    private val jsonLoader: JsonLoader
) {

    /**
     * TODO:
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

/**
 * TODO:
 */
class AudioChunksLocalDataSource {

    /**
     * TODO:
     */
    suspend fun getAudioChunks(): List<AudioChunk> {

        // stubbed
        return listOf()
    }
}