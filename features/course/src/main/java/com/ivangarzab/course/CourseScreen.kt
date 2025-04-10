package com.ivangarzab.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ivangarzab.course.components.CourseScreenBanner
import com.ivangarzab.course.components.CourseScreenInfo
import com.ivangarzab.course.components.CourseScreenUnitList
import com.ivangarzab.data.course.Course
import com.ivangarzab.data.course.Day
import com.ivangarzab.data.course.Info
import com.ivangarzab.data.course.Unit as CourseUnit
import com.ivangarzab.resources.ui.theme.TalkTheme

/**
 * The purpose of this composable is to serve as the main entry point for displaying
 * all of the [Course] information in a single screen.
 */
@Composable
fun CourseScreen(
    modifier: Modifier = Modifier,
    course: Course,
    onUnitDayClick: (Day) -> Unit
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        CourseScreenBanner(
            thumbnailImageUrl = course.info.thumbnailImageUrl,
            backgroundImageUrl = course.info.backgroundImageUrl
        )
        CourseScreenInfo(info = course.info)
        CourseScreenUnitList(
            units = course.units,
            onUnitDayClick = { onUnitDayClick(it) }
        )
    }
}

@Preview
@Composable
fun CourseScreenEmptyPreview() {
    TalkTheme {
        CourseScreen(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            course = Course("Example Course", Info("Course Title", "Course Subtitle", "Course Description", "Course Author"), listOf()),
            onUnitDayClick = { }
        )
    }
}

@Preview
@Composable
fun CourseScreenPreview() {
    TalkTheme {
        CourseScreen(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            course = Course(
                id = "Example Course",
                info = Info("Course Title", "Course Subtitle", "Course Description", "Course Author"),
                units = listOf(
                    CourseUnit(
                        "1", "Unit 1 Description",
                        listOf(
                            Day("1.1", "Learning Objective", "Some Topic", "Additional Info"),
                            Day("1.2", "Learning Objective", "Some Topic", "Additional Info")
                        )
                    ),
                    CourseUnit(
                        "2", "Unit 2 Description",
                        listOf(
                            Day("2.1", "Learning Objective", "Some Topic", "Additional Info")
                        )
                    ),
                )
            ),
            onUnitDayClick = { }
        )
    }
}
