package com.ivangarzab.talk

import androidx.lifecycle.ViewModel
import com.ivangarzab.data.audio.AudioChunksRepository
import com.ivangarzab.data.course.CourseRepository

/**
 * This [ViewModel] is responsible for managing the business logic of the [MainScreen].
 */
class MainScreenViewModel(
    private val courseRepository: CourseRepository,
    private val audioChunksRepository: AudioChunksRepository
) : ViewModel() {

    val courseData = courseRepository.listenForCourseData()

    val audioChunksData = audioChunksRepository.listenForAudioChunks()
}