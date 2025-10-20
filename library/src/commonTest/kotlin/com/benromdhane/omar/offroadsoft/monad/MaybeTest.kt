package com.benromdhane.omar.offroadsoft.monad

import kotlin.test.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MaybeTest {

    @Test
    fun `maybe present must return true if it was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .present()

        assertTrue { result }
    }

    @Test
    fun `maybe empty must return false if it was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .empty()

        assertFalse { result }
    }

    @Test
    fun `maybe present must return false if it was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Any>()
                .present()

        assertFalse { result }
    }

    @Test
    fun `maybe empty must return true if it was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Any>()
                .empty()

        assertTrue { result }
    }

    @Test
    fun `maybe orNull must return null if it was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Any>()
                .orNull()

        assertNull(result)
    }

    @Test
    fun `maybe orNull must return the initial element if it was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `maybe or with element parameter must return the provided element if it was initiated as empty`() {
        val element = Uuid.random().toString()
        val result =
            Maybe
                .Empty
                .of<String>()
                .or(element)

        assertEquals(element, result)
    }

    @Test
    fun `maybe or with element parameter must return the initial element if it was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val secondElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .or(secondElement)

        assertEquals(initialElement, result)
    }

    @Test
    fun `maybe or throw must throw empty maybe exception if it was initiated as empty`() {
        assertFailsWith<Maybe.EmptyMaybeException> {
            Maybe
                .Empty
                .of<String>()
                .orThrow()
        }
    }

    @Test
    fun `maybe or throw must return the initial element if it was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .orThrow()

        assertEquals(initialElement, result)
    }
}