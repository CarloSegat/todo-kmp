package com.carlosegat.todo.android.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlosegat.todo.android.TaskViewModel
import com.carlosegat.todo.android.ui.screens.StatisticsScreen
import com.carlosegat.todo.android.ui.screens.TaskDetailScreen
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
                onOpenTask = { id -> navController.navigate("task/$id") },
            )
        }
        composable("stats") {
            StatisticsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
        // backStackEntry = the Navigation back-stack entry for THIS destination instance:
        // it carries the route's parsed arguments (here, {id}) plus a Lifecycle + ViewModelStore
        // scoped to this screen.
        composable("task/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            if (id != null) {
                TaskDetailScreen(
                    todoId = id,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
