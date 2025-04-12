package com.ivangarzab.websocket.di

import com.ivangarzab.websocket.repositories.WebSocketRepository
import com.ivangarzab.websocket.usecases.ObserveWebSocketResponseUseCase
import com.ivangarzab.websocket.usecases.SendAudioChunkUseCase
import com.ivangarzab.websocket.usecases.StartWebSocketUseCase
import org.koin.dsl.module

/**
 * [org.koin.core.Koin] dependency module for injection of the websocket domain.
 */
val webSocketDomainModule = module {
    factory {
        ObserveWebSocketResponseUseCase(
            get(WebSocketRepository::class)
        )
    }
    factory {
        StartWebSocketUseCase(
            get(WebSocketRepository::class)
        )
    }
    factory {
        SendAudioChunkUseCase(
            get(WebSocketRepository::class)
        )
    }
}