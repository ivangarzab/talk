package com.ivangarzab.talk.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ivangarzab.course.CourseScreen
import com.ivangarzab.data.audio.AudioChunk
import com.ivangarzab.data.course.Course
import com.ivangarzab.data.course.Info
import com.ivangarzab.resources.ui.theme.TalkTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            TalkTheme {
                MainScreenStateful()
            }
        }
    }
}

@Composable
fun MainScreenStateful(
    viewModel: MainScreenViewModel = koinViewModel(),
) {
    val courseData by viewModel.courseData.collectAsState()

    val audioChunkData by viewModel.audioChunksData.collectAsState()

    MainActivityScreen(
        course = courseData,
        audioChunks = audioChunkData
    )
}

@Composable
fun MainActivityScreen(
    modifier: Modifier = Modifier,
    course: Course?,
    audioChunks:List<AudioChunk>
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        course?.let {
            CourseScreen(
                modifier = modifier.padding(innerPadding),
                course = it,
                onUnitDayClick = { day ->
                    //TODO: Navigate into the next screen
                }
            )
        }
    }
}

@Preview
@Composable
fun MainActivityScreenPreview() {
    MainActivityScreen(
        course = Course(
            id = "c1",
            Info("Speak", "1", "1", "Take Home Project"),
            listOf()
        ),
        audioChunks = listOf()
    )
}