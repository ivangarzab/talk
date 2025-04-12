package com.ivangarzab.course

import androidx.lifecycle.ViewModel
import com.ivangarzab.data.course.CourseRepository

/**
 * This [ViewModel] is responsible for managing the business logic of the Course feature,
 * providing data and functionality to the associated UI components.
 */
class CourseViewModel(
    courseRepository: CourseRepository
) : ViewModel() {

    val courseData = courseRepository.listenForCourseData()
}