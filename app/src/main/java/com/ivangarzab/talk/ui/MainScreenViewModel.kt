package com.ivangarzab.talk.ui

import androidx.lifecycle.ViewModel
import com.ivangarzab.data.audio.AudioChunksRepository
import com.ivangarzab.data.course.CourseRepository

/**
 * This [ViewModel] is responsible for managing the business logic of the [com.ivangarzab.talk.ui.MainActivityScreen].
 */
class MainScreenViewModel(
    private val courseRepository: CourseRepository,
    private val audioChunksRepository: AudioChunksRepository
) : ViewModel() {

    val courseData = courseRepository.listenForCourseData()

    val audioChunksData = audioChunksRepository.listenForAudioChunks()
}