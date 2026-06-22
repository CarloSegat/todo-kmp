package com.carlosegat.todo.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TodoTest {

    @Test
    fun createSetsTitleAndDefaults() {
        val todo = Todo.create("Buy milk")
        assertEquals("Buy milk", todo.title)
        assertFalse(todo.done)
        assertTrue(todo.id.isNotBlank())
    }

    @Test
    fun createGivesUniqueIds() {
        assertTrue(Todo.create("a").id != Todo.create("b").id)
    }

    @Test
    fun toggledFlipsDone() {
        val todo = Todo.create("x")
        assertTrue(todo.toggled().done)
        assertFalse(todo.toggled().toggled().done)
    }

    @Test
    fun toggledKeepsOtherFields() {
        val todo = Todo.create("x")
        val toggled = todo.toggled()
        assertEquals(todo.id, toggled.id)
        assertEquals(todo.title, toggled.title)
        assertEquals(todo.createdAt, toggled.createdAt)
    }
}
