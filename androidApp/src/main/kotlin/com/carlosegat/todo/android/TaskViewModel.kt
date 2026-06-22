package com.carlosegat.todo.android

import androidx.lifecycle.ViewModel
import com.carlosegat.todo.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskViewModel : ViewModel() {
    // MutableStateFlow<T>: a hot, observable state holder that ALWAYS has a current value
    // (_todos.value). It is read AND write, and emits the new value to every collector
    // whenever .value changes (it skips emissions equal to the previous value).
    // This is the single source of truth; only the ViewModel writes to it (hence private + `_`).
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())

    // StateFlow<T>: the READ-ONLY view of that state — a .value getter + observable, no setter.
    // asStateFlow() wraps _todos in such a read-only StateFlow, so the UI can read/observe the
    // list but cannot mutate it (encapsulation). Compose's collectAsState() subscribes to this
    // and recomposes whenever the value changes.
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    fun add(title: String) {
        _todos.value += Todo.create(title)
    }

    fun toggle(id: String) {
        _todos.value = _todos.value.map { if (it.id == id) it.toggled() else it }
    }
}
