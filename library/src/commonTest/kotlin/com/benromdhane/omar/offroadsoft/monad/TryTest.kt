package com.benromdhane.omar.offroadsoft.monad

import com.benromdhane.omar.offroadsoft.monad.error.Try
import io.kotest.assertions.assertSoftly
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TryTest {

    @Test
    fun `success must return false if try was initiated as failure`() {
        val initialValue = Exception()
        val result =
            Try.seed<Int>(initialValue)
                .success()

        assertFalse { result }
    }

    @Test
    fun `success must return true if try was initiated as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            Try.seed(initialValue)
                .success()

        assertTrue { result }
    }

    @Test
    fun `failure must return true if try was initiated as failure`() {
        val initialValue = Exception()
        val result =
            Try.seed<Int>(initialValue)
                .failure()

        assertTrue { result }
    }

    @Test
    fun `failure must return false if try was initiated as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            Try.seed(initialValue)
                .failure()

        assertFalse { result }
    }

    @Test
    fun `trying must return failure if the provided expression throws an exception`() {
        @Suppress("DIVISION_BY_ZERO")
        val initialValue = { 10 / 0 }
        val result =
            Try.trying(initialValue)
                .failure()

        assertTrue { result }
    }

    @Test
    fun `trying must return success if the provided expression does not throw an exception`() {
        val initialValue = { 10 / 1 }
        val result =
            Try.trying(initialValue)
                .success()

        assertTrue { result }
    }

    @Test
    fun `to maybe success must return empty maybe if the initial try was initiated as failure`() {
        val initialValue = Exception()
        val result =
            Try.seed<Int>(initialValue)
                .toMaybeSuccess()
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to maybe success must return maybe with initial expression if the initial try was initiated as success`() {
        val initialValue = Random.nextInt()
        val result =
            Try.seed(initialValue)
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to maybe failure must return empty maybe if the initial try was initiated as success`() {
        val initialValue = Random.nextInt()
        val result =
            Try.seed(initialValue)
                .toMaybeFailure()
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to maybe failure must return maybe with failure throwable if the initial try was initiated as failure`() {
        val initialValue = Exception(Uuid.random().toString())
        val result =
            Try.seed<Int>(initialValue)
                .toMaybeFailure()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `map success must return transformed value if initial try was initiated as success and mapping operation does not throw any exception`() {
        val initialValue = Uuid.random().toString()
        val result =
            Try.seed(initialValue)
                .mapSuccess { it.length }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue.length, result)
    }

    @Test
    fun `map success must return failure if initial try was initiated as success and mapping operation throws an exception`() {
        val initialValue = Uuid.random().toString()

        @Suppress("DIVISION_BY_ZERO")
        val result =
            Try.seed(initialValue)
                .mapSuccess { it.length / 0 }
                .failure()

        assertTrue { result }
    }

    @Test
    fun `map success must return failure if initial try was initiated as failure and mapping operation does not throw any exception`() {
        val initialValue = Exception(Uuid.random().toString())
        val result =
            Try.seed<String>(initialValue)
                .mapSuccess { it.length }
                .toMaybeFailure()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `map success must return failure if initial try was initiated as failure and mapping operation throws an exception`() {
        val initialValue = Exception(Uuid.random().toString())

        @Suppress("DIVISION_BY_ZERO")
        val result =
            Try.seed<String>(initialValue)
                .mapSuccess { it.length / 0 }
                .toMaybeFailure()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `map failure must return success if initial try was initiated as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            Try.seed(initialValue)
                .mapFailure { Exception(Uuid.random().toString()) }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `map failure must return transformed failure if initial try was initiated as failure`() {
        val initialValue = Exception(Uuid.random().toString())
        val alternativeValue = Exception(Uuid.random().toString())
        val result =
            Try.seed<Int>(initialValue)
                .mapFailure { alternativeValue }
                .toMaybeFailure()
                .orNull()!!

        assertEquals(alternativeValue, result)
    }

    @Test
    fun `recover must return initial value if try was initiated as success`() {
        val initialValue = Uuid.random().toString()
        val alternativeValue = Uuid.random().toString()
        val result =
            Try.seed(initialValue)
                .recover(alternativeValue)
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `recover must return alternative value if try was initiated as failure`() {
        val initialValue = Exception(Uuid.random().toString())
        val alternativeValue = Uuid.random().toString()
        val result =
            Try.seed<String>(initialValue)
                .recover(alternativeValue)
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeValue, result)
    }

    @Test
    fun `recover with alternative seed must return initial value if try was initiated as success`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val alternativeValue = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Try.seed(initialValue)
                .recover(alternativeValue)
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `recover with alternative seed must return alternative value if try was initiated as failure`() {
        val initialValue = Exception(Uuid.random().toString())
        var evaluated = false
        val alternativeSeed = Uuid.random().toString()
        val alternativeValue = {
            evaluated = true
            alternativeSeed
        }
        val result =
            Try.seed<String>(initialValue)
                .recover(alternativeValue)
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeSeed, result)
        }
    }

    @Test
    fun `recover with failure predicate must return initial value if try was initiated as success and failure meet the condition`() {
        val initialValue = Uuid.random().toString()
        val alternativeValue = Uuid.random().toString()
        val result =
            Try.seed(initialValue)
                .recover(alternativeValue) { true }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `recover with failure predicate must return alternative value if try was initiated as failure and failure meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        val alternativeValue = Uuid.random().toString()
        val result =
            Try.seed<String>(initialValue)
                .recover(alternativeValue) { it is Exception }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeValue, result)
    }

    @Test
    fun `recover with failure predicate must return initial value if try was initiated as success and failure does not meet the condition`() {
        val initialValue = Uuid.random().toString()
        val alternativeValue = Uuid.random().toString()
        val result =
            Try.seed(initialValue)
                .recover(alternativeValue) { false }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `recover with failure predicate must return initial failure if try was initiated as failure and failure does not meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        val alternativeValue = Uuid.random().toString()
        val result =
            Try.seed<String>(initialValue)
                .recover(alternativeValue) { it is IllegalArgumentException }
                .toMaybeFailure()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `recover with failure predicate and alternative seed must return initial value if try was initiated as success and failure meet the condition`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val alternativeValue = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Try.seed(initialValue)
                .recover(alternativeValue) { true }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `recover with failure predicate and alternative seed must return alternative value if try was initiated as failure and failure meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        var evaluated = false
        val alternativeSeed = Uuid.random().toString()
        val alternativeValue = {
            evaluated = true
            alternativeSeed
        }
        val result =
            Try.seed<String>(initialValue)
                .recover(alternativeValue) { it is Exception }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeSeed, result)
        }
    }

    @Test
    fun `recover with failure predicate and alternative seed must return initial value if try was initiated as success and failure does not meet the condition`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val alternativeValue = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Try.seed(initialValue)
                .recover(alternativeValue) { false }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `recover with failure predicate and alternative seed must return initial failure if try was initiated as failure and failure does not meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        var evaluated = false
        val alternativeValue = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Try.seed<String>(initialValue)
                .recover(alternativeValue) { it is IllegalArgumentException }
                .toMaybeFailure()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `recover with failure type must return initial value if try was initiated as success and failure meet the condition`() {
        val initialValue = Uuid.random().toString()
        val alternativeValue = Uuid.random().toString()
        val result =
            Try.seed(initialValue)
                .recover(alternativeValue, Throwable::class)
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `recover with failure type must return alternative value if try was initiated as failure and failure meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        val alternativeValue = Uuid.random().toString()
        val result =
            Try.seed<String>(initialValue)
                .recover(alternativeValue, Exception::class)
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeValue, result)
    }

    @Test
    fun `recover with failure type must return initial value if try was initiated as success and failure does not meet the condition`() {
        val initialValue = Uuid.random().toString()
        val alternativeValue = Uuid.random().toString()
        val result =
            Try.seed(initialValue)
                .recover(alternativeValue, IllegalArgumentException::class)
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `recover with failure type must return initial failure if try was initiated as failure and failure does not meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        val alternativeValue = Uuid.random().toString()
        val result =
            Try.seed<String>(initialValue)
                .recover(alternativeValue, IllegalArgumentException::class)
                .toMaybeFailure()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `recover with failure type and alternative seed must return initial value if try was initiated as success and failure meet the condition`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val alternativeValue = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Try.seed(initialValue)
                .recover(alternativeValue, Throwable::class)
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `recover with failure type and alternative seed must return alternative value if try was initiated as failure and failure meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        var evaluated = false
        val alternativeSeed = Uuid.random().toString()
        val alternativeValue = {
            evaluated = true
            alternativeSeed
        }
        val result =
            Try.seed<String>(initialValue)
                .recover(alternativeValue, Exception::class)
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeSeed, result)
        }
    }

    @Test
    fun `recover with failure type and alternative seed must return initial value if try was initiated as success and failure does not meet the condition`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val alternativeValue = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Try.seed(initialValue)
                .recover(alternativeValue, IllegalArgumentException::class)
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `recover with failure type and alternative seed must return initial failure if try was initiated as failure and failure does not meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        var evaluated = false
        val alternativeValue = {
            evaluated = true
            Uuid.random().toString()
        }
        val result =
            Try.seed<String>(initialValue)
                .recover(alternativeValue, IllegalArgumentException::class)
                .toMaybeFailure()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `filter success must return initial value if try was initiated as failure and success meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        val alternativeValue = Exception(Uuid.random().toString())
        val result =
            Try.seed<String>(initialValue)
                .filterSuccess(alternativeValue) { true }
                .toMaybeFailure()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `filter success must return initial value if try was initiated as failure and success does not meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        val alternativeValue = Exception(Uuid.random().toString())
        val result =
            Try.seed<String>(initialValue)
                .filterSuccess(alternativeValue) { false }
                .toMaybeFailure()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `filter success must return initial value if try was initiated as success and success meet the condition`() {
        val initialValue = Uuid.random().toString()
        val alternativeValue = Exception(Uuid.random().toString())
        val result =
            Try.seed(initialValue)
                .filterSuccess(alternativeValue) { it.isNotEmpty() }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `filter success must return alternative failure if try was initiated as success and success does not meet the condition`() {
        val initialValue = Uuid.random().toString()
        val alternativeValue = Exception(Uuid.random().toString())
        val result =
            Try.seed(initialValue)
                .filterSuccess(alternativeValue) { it.isEmpty() }
                .toMaybeFailure()
                .orNull()!!

        assertEquals(alternativeValue, result)
    }

    @Test
    fun `filter success not must return initial value if try was initiated as failure and success meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        val alternativeValue = Exception(Uuid.random().toString())
        val result =
            Try.seed<String>(initialValue)
                .filterSuccessNot(alternativeValue) { true }
                .toMaybeFailure()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `filter success not must return initial value if try was initiated as failure and success does not meet the condition`() {
        val initialValue = Exception(Uuid.random().toString())
        val alternativeValue = Exception(Uuid.random().toString())
        val result =
            Try.seed<String>(initialValue)
                .filterSuccessNot(alternativeValue) { false }
                .toMaybeFailure()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `filter success not must return alternative failure if try was initiated as success and success meet the condition`() {
        val initialValue = Uuid.random().toString()
        val alternativeValue = Exception(Uuid.random().toString())
        val result =
            Try.seed(initialValue)
                .filterSuccessNot(alternativeValue) { it.isNotEmpty() }
                .toMaybeFailure()
                .orNull()!!

        assertEquals(alternativeValue, result)
    }

    @Test
    fun `filter success not must return initial value if try was initiated as success and success does not meet the condition`() {
        val initialValue = Uuid.random().toString()
        val alternativeValue = Exception(Uuid.random().toString())
        val result =
            Try.seed(initialValue)
                .filterSuccessNot(alternativeValue) { it.isEmpty() }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }
}