package com.ivangarzab.record.di

import com.ivangarzab.data.audio.AudioChunksRepository
import com.ivangarzab.data.network.WebSocketRepositoryImpl
import com.ivangarzab.record.RecordViewModel
import com.ivangarzab.websocket.usecases.ObserveWebSocketResponseUseCase
import com.ivangarzab.websocket.usecases.SendAudioChunkUseCase
import com.ivangarzab.websocket.usecases.StartWebSocketUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * [org.koin.core.Koin] dependency module for injection of the record feature.
 */
val recordFeatureModule = module {
    viewModel {
        RecordViewModel(
            audioChunksRepository = get(AudioChunksRepository::class),
            observeWebSocketResponseUseCase = get(ObserveWebSocketResponseUseCase::class),
            startWebSocketUseCase = get(StartWebSocketUseCase::class),
            sendAudioChunkUseCase = get(SendAudioChunkUseCase::class)
        )
    }
}