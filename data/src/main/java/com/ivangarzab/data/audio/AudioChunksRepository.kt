package com.ivangarzab.data.audio

import com.ivangarzab.data.network.NetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * The purpose of this repository is to manage [AudioChunk], providing a unified interface
 * for accessing and updating audio chunk data from both remote and local data sources.
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
     * Consume a [StateFlow] of [List] of [AudioChunk] data.
     */
    fun listenForAudioChunks(): StateFlow<List<AudioChunk>> {
        return _audioChunks.asStateFlow()
    }
}