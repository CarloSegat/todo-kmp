package com.carlosegat.todo.domain

import com.carlosegat.todo.data.TodoApi
import com.carlosegat.todo.data.TodoUpdateDto
import com.carlosegat.todo.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Shared business logic + in-memory state. Lives in commonMain, so Android and iOS
// drive the SAME TaskManager; each platform only adds a thin presentation adapter.
class TaskManager(private val api: TodoApi = TodoApi()) {
    // MutableStateFlow<T>: a hot, observable state holder that ALWAYS has a current value
    // (_todos.value). It is read AND write, and emits the new value to every collector
    // whenever .value changes. This is the single source of truth (private + `_`).
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())

    // StateFlow<T>: the READ-ONLY view exposed to callers — observe, but cannot mutate.
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    // Replace the single todo with this id (leaving the rest untouched). The lambda receives the
    // existing item, which is handy for carrying over local-only fields like imagePath.
    private fun replaceById(id: String, transform: (Todo) -> Todo) {
        _todos.value = _todos.value.map { if (it.id == id) transform(it) else it }
    }

    // Replace local state with the backend's list. Caller runs this in a coroutine scope.
    suspend fun load() {
        _todos.value = api.fetchAll()
    }

    // await-then-apply: POST the locally-built Todo (client UUID), then append the SERVER copy
    // (it carries the server's createdAt/done; the id matches, so deeplinks stay stable).
    suspend fun add(title: String) {
        val saved = api.create(Todo.create(title))
        _todos.value += saved
    }

    // await-then-apply: PUT only the flipped `done` (sparse body), then merge the server copy back,
    // carrying over the existing local-only imagePath.
    suspend fun toggle(id: String) {
        val current = _todos.value.find { it.id == id } ?: return
        val saved = api.update(id, TodoUpdateDto(done = !current.done))
        replaceById(id) { saved.copy(imagePath = it.imagePath) }
    }

    // await-then-apply: delete on the server (expect 204), then drop it from local state.
    suspend fun delete(id: String) {
        api.delete(id)
        _todos.value = _todos.value.filterNot { it.id == id }
    }

    // Optimistic: reflect the edit in state IMMEDIATELY, then PUT only if a server-tracked field
    // changed
    suspend fun update(updated: Todo) {
        val previous = _todos.value.find { it.id == updated.id } ?: return

        // call immediately so each typed character int eh "Description" appears
        replaceById(updated.id) { updated }

        // Only PUT when a field the server tracks changed
        val trackedChanged = previous.title != updated.title ||
            previous.description != updated.description ||
            previous.done != updated.done
        if (!trackedChanged) return

        // persist
        val updated = api.update(
            updated.id,
            TodoUpdateDto(title = updated.title, description = updated.description, done = updated.done),
        )
        replaceById(updated.id) { updated }
    }

    fun setImagePath(id: String, path: String?) {
        replaceById(id) { it.copy(imagePath = path) }
    }
}
