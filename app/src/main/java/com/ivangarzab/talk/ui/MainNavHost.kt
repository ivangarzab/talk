package com.ivangarzab.talk.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ivangarzab.course.CourseScreen
import com.ivangarzab.record.RecordScreen
import org.koin.androidx.compose.koinViewModel

/**
 * TODO:
 */
@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = NavRoutes.COURSE,
        modifier = modifier
    ) {
        composable(NavRoutes.COURSE) {
            val viewModel: MainScreenViewModel = koinViewModel()
            val courseData by viewModel.courseData.collectAsState()

            courseData?.let { course ->
                CourseScreen(
                    course = course,
                    onUnitDayClick = { day ->
//                        viewModel.startListeningForTextResponses()
                        navController.navigate("${NavRoutes.RECORD}/${day.id}")
                    }
                )
            }
        }

        composable(
            route = "${NavRoutes.RECORD}/{dayId}",
            arguments = listOf(navArgument("dayId") { type = NavType.StringType })
        ) { backStackEntry ->
            val dayId = backStackEntry.arguments?.getString("dayId") ?: ""
//            val viewModel: RecordScreenViewModel = koinViewModel()

            RecordScreen(
                onRecordClick = { navController.navigateUp() }
            )
        }
    }
}

/**
 * TODO:
 */
object NavRoutes {
    const val COURSE = "course"
    const val RECORD = "record"
}

@Preview
@Composable
fun NavHostPreview() {
    MainNavHost()
}