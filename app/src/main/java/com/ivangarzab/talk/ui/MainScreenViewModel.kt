package com.ivangarzab.talk.ui

import androidx.lifecycle.ViewModel
import com.ivangarzab.data.course.CourseRepository

/**
 * This [ViewModel] is responsible for managing the business logic of the [com.ivangarzab.talk.ui.MainScreen].
 */
class MainScreenViewModel(
    courseRepository: CourseRepository
) : ViewModel() {

    val courseData = courseRepository.listenForCourseData()
}