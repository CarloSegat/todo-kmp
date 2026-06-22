package com.carlosegat.todo.domain

import com.carlosegat.todo.model.Todo
import kotlin.test.Test
import kotlin.test.assertEquals

class TodoStatsTest {

    private fun todo(done: Boolean) = Todo(id = "x", title = "t", done = done, createdAt = 0L)

    @Test
    fun emptyListIsAllZero() {
        val stats = emptyList<Todo>().stats()
        assertEquals(0, stats.total)
        assertEquals(0, stats.done)
        assertEquals(0, stats.percentDone) // guard: no divide-by-zero
    }

    @Test
    fun allDoneIsHundredPercent() {
        val stats = listOf(todo(true), todo(true)).stats()
        assertEquals(2, stats.total)
        assertEquals(2, stats.done)
        assertEquals(100, stats.percentDone)
    }

    @Test
    fun halfDoneIsFiftyPercent() {
        val stats = listOf(todo(true), todo(false)).stats()
        assertEquals(50, stats.percentDone)
    }

    @Test
    fun percentDoneTruncates() {
        // 1 of 3 done = 33.33% -> integer division truncates -> 33
        val stats = listOf(todo(true), todo(false), todo(false)).stats()
        assertEquals(33, stats.percentDone)
    }
}
