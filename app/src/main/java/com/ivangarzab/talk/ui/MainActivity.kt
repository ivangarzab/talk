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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ivangarzab.data.course.Course
import com.ivangarzab.data.course.Day
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

    val coroutineScope = rememberCoroutineScope()

    /*fun startListeningForTextResponses() {
        coroutineScope.launch {
            viewModel.responseTextData.collectLatest {
                Timber.d("Received response text: $it")
            }
        }
    }
*/
    val navController = rememberNavController()

    MainScreen(
        navController = navController,
        course = courseData,
        onUnitDayClick = { day ->
            // In the real app, we would use the Day param for something
//            startListeningForTextResponses()
            viewModel.startListeningForTextResponses()
        }
    )
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    course: Course?,
    onUnitDayClick: (Day) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        MainNavHost(
            modifier = modifier.padding(innerPadding),
            navController = navController,
        )
        /*course?.let {
            CourseScreen(
                modifier = modifier.padding(innerPadding),
                course = it,
                onUnitDayClick = onUnitDayClick
            )
        }*/
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    TalkTheme {
        MainScreen(
            modifier = Modifier.fillMaxSize(),
            navController = rememberNavController(),
            course = Course(
                id = "c1",
                Info("Speak", "1", "1", "Take Home Project"),
                listOf()
            ),
            onUnitDayClick = { }
        )
    }
}