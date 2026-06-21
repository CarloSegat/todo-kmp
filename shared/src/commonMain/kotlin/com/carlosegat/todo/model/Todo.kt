package com.carlosegat.todo.model

import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Todo(
    val id: String,
    val title: String,
    val done: Boolean = false,
    val createdAt: Long = 0L,
    val description: String = "",
    val imagePath: String? = null,
) {
    companion object { // Kotlin's "static": members on the class itself, called as Todo.create(...)
        // Creating a Todo (client-generated id + creation time) is a domain rule, so it
        // lives in the model — in shared/commonMain, every platform builds Todos the same way.
        @OptIn(ExperimentalUuidApi::class) // acknowledge kotlin.uuid.Uuid is still experimental (else compile error)
        fun create(title: String): Todo = Todo(
            id = Uuid.random().toString(),
            title = title,
            createdAt = Clock.System.now().toEpochMilliseconds(),
        )
    }
}
