package com.ivangarzab.course.di

import com.ivangarzab.course.CourseViewModel
import com.ivangarzab.data.course.CourseRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * [org.koin.core.Koin] dependency module for injection of the course feature.
 */
val courseFeatureModule = module {
    viewModel {
        CourseViewModel(
            courseRepository = get(CourseRepository::class)
        )
    }
}