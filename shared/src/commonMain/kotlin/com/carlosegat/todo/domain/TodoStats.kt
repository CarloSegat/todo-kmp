package com.carlosegat.todo.domain

import com.carlosegat.todo.model.Todo

data class TodoStats(
    val total: Int,
    val done: Int,
    val percentDone: Int, // 0..100
)

// Pure domain calculation: no UI, identical on every platform, trivial to unit-test.
fun List<Todo>.stats(): TodoStats {
    val total = size
    val done = count { it.done }
    val percent = if (total == 0) 0 else done * 100 / total
    return TodoStats(total = total, done = done, percentDone = percent)
}
