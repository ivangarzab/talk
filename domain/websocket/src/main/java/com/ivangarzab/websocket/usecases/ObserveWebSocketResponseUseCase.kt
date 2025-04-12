package com.ivangarzab.websocket.usecases

import com.ivangarzab.websocket.models.WebSocketResponse
import com.ivangarzab.websocket.repositories.WebSocketRepository
import kotlinx.coroutines.flow.Flow

/**
 * The purpose of this use case class is to handle the observation of [WebSocketResponse]
 * from the [WebSocketRepository].
 */
class ObserveWebSocketResponseUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke(): Flow<List<WebSocketResponse>> = webSocketRepository.listenForWebSocketResponses()
}