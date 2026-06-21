package com.carlosegat.todo.android

import androidx.lifecycle.ViewModel
import com.carlosegat.todo.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskViewModel : ViewModel() {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    fun add() {
        val title = "Task ${_todos.value.size + 1}"
        println("TodoVM: adding $title")
        _todos.value = _todos.value + Todo.create(title)
    }

    fun toggle(id: String) {
        val updated = _todos.value.toMutableList()
        val i = updated.indexOfFirst { it.id == id }
        if (i != -1) {
            updated[i] = updated[i].copy(done = !updated[i].done)
        }
        _todos.value = updated
    }
}
