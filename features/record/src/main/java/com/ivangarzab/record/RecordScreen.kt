package com.ivangarzab.record

import android.content.res.Configuration
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.record.components.RecordScreenButton
import com.ivangarzab.resources.ui.theme.TalkTheme

/**
 * The purpose of this composable is to serve as the main entry point for displaying
 * and interacting with the recording screen.
 */
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    responseText: String = "",
    onRecordButtonClicked: () -> Unit
) {
    // State to trigger the button's jumping animation
    var shouldAnimate by remember { mutableStateOf(false) }
    // Vertical offset for the jumping animation
    val verticalOffset by animateDpAsState(
        targetValue = if (shouldAnimate) (-20).dp else 0.dp,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(durationMillis = 450),
            repeatMode = RepeatMode.Reverse
        ),
        label = "buttonJumpAnimation"
    )

    LaunchedEffect(Unit) {
        shouldAnimate = true
    }

    Box(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 36.dp)
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            when (responseText.isEmpty()) {
                true -> {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 150.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        text = "Tap the record button to start",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center
                    )
                }
                false -> {
                    Text(
                        modifier = Modifier
                            .padding(top = 128.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                            .align(Alignment.TopCenter),
                        text = responseText,
                        style = MaterialTheme.typography.displayLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
            RecordScreenButton(
                modifier = Modifier
                    .padding(bottom = 42.dp + verticalOffset)
                    .align(Alignment.BottomCenter),
                onRecordClick = onRecordButtonClicked
            )
        }
    }
}

@Preview
@Composable
fun RecordScreenPreview() {
    TalkTheme {
        RecordScreen(
            responseText = "THIS IS A VERY LONG AND ACCURATE TEST",
            onRecordButtonClicked = { }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RecordScreenPreviewNight() {
    TalkTheme {
        RecordScreen(
            responseText = "THIS IS A VERY LONG AND ACCURATE TEST",
            onRecordButtonClicked = { }
        )
    }
}

@Preview
@Composable
fun RecordScreenEmptyPreview() {
    TalkTheme {
        RecordScreen(
            responseText = "",
            onRecordButtonClicked = { }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RecordScreenEmptyPreviewNight() {
    TalkTheme {
        RecordScreen(
            responseText = "",
            onRecordButtonClicked = { }
        )
    }
}