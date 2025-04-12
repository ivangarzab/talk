package com.ivangarzab.websocket.usecases

import com.ivangarzab.websocket.models.AudioChunk
import com.ivangarzab.websocket.repositories.WebSocketRepository

/**
 * The purpose of this use case class is to leverage the [WebSocketRepository] to
 * send an [AudioChunk] to the server through the WebSocket connection.
 */
class SendAudioChunkUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    suspend operator fun invoke(audioChunk: AudioChunk) {
        webSocketRepository.sendAudioDataChunkMessage(audioChunk)
    }
}