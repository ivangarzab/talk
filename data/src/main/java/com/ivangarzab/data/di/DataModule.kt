package com.ivangarzab.data.di

import android.content.Context
import com.ivangarzab.data.audio.AudioChunksLocalDataSource
import com.ivangarzab.data.audio.AudioChunksRemoteDataSource
import com.ivangarzab.data.audio.AudioChunksRepository
import com.ivangarzab.data.course.CourseLocalDataSource
import com.ivangarzab.data.course.CourseRemoteDataSource
import com.ivangarzab.data.course.CourseRepository
import com.ivangarzab.data.util.JsonLoader
import com.ivangarzab.data.network.NetworkRepository
import com.ivangarzab.data.network.WebSocketRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

/**
 * [org.koin.core.Koin] dependency module for injection from the data layer.
 */
val dataModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    single { JsonLoader(get(Context::class)) }
    // network data
    factory { NetworkRepository(get(Context::class)) }
    factory { WebSocketRepository() }
    // Course data
    single { CourseRemoteDataSource(get(JsonLoader::class)) }
    single { CourseLocalDataSource() }
    single {
        CourseRepository(
            get(CourseRemoteDataSource::class),
            get(CourseLocalDataSource::class),
            get(NetworkRepository::class),
            get(CoroutineScope::class)
        )
    }
    // Audio chunks data
    single { AudioChunksRemoteDataSource(get(JsonLoader::class)) }
    single { AudioChunksLocalDataSource() }
    single {
        AudioChunksRepository(
            get(AudioChunksRemoteDataSource::class),
            get(AudioChunksLocalDataSource::class),
            get(NetworkRepository::class),
            get(CoroutineScope::class)
        )
    }
}