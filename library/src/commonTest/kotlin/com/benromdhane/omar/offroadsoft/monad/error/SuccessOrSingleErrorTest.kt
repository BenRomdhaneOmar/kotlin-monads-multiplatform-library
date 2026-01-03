package com.benromdhane.omar.offroadsoft.monad.error

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SuccessOrSingleErrorTest {

    @Test
    fun `success must return false if success of single error was initiated as error`() {
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .success()

        assertFalse { result }
    }

    @Test
    fun `success must return true if success of single error was initiated as success`() {
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .success()

        assertTrue { result }
    }

    @Test
    fun `error must return true if success of single error was initiated as error`() {
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .error()

        assertTrue { result }
    }

    @Test
    fun `error must return false if success of single error was initiated as success`() {
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .error()

        assertFalse { result }
    }

    @Test
    fun `to maybe success must return empty maybe if success of single error was initiated as error`() {
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .toMaybeSuccess()
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to maybe success must return maybe with initial value if success of single error was initiated as success`() {
        val initialSuccess = Uuid.random().toString()
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialSuccess, result)
    }

    @Test
    fun `to maybe error must return maybe with initial error if success of single error was initiated as error`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .toMaybeError()
                .orNull()!!

        assertEquals(initialError, result)
    }

    @Test
    fun `to maybe error must return empty maybe if success of single error was initiated as success`() {
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .toMaybeError()
                .empty()

        assertTrue { result }
    }
}