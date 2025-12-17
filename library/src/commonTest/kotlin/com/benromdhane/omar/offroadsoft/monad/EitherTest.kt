package com.benromdhane.omar.offroadsoft.monad

import io.kotest.assertions.assertSoftly
import kotlin.random.Random
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

    @Test
    fun `map right must transform the initial element if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .mapRight { it.length }
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement.length, result)
    }

    @Test
    fun `map right must be ignored if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .mapRight { it.length }
                .left()

        assertTrue { result }
    }

    @Test
    fun `map left must transform the initial element if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .mapLeft { it.length }
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement.length, result)
    }

    @Test
    fun `map left must be ignored if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .mapLeft { it.length }
                .right()

        assertTrue { result }
    }

    @Test
    fun `filter right must be ignored if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val alternative = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .filterRight(alternative) { it.isEmpty() }
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `filter right must be ignored if either was initiated as right with a value that is valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val alternative = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .filterRight(alternative) { it.isNotEmpty() }
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `filter right must return left either if initial either was initiated as right with a value that is not valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val alternative = Random.nextInt()
        val result =
            Either
                .Right
                .of<Int, _>(initialElement)
                .filterRight(alternative) { it.isEmpty() }
                .toMaybeLeft()
                .orNull()!!

        assertEquals(alternative, result)
    }

    @Test
    fun `filter right with left value seed must be ignored if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        var evaluated = false
        val alternative = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .filterRight(alternative) { it.isEmpty() }
                .toMaybeLeft()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialElement, result)
        }
    }

    @Test
    fun `filter right with left value seed must be ignored if either was initiated as right with a value that is valid for the filter`() {
        val initialElement = Uuid.random().toString()
        var evaluated = false
        val alternative = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .filterRight(alternative) { it.isNotEmpty() }
                .toMaybeRight()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialElement, result)
        }
    }

    @Test
    fun `filter right with left value seed must return left either if initial either was initiated as right with a value that is not valid for the filter`() {
        val initialElement = Uuid.random().toString()
        var evaluated = false
        val alternativeSeed = Random.nextInt()
        val alternative = {
            evaluated = true
            alternativeSeed
        }
        val result =
            Either
                .Right
                .of<Int, _>(initialElement)
                .filterRight(alternative) { it.isEmpty() }
                .toMaybeLeft()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeSeed, result)
        }
    }

    @Test
    fun `filter left must be ignored if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val alternative = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .filterLeft(alternative) { it.isEmpty() }
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `filter left must be ignored if either was initiated as left with a value that is valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val alternative = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .filterLeft(alternative) { it.isNotEmpty() }
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `filter left must return right either if initial either was initiated as left with a value that is not valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val alternative = Random.nextInt()
        val result =
            Either
                .Left
                .of<_, Int>(initialElement)
                .filterLeft(alternative) { it.isEmpty() }
                .toMaybeRight()
                .orNull()!!

        assertEquals(alternative, result)
    }

    @Test
    fun `filter left with right value seed must be ignored if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        var evaluated = false
        val alternative = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .filterLeft(alternative) { it.isEmpty() }
                .toMaybeRight()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialElement, result)
        }
    }

    @Test
    fun `filter left with right value seed must be ignored if either was initiated as left with a value that is valid for the filter`() {
        val initialElement = Uuid.random().toString()
        var evaluated = false
        val alternative = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .filterLeft(alternative) { it.isNotEmpty() }
                .toMaybeLeft()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialElement, result)
        }
    }

    @Test
    fun `filter left with right value seed must return right either if initial either was initiated as left with a value that is not valid for the filter`() {
        val initialElement = Uuid.random().toString()
        var evaluated = false
        val alternativeSeed = Random.nextInt()
        val alternative = {
            evaluated = true
            alternativeSeed
        }
        val result =
            Either
                .Left
                .of<_, Int>(initialElement)
                .filterLeft(alternative) { it.isEmpty() }
                .toMaybeRight()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeSeed, result)
        }
    }

    @Test
    fun `to right must be ignored if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val alternative = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .toRight(alternative)
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `to right must return right either with alternative value if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val alternative = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .toRight(alternative)
                .toMaybeRight()
                .orNull()!!

        assertEquals(alternative, result)
    }

    @Test
    fun `to right with alternative seed must be ignored if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        var evaluated = false
        val alternative = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .toRight(alternative)
                .toMaybeRight()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialElement, result)
        }
    }

    @Test
    fun `to right with alternative seed must return right either with alternative value if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        var evaluated = false
        val alternativeSeed = Uuid.random().toString()
        val alternative = {
            evaluated = true
            alternativeSeed
        }
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .toRight(alternative)
                .toMaybeRight()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeSeed, result)
        }
    }

    @Test
    fun `to left must be ignored if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val alternative = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .toLeft(alternative)
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `to left must return left either with alternative value if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val alternative = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .toLeft(alternative)
                .toMaybeLeft()
                .orNull()!!

        assertEquals(alternative, result)
    }
}