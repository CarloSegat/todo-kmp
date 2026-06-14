package com.carlosegat.todo.model

data class Todo(
    val id: String,
    val title: String,
    val done: Boolean = false,
    val createdAt: Long = 0L,
    val description: String = "",
    val imagePath: String? = null,
)
