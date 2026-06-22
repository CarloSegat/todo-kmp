package com.carlosegat.todo.android.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlosegat.todo.android.TaskViewModel
import com.carlosegat.todo.android.ui.screens.StatisticsScreen
import com.carlosegat.todo.android.ui.screens.TaskListScreen

@Composable
fun TodoNavHost() {
    val navController = rememberNavController()
    // SAME TaskViewModel is shared by the list and statistics screens.
    val viewModel: TaskViewModel = viewModel()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            TaskListScreen(
                viewModel = viewModel,
                onOpenStats = { navController.navigate("stats") },
            )
        }
        composable("stats") {
            StatisticsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
