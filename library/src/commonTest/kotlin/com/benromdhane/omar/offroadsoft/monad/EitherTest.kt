package com.benromdhane.omar.offroadsoft.monad

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class EitherTest {

    @Test
    fun `right must return true if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .right()

        assertTrue { result }
    }

    @Test
    fun `right must return false if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .right()

        assertFalse { result }
    }

    @Test
    fun `left must return false if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .left()

        assertFalse { result }
    }

    @Test
    fun `left must return true if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .left()

        assertTrue { result }
    }

    @Test
    fun `to maybe right must return maybe that contains the initial element if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `to maybe right must return empty maybe if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .toMaybeRight()
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to maybe left must return maybe that contains the initial element if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `to maybe left must return empty maybe if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .toMaybeLeft()
                .empty()

        assertTrue { result }
    }
}