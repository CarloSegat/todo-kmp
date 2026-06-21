package com.carlosegat.todo.android.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.carlosegat.todo.android.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskViewModel) {
    val todos by viewModel.todos.collectAsState()

    Scaffold(
        // the { } wraps the call in a lambda
        // the lambda syntax () -> ... ony works for types
        topBar = { TopAppBar(title = { Text("Todos") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.add() }) {
                Icon(Icons.Default.Add, contentDescription = "Add todo")
            }
        }
    ) { paddingValues -> // the Scaffold computed and passes the paddingValues
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // items(...){...} is a block body
            // key could have been: key = { e -> e.id }
            items(todos, key = { it.id }) { todo ->
                ListItem(
                    leadingContent = {
                        Checkbox(
                            checked = todo.done,
                            onCheckedChange = { viewModel.toggle(todo.id) }
                        )
                    },
                    headlineContent = { Text(todo.title) }
                )
            }
        }
    }
}
