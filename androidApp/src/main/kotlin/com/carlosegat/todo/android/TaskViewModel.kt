package com.carlosegat.todo.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosegat.todo.domain.TaskManager
import com.carlosegat.todo.model.Todo
import kotlinx.coroutines.launch

// Thin Android presentation adapter: it owns a shared TaskManager and forwards to it.
// The only Android-specific part is being a lifecycle-aware ViewModel; the state and
// the logic live in commonMain (TaskManager), ready to be reused by iOS. The suspend
// calls run in viewModelScope, with errors logged (not crashed) so a down backend is survivable.
class TaskViewModel : ViewModel() {
    private val taskManager = TaskManager()

    val todos = taskManager.todos

    init {
        viewModelScope.launch {
            try {
                taskManager.load()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "load failed", e)
            }
        }
    }

    fun add(title: String) {
        viewModelScope.launch {
            try {
                taskManager.add(title)
            } catch (e: Exception) {
                Log.e("TaskViewModel", "add failed", e)
            }
        }
    }

    fun toggle(id: String) {
        viewModelScope.launch {
            try {
                taskManager.toggle(id)
            } catch (e: Exception) {
                Log.e("TaskViewModel", "toggle failed", e)
            }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            try {
                taskManager.delete(id)
            } catch (e: Exception) {
                Log.e("TaskViewModel", "delete failed", e)
            }
        }
    }

    fun update(todo: Todo) = taskManager.update(todo)
    fun setImagePath(id: String, path: String?) = taskManager.setImagePath(id, path)
}
