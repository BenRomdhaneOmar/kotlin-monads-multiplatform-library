package com.benromdhane.omar.offroadsoft.monad

import com.benromdhane.omar.offroadsoft.monad.error.Try
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
                .orNull()

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
                .orNull()

        assertEquals(initialValue, result)
    }
}