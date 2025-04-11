package com.ivangarzab.record.di

import com.ivangarzab.data.audio.AudioChunksRepository
import com.ivangarzab.data.network.WebSocketRepository
import com.ivangarzab.record.RecordViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * [org.koin.core.Koin] dependency module for injection of the record feature.
 */
val recordFeatureModule = module {
    viewModel {
        RecordViewModel(
            audioChunksRepository = get(AudioChunksRepository::class),
            webSocketRepository = get(WebSocketRepository::class)
        )
    }
}