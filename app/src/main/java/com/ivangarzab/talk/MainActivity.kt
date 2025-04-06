package com.ivangarzab.talk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.data.audio.AudioChunk
import com.ivangarzab.data.audio.AudioChunksRepository
import com.ivangarzab.data.course.Course
import com.ivangarzab.data.course.CourseRepository
import com.ivangarzab.data.course.Info
import com.ivangarzab.resources.ui.theme.TalkTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ///////// Move into ViewModel /////////
        val courseRepository: CourseRepository by inject()
        val audioChunkRepository: AudioChunksRepository by inject()
        //////////////////////////////////////

        setContent {
            TalkTheme {
                MainScreenStateful(courseRepository, audioChunkRepository)
            }
        }
    }
}

@Composable
fun MainScreenStateful(
    courseRepository: CourseRepository,
    audioChunkRepository: AudioChunksRepository
) {
    val courseData by courseRepository.listenForCourseData().collectAsState()
    val audioChunkData by audioChunkRepository.listenForAudioChunks().collectAsState()

    MainScreen(courseData, audioChunkData)
}

@Composable
fun MainScreen(
    courseData: Course?,
    audioChunkData: List<AudioChunk>
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Greeting(
                modifier = Modifier.weight(1f),
                text = courseData.toString()
            )
            HorizontalDivider(thickness = 1.dp)
            Greeting(
                modifier = Modifier.weight(1f),
                text = audioChunkData.toString()
            )
        }
    }
}

@Composable
fun Greeting(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    TalkTheme {
        MainScreen(
            courseData = Course(
                id = "c1",
                Info("Speak", "1", "1", "Take Home Project"),
                listOf()
            ),
            audioChunkData = listOf()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TalkTheme {
        Greeting("Android")
    }
}