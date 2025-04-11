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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ivangarzab.course.CourseScreen
import com.ivangarzab.record.RecordScreen
import com.ivangarzab.resources.ui.theme.TalkTheme
import org.koin.androidx.compose.koinViewModel

/**
 * The main activity of the application, responsible for setting up the navigation and UI components.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            TalkTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.navigationBars
                ) { innerPadding ->
                    MainNavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                    )
                }
            }
        }
    }
}

/**
 * The purpose of this composable function is to serve as the main navigation host for the application,
 * handling the routing between different screens.
 */
@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MainScreenViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.COURSE,
        modifier = modifier
    ) {
        composable(NavRoutes.COURSE) {
            val courseData by viewModel.courseData.collectAsState()

            courseData?.let { course ->
                CourseScreen(
                    course = course,
                    onUnitDayClick = { day ->
                        navController.navigate("${NavRoutes.RECORD}/${day.id}")
                    }
                )
            }
        }

        composable(
            route = "${NavRoutes.RECORD}/{dayId}",
            arguments = listOf(navArgument("dayId") { type = NavType.StringType })
        ) { backStackEntry ->
            // In the real app, we would use the Day param for something.
            val dayId = backStackEntry.arguments?.getString("dayId") ?: ""

            RecordScreen(
                onRecordClick = { viewModel.startListeningForTextResponses() }
            )
        }
    }
}

/**
 * Define the navigation routes mapped to specific screens.
 */
object NavRoutes {
    const val COURSE = "course"
    const val RECORD = "record"
}

@Preview
@Composable
fun MainNavHostPreview() {
    MainNavHost()
}