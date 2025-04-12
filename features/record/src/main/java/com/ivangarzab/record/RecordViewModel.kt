package com.ivangarzab.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivangarzab.data.audio.AudioChunksRepository
import com.ivangarzab.websocket.models.WebSocketResponse
import com.ivangarzab.websocket.models.WebSocketResponseType
import com.ivangarzab.websocket.usecases.ObserveWebSocketResponseUseCase
import com.ivangarzab.websocket.usecases.SendAudioChunkUseCase
import com.ivangarzab.websocket.usecases.StartWebSocketUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * This [ViewModel] is responsible for managing the business logic of the Record feature,
 * providing data and functionality to the associated UI components.
 */
class RecordViewModel(
    audioChunksRepository: AudioChunksRepository,
    observeWebSocketResponseUseCase: ObserveWebSocketResponseUseCase,
    val startWebSocketUseCase: StartWebSocketUseCase,
    val sendAudioChunkUseCase: SendAudioChunkUseCase
) : ViewModel() {

    // Properties to track coroutine Jobs that need to be cancelled for subsequent runs
    private var webSocketSetupJob: Job? = null
    private var webSocketListeningJob: Job? = null
    private var audioChunksJob: Job? = null

    private val audioChunksData = audioChunksRepository.listenForAudioChunks()

    private val webSocketResponsesData = observeWebSocketResponseUseCase()

    private val _responseText: MutableStateFlow<String> = MutableStateFlow("")
    val responseText: StateFlow<String> = _responseText

    fun onStreamingSessionStarted() {
        webSocketSetupJob =viewModelScope.launch(Dispatchers.Default) {
            // Clean up responseText in case this is not the first time we start a streaming session
            _responseText.value = ""
            startWebSocketUseCase(learningLocale = "en-US") // Using default inputSampleRate
        }

        webSocketListeningJob = viewModelScope.launch(Dispatchers.Default) {
            webSocketResponsesData.collect { response: List<WebSocketResponse> ->
                response.lastOrNull()?.let { latest ->
                    when (latest.type) {
                        WebSocketResponseType.METADATA -> {
                            // We can start sending audio chunks
                            Timber.d("Streaming session has started")
                            onSendStreamingSessionAudioChunks()
                        }
                        WebSocketResponseType.RESULT -> {
                            // Given that we're using Gson to parse the incoming JSON,
                            //  latest.text may indeed be null, in spite of what Android Studio thinks!
                            if (latest.text != null && latest.text.isNotEmpty()) {
                                Timber.d("Received text response from web socket: ${latest.text}")
                                _responseText.value = latest.text
                            }
                        }
                        WebSocketResponseType.CLOSED -> {
                            Timber.d("Streaming session has ended")
                            onStreamingSessionEnded()
                        }
                    }
                }
            }
        }
    }

    private fun onSendStreamingSessionAudioChunks() {
        Timber.d("Sending all audio chunks at once")
        audioChunksJob = viewModelScope.launch(Dispatchers.Default) {
            for (audioChunk in audioChunksData.value) {
                sendAudioChunkUseCase(audioChunk)
            }
        }
    }

    private fun onStreamingSessionEnded() {
        // Cancel all coroutine jobs once we're done streaming
        webSocketSetupJob?.cancel()
        webSocketListeningJob?.cancel()
        audioChunksJob?.cancel()

        // Reset job references for potential reuse
        webSocketSetupJob = null
        webSocketListeningJob = null
        audioChunksJob = null
    }

    override fun onCleared() {
        onStreamingSessionEnded()
        super.onCleared()
    }
}