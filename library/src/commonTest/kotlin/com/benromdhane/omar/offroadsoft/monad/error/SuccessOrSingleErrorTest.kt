package com.benromdhane.omar.offroadsoft.monad.error

import io.kotest.assertions.assertSoftly
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

    @Test
    fun `map success must transform the initial success value if success or single error was initiated as success`() {
        val initialSuccess = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .mapSuccess {
                    evaluated = true
                    it.length
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(initialSuccess.length, result)
        }
    }

    @Test
    fun `map success must be ignored if success or single error was initiated as error`() {
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .mapSuccess {
                    evaluated = true
                    it.length
                }
                .error()

        assertSoftly {
            assertFalse { evaluated }
            assertTrue { result }
        }
    }

    @Test
    fun `map error must transform the initial error value if success or single error was initiated as error`() {
        val initialError = Exception(Uuid.random().toString())
        val newError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .mapError {
                    evaluated = true
                    newError
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(newError, result)
        }
    }

    @Test
    fun `map error must be ignored if success or single error was initiated as success`() {
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .mapError {
                    evaluated = true
                    it.message!!
                }
                .success()

        assertSoftly {
            assertFalse { evaluated }
            assertTrue { result }
        }
    }

    @Test
    fun `flat map success must return error with initial error if initial success or single error was initiated as error and mapping result is error success or single error`() {
        val initialError = Exception(Uuid.random().toString())
        val newError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess {
                    evaluated = true
                    SuccessOrSingleError.Error.of<String, _>(newError)
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `flat map success must return error with initial error if initial success or single error was initiated as error and mapping result is success success or single error`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess {
                    evaluated = true
                    SuccessOrSingleError.Success.of(Uuid.random().toString())
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `flat map success must return error if initial success or single error was initiated as success and mapping result is error success or single error`() {
        val error = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .flatMapSuccess {
                    evaluated = true
                    SuccessOrSingleError.Error.of<String, _>(error)
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(error, result)
        }
    }

    @Test
    fun `flat map success must return success if initial success or single error was initiated as success and mapping result is success success or single error`() {
        val initialSuccess = Uuid.random().toString()
        val newSuccess = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .flatMapSuccess {
                    evaluated = true
                    SuccessOrSingleError.Success.of(newSuccess)
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(newSuccess, result)
        }
    }
}