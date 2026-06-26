package com.carlosegat.todo.domain

import com.carlosegat.todo.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Shared business logic + in-memory state. Lives in commonMain, so Android and iOS
// drive the SAME TaskManager; each platform only adds a thin presentation adapter.
class TaskManager {
    // MutableStateFlow<T>: a hot, observable state holder that ALWAYS has a current value
    // (_todos.value). It is read AND write, and emits the new value to every collector
    // whenever .value changes. This is the single source of truth (private + `_`).
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())

    // StateFlow<T>: the READ-ONLY view exposed to callers — observe, but cannot mutate.
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    fun add(title: String) {
        _todos.value += Todo.create(title)
    }

    fun toggle(id: String) {
        _todos.value = _todos.value.map { if (it.id == id) it.toggled() else it }
    }

    fun delete(id: String) {
        _todos.value = _todos.value.filterNot { it.id == id }
    }
}
