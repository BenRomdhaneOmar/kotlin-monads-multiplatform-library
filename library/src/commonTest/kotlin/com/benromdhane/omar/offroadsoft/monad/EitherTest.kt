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

    @Test
    fun `to left with alternative seed must be ignored if either was initiated as left`() {
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
                .toLeft(alternative)
                .toMaybeLeft()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialElement, result)
        }
    }

    @Test
    fun `to left with alternative seed must return left either with alternative value if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        var evaluated = false
        val alternativeSeed = Uuid.random().toString()
        val alternative = {
            evaluated = true
            alternativeSeed
        }
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .toLeft(alternative)
                .toMaybeLeft()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeSeed, result)
        }
    }

    @Test
    fun `flat map right must transform the initial element if either was initiated as right and the result of the mapping is right either with same left element type`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .flatMapRight { Either.Right.of(it.length) }
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement.length, result)
    }

    @Test
    fun `flat map right must transform the initial element if either was initiated as right and the result of the mapping is left either with same left element type`() {
        val initialElement = Uuid.random().toString()
        val finalValue = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .flatMapRight { Either.Left.of<_, Int>(finalValue) }
                .toMaybeLeft()
                .orNull()!!

        assertEquals(finalValue, result)
    }

    @Test
    fun `flat map right with result mapping left either must be ignored if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .flatMapRight { Either.Left.of<_, String>(it + Uuid.random().toString()) }
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `flat map right with result mapping right either must be ignored if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .flatMapRight { Either.Right.of(it.length) }
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `flat map left must transform the initial element if either was initiated as left and the result of the mapping is left either with same right element type`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .flatMapLeft { Either.Left.of(it.length) }
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement.length, result)
    }

    @Test
    fun `flat map left must transform the initial element if either was initiated as left and the result of the mapping is right either with same right element type`() {
        val initialElement = Uuid.random().toString()
        val finalValue = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .flatMapLeft { Either.Right.of<Int, String>(finalValue) }
                .toMaybeRight()
                .orNull()!!

        assertEquals(finalValue, result)
    }

    @Test
    fun `flat map left with result mapping right either must be ignored if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .flatMapLeft { Either.Right.of<String, _>(it + Uuid.random().toString()) }
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `flat map left with result mapping left either must be ignored if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .flatMapLeft { Either.Left.of(it + Uuid.random().toString()) }
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `flatten for either with right side as either with uniform types must transform the initial composed type either to flat type either if it was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<Int, _>(Either.Right.of<Int, _>(initialElement))
                .flatten()
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `flatten for either with right side as either with uniform types must transform the initial composed type either to flat type either if it was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(Either.Left.of<_, Int>(initialElement))
                .flatten()
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `flatten for either with left side as either with uniform types must transform the initial composed type either to flat type either if it was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(Either.Right.of<Int, _>(initialElement))
                .flatten()
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `flatten for either with left side as either with uniform types must transform the initial composed type either to flat type either if it was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, Int>(Either.Left.of<_, Int>(initialElement))
                .flatten()
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `flatten for either with both sides as either with uniform types must transform the initial composed type either to flat type either if the it was initiated as left either of left either`() {
        val initialElement = Random.nextInt()
        val result =
            Either
                .Left
                .of<_, Either<Int, String>>(Either.Left.of<_, String>(initialElement))
                .flatten()
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `flatten for either with both sides as either with uniform types must transform the initial composed type either to flat type either if the it was initiated as left either of right either`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, Either<Int, String>>(Either.Right.of<Int, _>(initialElement))
                .flatten()
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `flatten for either with both sides as either with uniform types must transform the initial composed type either to flat type either if the it was initiated as right either of left either`() {
        val initialElement = Random.nextInt()
        val result =
            Either
                .Right
                .of<Either<Int, String>, _>(Either.Left.of<_, String>(initialElement))
                .flatten()
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `flatten for either with both sides as either with uniform types must transform the initial composed type either to flat type either if the it was initiated as right either of right either`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<Either<Int, String>, _>(Either.Right.of<Int, _>(initialElement))
                .flatten()
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `switch must switch the either direction and value if it was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<Int, _>(initialElement)
                .switch()
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `switch must switch the either direction and value if it was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, Int>(initialElement)
                .switch()
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `fold must return right mapper transformation if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<Int, _>(initialElement)
                .fold(
                    { it },
                    { it.toString() }
                )

        assertEquals(initialElement, result)
    }

    @Test
    fun `fold must return left mapper transformation if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, Int>(initialElement)
                .fold(
                    { it.toString() },
                    { it }
                )

        assertEquals(initialElement, result)
    }

    @Test
    fun `fold must return right value if either was initiated as right with same left and right types`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .fold()

        assertEquals(initialElement, result)
    }

    @Test
    fun `fold must return left value if either was initiated as left with same left and right types`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .fold()

        assertEquals(initialElement, result)
    }

    @Test
    fun `fold with single mapper must return mapped right value if either was initiated as right with same left and right types`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .fold { it.length }

        assertEquals(initialElement.length, result)
    }

    @Test
    fun `fold with single mapper must return mapped left value if either was initiated as left with same left and right types`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .fold { it.length }

        assertEquals(initialElement.length, result)
    }

    @Test
    fun `to filtered maybe right must return empty maybe if either was initiated as left either`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .toFilteredMaybeRight { it.length == 5 }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to filtered maybe right must return empty maybe if either was initiated as right with a value that is not valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<Int, _>(initialElement)
                .toFilteredMaybeRight { it.isEmpty() }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to filtered maybe right must return maybe contains initial value if either was initiated as right with a value that is valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<Int, _>(initialElement)
                .toFilteredMaybeRight { it.isNotEmpty() }
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `to filtered maybe left must return empty maybe if either was initiated as right either`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<Int, _>(initialElement)
                .toFilteredMaybeLeft { it == 5 }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to filtered maybe left must return empty maybe if either was initiated as left with a value that is not valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, Int>(initialElement)
                .toFilteredMaybeLeft { it.isEmpty() }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to filtered maybe left must return maybe contains initial value if either was initiated as left with a value that is valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, Int>(initialElement)
                .toFilteredMaybeLeft { it.isNotEmpty() }
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `to filtered maybe must return empty maybe if either was initiated as left with value that is not valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .toFilteredMaybe { it.isEmpty() }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to filtered maybe must return maybe contains initial value if either was initiated as left with value that is valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .toFilteredMaybe { it.isNotEmpty() }
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `to filtered maybe must return empty maybe if either was initiated as right with value that is not valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .toFilteredMaybe { it.isEmpty() }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to filtered maybe must return maybe contains initial value if either was initiated as right with value that is valid for the filter`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .toFilteredMaybe { it.isNotEmpty() }
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `to maybe must return maybe contains initial value if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, String>(initialElement)
                .toMaybe()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `to maybe must return maybe contains initial value if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, _>(initialElement)
                .toMaybe()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `as right either must return right either with initial element`() {
        val initialElement = Uuid.random().toString()
        val result =
            initialElement.asRightEither<Int, _>()
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialElement, result)
    }
}