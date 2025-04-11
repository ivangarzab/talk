package com.ivangarzab.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivangarzab.data.audio.AudioChunksRepository
import com.ivangarzab.data.network.WebSocketRepositoryImpl
import com.ivangarzab.websocket.models.WebSocketResponse
import com.ivangarzab.websocket.usecases.ObserveWebSocketResponseUseCase
import com.ivangarzab.websocket.usecases.SendAudioChunkUseCase
import com.ivangarzab.websocket.usecases.StartWebSocketUseCase
import kotlinx.coroutines.Dispatchers
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

    private val audioChunksData = audioChunksRepository.listenForAudioChunks()

    private val webSocketResponsesData = observeWebSocketResponseUseCase()

    private val _responseText: MutableStateFlow<String> = MutableStateFlow("")
    val responseText: StateFlow<String> = _responseText

    fun startListeningForTextResponses() {
        viewModelScope.launch(Dispatchers.Default) {
            startWebSocketUseCase(learningLocale = "en-US") // Using default inputSampleRate
            for (audioChunk in audioChunksData.value) {
                sendAudioChunkUseCase(audioChunk)
            }
        }
        viewModelScope.launch(Dispatchers.Default) {
            webSocketResponsesData.collect { response: List<WebSocketResponse> ->
                response.lastOrNull()?.let { latest ->
                    Timber.d("Got a new web socket response: $latest")
                    // Given that we're using Gson to parse the incoming JSON,
                    //  latest.text may indeed be null, in spite of what Android Studio thinks!
                    if (latest.text != null && latest.text.isNotEmpty()) {
                        Timber.i("Received text response from web socket: ${latest.text}")
                        _responseText.value = latest.text
                    }
                }
            }
        }
    }
}