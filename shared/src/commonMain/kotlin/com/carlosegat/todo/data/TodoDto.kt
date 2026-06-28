package com.carlosegat.todo.data

import com.carlosegat.todo.model.Todo
import kotlinx.serialization.EncodeDefault
import kotlin.time.Instant
import kotlinx.serialization.Serializable

// Wire shape returned by the BE (GET/POST/PUT responses).
@Serializable
data class TodoDto(
    val id: String,
    val title: String,
    val done: Boolean,
    val description: String = "",
    val createdAt: String,
)

// Create request body: only the fields the server accepts on POST. `done` and `createdAt`
// are set server-side
@Serializable
data class TodoCreateDto(
    val id: String,
    val title: String,
    val description: String,
)

// encodeDefaults = false (set explicitly in TodoApi's Json) omits the untouched fields (ie they
// are null) so only changed fields go on the wire.
@Serializable()
data class TodoUpdateDto(
    val title: String? = null,
    val description: String? = null,
    val done: Boolean? = null,
)

// imagePath is a local-only field with no server counterpart
fun TodoDto.toDomain(): Todo = Todo(
    id = id,
    title = title,
    done = done,
    createdAt = parseEpochMillis(createdAt),
    description = description,
    imagePath = null,
)

fun Todo.toCreateDto(): TodoCreateDto = TodoCreateDto(
    id = id,
    title = title,
    description = description,
)

// The BE uses datetime.now() witjout timezone
// Instant.parse requires it
// Temporary fix: assume UTC
private fun parseEpochMillis(iso: String): Long {
    val time = iso.substringAfter('T', "")
    val hasZone = time.endsWith("Z") || time.contains('+') || time.contains('-')
    return Instant.parse(if (hasZone) iso else "${iso}Z").toEpochMilliseconds()
}
