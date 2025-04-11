package com.ivangarzab.websocket.usecases

import com.ivangarzab.websocket.repositories.WebSocketRepository

/**
 * The purpose of this use case class is to leverage the [WebSocketRepository] to
 * establish a connection to the WebSocket server and initiate the streaming process.
 */
class StartWebSocketUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    suspend operator fun invoke(
        learningLocale: String = "en-US",
        inputSampleRate: Int = 16000
    ) {
        // Open the web socket
        webSocketRepository.openWebSocket()
        // Send the 'start' message
        webSocketRepository.sendStartStreamingMessage(learningLocale, inputSampleRate)
    }
}