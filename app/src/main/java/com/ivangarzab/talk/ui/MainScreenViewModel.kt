package com.ivangarzab.talk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivangarzab.data.audio.AudioChunksRepository
import com.ivangarzab.data.course.CourseRepository
import com.ivangarzab.data.network.WebSocketResponse
import com.ivangarzab.data.network.WebSocketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * This [ViewModel] is responsible for managing the business logic of the [com.ivangarzab.talk.ui.MainScreen].
 */
class MainScreenViewModel(
    courseRepository: CourseRepository,
    audioChunksRepository: AudioChunksRepository,
    private val webSocketRepository: WebSocketRepository //TODO: Use a repo instead
) : ViewModel() {

    val courseData = courseRepository.listenForCourseData()

    /*val responseTextData = flow {
        webSocketService.sendStartStreamingMessage(learningLocale = "en-US") // Using default inputSampleRate
        var counter = 0
        // Send initial audio chunk if available
        if (audioChunksData.value.isNotEmpty()) {
            webSocketService.sendAudioDataChunkMessage(audioChunksData.value[counter])
            counter++
        }

        webSocketResponsesData.collect { response: List<WebSocketResponse> ->
            response.lastOrNull()?.let { latest ->
                Timber.d("Got a new web socket response: $latest")
                // We may get a response with null text -- only emit if there's actual text to do so
                latest.text.let { latestText: String ->
                    emit(latestText)
                }

                // Send the next audio chunk if available
                if (counter < audioChunksData.value.size) {
                    webSocketService.sendAudioDataChunkMessage(audioChunksData.value[counter])
                    counter++
                }
            }
        }
    }.flowOn(Dispatchers.IO)*/

    private val audioChunksData = audioChunksRepository.listenForAudioChunks()

    private val webSocketResponsesData = webSocketRepository.listenForWebSocketResponses()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            webSocketRepository.sendStartStreamingMessage(learningLocale = "en-US") // Using default inputSampleRate
        }
    }

    fun startListeningForTextResponses() {
        viewModelScope.launch(Dispatchers.IO) {
            for (audioChunk in audioChunksData.value) {
                webSocketRepository.sendAudioDataChunkMessage(audioChunk)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            webSocketResponsesData.collect { response: List<WebSocketResponse> ->
                response.lastOrNull()?.let { latest ->
                    Timber.d("Got a new web socket response: $latest")
                    // Given that we're using Gson to parse the incoming JSON,
                    //  latest.text may indeed be null, in spite of what Android Studio thinks!
                    if (latest.text != null && latest.text.isNotEmpty()) {
                        Timber.i("Received text response from web socket: ${latest.text}")
                    }
                }
            }
        }
    }
}