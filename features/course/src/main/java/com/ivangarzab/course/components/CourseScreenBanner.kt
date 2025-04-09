package com.ivangarzab.course.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ivangarzab.resources.R

/**
 * The purpose of this Composable function is to display a [com.ivangarzab.data.course.Course]
 * banner with a background image and a thumbnail image.
 */
@Composable
fun CourseScreenBanner(
    modifier: Modifier = Modifier,
    thumbnailImageUrl: String? = null,
    backgroundImageUrl: String? = null
) {
    Box(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            contentDescription = "Course background image",
            contentScale = ContentScale.Crop,
            model = backgroundImageUrl,
            placeholder = painterResource(id = R.drawable.course_default_header_background_vector),
            error = painterResource(id = R.drawable.course_default_header_background_vector),
            fallback = painterResource(id = R.drawable.course_default_header_background_vector)
        )
        AsyncImage(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(CircleShape)
                .size(125.dp),
            contentDescription = "Course thumbnail image",
            model = thumbnailImageUrl,
            placeholder = painterResource(id = R.drawable.profile_placeholder),
            error = painterResource(id = R.drawable.profile_placeholder),
            fallback = painterResource(id = R.drawable.profile_placeholder)
        )
    }
}

@Preview
@Composable
fun CourseScreenBannerPreview() {
    CourseScreenBanner(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    )
}