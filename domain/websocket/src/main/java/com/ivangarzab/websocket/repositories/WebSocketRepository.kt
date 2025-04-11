package com.ivangarzab.websocket.repositories

import com.ivangarzab.websocket.models.AudioChunk
import com.ivangarzab.websocket.models.WebSocketResponse
import kotlinx.coroutines.flow.StateFlow

/**
 * The purpose of this interface is to cleanly define the contract for WebSocket operations.
 */
interface WebSocketRepository {

    /**
     * Consumes a [StateFlow] of a list of [com.ivangarzab.websocket.models.WebSocketResponse].
     */
    fun listenForWebSocketResponses(): StateFlow<List<WebSocketResponse>>

    /**
     * Opens the web socket connection to establish real-time communication with the backend server.
     */
    suspend fun openWebSocket()

    /**
     * Sends a message to start streaming audio data, given the provided [learningLocale]
     * and the [inputSampleRate].
     */
    suspend fun sendStartStreamingMessage(
        learningLocale: String = "en-US",
        inputSampleRate: Int = 16000
    )

    /**
     * Sends a message containing an [AudioChunk] for the backend to process.
     */
    suspend fun sendAudioDataChunkMessage(audioChunk: AudioChunk)
}