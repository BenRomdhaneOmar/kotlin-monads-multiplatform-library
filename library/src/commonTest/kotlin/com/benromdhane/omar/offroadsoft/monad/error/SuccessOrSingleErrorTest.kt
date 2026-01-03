package com.benromdhane.omar.offroadsoft.monad.error

import io.kotest.assertions.assertSoftly
import kotlin.random.Random
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

    @Test
    fun `filter success must be ignored if initial success or single error was initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .filterSuccess(Exception(Uuid.random().toString())) {
                    evaluated = true
                    false
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `filter success must be ignored if initial success or single error was initiated as error and filter is valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .filterSuccess(Exception(Uuid.random().toString())) {
                    evaluated = true
                    true
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `filter success must return success with initial value if initial success or single error was initiated as success and filter is valid`() {
        val initialSuccess = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .filterSuccess(Exception(Uuid.random().toString())) {
                    evaluated = true
                    it.isNotEmpty()
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(initialSuccess, result)
        }
    }

    @Test
    fun `filter success must return error with initial alternative error if initial success or single error was initiated as success and filter is not valid`() {
        var evaluated = false
        val alternativeError = Exception(Uuid.random().toString())
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .filterSuccess(alternativeError) {
                    evaluated = true
                    it.isEmpty()
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeError, result)
        }
    }

    @Test
    fun `filter success with error provider must be ignored if initial success or single error was initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        var conditionEvaluated = false
        var providerEvaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .filterSuccess(
                    {
                        providerEvaluated = true
                        Exception(Uuid.random().toString())
                    },
                    {
                        conditionEvaluated = true
                        false
                    }
                )
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { conditionEvaluated }
            assertFalse { providerEvaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `filter success with error provider must be ignored if initial success or single error was initiated as error and filter is valid`() {
        val initialError = Exception(Uuid.random().toString())
        var conditionEvaluated = false
        var providerEvaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .filterSuccess(
                    {
                        providerEvaluated = true
                        Exception(Uuid.random().toString())
                    },
                    {
                        conditionEvaluated = true
                        true
                    }
                )
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { conditionEvaluated }
            assertFalse { providerEvaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `filter success with error provider must return success with initial value if initial success or single error was initiated as success and filter is valid`() {
        val initialSuccess = Uuid.random().toString()
        var conditionEvaluated = false
        var providerEvaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .filterSuccess(
                    {
                        providerEvaluated = true
                        Exception(Uuid.random().toString())
                    },
                    {
                        conditionEvaluated = true
                        it.isNotEmpty()
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { conditionEvaluated }
            assertFalse { providerEvaluated }
            assertEquals(initialSuccess, result)
        }
    }

    @Test
    fun `filter success with error provider must return error with initial alternative error if initial success or single error was initiated as success and filter is not valid`() {
        var conditionEvaluated = false
        var providerEvaluated = false
        val alternativeError = Exception(Uuid.random().toString())
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .filterSuccess(
                    {
                        providerEvaluated = true
                        alternativeError
                    },
                    {
                        conditionEvaluated = true
                        it.isEmpty()
                    }
                )
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertTrue { conditionEvaluated }
            assertTrue { providerEvaluated }
            assertEquals(alternativeError, result)
        }
    }

    @Test
    fun `filter success not must be ignored if initial success or single error was initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .filterSuccessNot(Exception(Uuid.random().toString())) {
                    evaluated = true
                    false
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `filter success not must be ignored if initial success or single error was initiated as error and filter is valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .filterSuccessNot(Exception(Uuid.random().toString())) {
                    evaluated = true
                    true
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `filter success not must return error with initial alternative error if initial success or single error was initiated as success and filter is valid`() {
        var evaluated = false
        val alternativeError = Exception(Uuid.random().toString())
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Random.nextInt(100, 1_000))
                .filterSuccessNot(alternativeError) {
                    evaluated = true
                    it >= 100
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeError, result)
        }
    }

    @Test
    fun `filter success not must return success with initial value if initial success or single error was initiated as success and filter is not valid`() {
        val initialSuccess = Random.nextInt(100, 1_000)
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .filterSuccessNot(Exception(Uuid.random().toString())) {
                    evaluated = true
                    it < 100
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(initialSuccess, result)
        }
    }
}