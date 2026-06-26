package com.carlosegat.todo.android

import androidx.lifecycle.ViewModel
import com.carlosegat.todo.domain.TaskManager
import com.carlosegat.todo.model.Todo

// Thin Android presentation adapter: it owns a shared TaskManager and forwards to it.
// The only Android-specific part is being a lifecycle-aware ViewModel; the state and
// the logic live in commonMain (TaskManager), ready to be reused by iOS.
class TaskViewModel : ViewModel() {
    private val taskManager = TaskManager()

    val todos = taskManager.todos

    fun add(title: String) = taskManager.add(title)
    fun toggle(id: String) = taskManager.toggle(id)
    fun delete(id: String) = taskManager.delete(id)
    fun update(todo: Todo) = taskManager.update(todo)
}
