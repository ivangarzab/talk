package com.ivangarzab.talk.di

import com.ivangarzab.data.audio.AudioChunksRepository
import com.ivangarzab.data.course.CourseRepository
import com.ivangarzab.talk.MainScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * [org.koin.core.Koin] dependency module for injection from the app layer.
 */
val appModule = module {
    viewModel {
        MainScreenViewModel(
            courseRepository = get(CourseRepository::class),
            audioChunksRepository = get(AudioChunksRepository::class)
        )
    }
}