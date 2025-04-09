package com.ivangarzab.course.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.data.course.Info

/**
 * The purpose of this composable function is to display [com.ivangarzab.data.course.Course]'s
 * main information.
 */
@Composable
fun CourseScreenInfo(
    modifier: Modifier = Modifier,
    info: Info
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = info.subtitle,
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = info.title,
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun CourseScreenInfoPreview() {
    CourseScreenInfo(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
        info = Info("Example Title", "Example Subtitle", "Example Description", "Example Author")
    )
}