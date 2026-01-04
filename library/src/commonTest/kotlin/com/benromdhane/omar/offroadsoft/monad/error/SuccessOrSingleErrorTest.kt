package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Either
import com.benromdhane.omar.offroadsoft.monad.Maybe
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
    fun `filter success must return error with alternative error if initial success or single error was initiated as success and filter is not valid`() {
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
    fun `filter success with error provider must return error with alternative error if initial success or single error was initiated as success and filter is not valid`() {
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
    fun `filter success not must return error with alternative error if initial success or single error was initiated as success and filter is valid`() {
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

    @Test
    fun `filter success not with error provider must be ignored if initial success or single error was initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        var conditionEvaluated = false
        var providerEvaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .filterSuccessNot(
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
    fun `filter success not with error provider must be ignored if initial success or single error was initiated as error and filter is valid`() {
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
    fun `filter success not with error provider must return error with alternative error if initial success or single error was initiated as success and filter is valid`() {
        var conditionEvaluated = false
        var providerEvaluated = false
        val alternativeError = Exception(Uuid.random().toString())
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Random.nextInt(100, 1_000))
                .filterSuccessNot(
                    {
                        providerEvaluated = true
                        alternativeError
                    },
                    {
                        conditionEvaluated = true
                        it >= 100
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
    fun `filter success not with error provider must return success with initial value if initial success or single error was initiated as success and filter is not valid`() {
        val initialSuccess = Random.nextInt(100, 1_000)
        var conditionEvaluated = false
        var providerEvaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .filterSuccessNot(
                    {
                        providerEvaluated = true
                        Exception(Uuid.random().toString())
                    },
                    {
                        conditionEvaluated = true
                        it < 100
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
    fun `to success must return success with initial value if success or single error was initiated as success`() {
        val initialSuccess = Uuid.random().toString()
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toSuccess(Uuid.random().toString())
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialSuccess, result)
    }

    @Test
    fun `to success must return success with alternative value if success or single error was initiated as failure`() {
        val alternativeSuccess = Uuid.random().toString()
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .toSuccess(alternativeSuccess)
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeSuccess, result)
    }

    @Test
    fun `to success with alternative provider must return success with initial value if success or single error was initiated as success`() {
        val initialSuccess = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toSuccess {
                    evaluated = true
                    Uuid.random().toString()
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialSuccess, result)
        }
    }

    @Test
    fun `to success with alternative provider must return success with alternative value if success or single error was initiated as failure`() {
        val alternativeSuccess = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .toSuccess {
                    evaluated = true
                    alternativeSuccess
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeSuccess, result)
        }
    }

    @Test
    fun `to success with error condition must return success with initial success if initial success or single error was initiated as success and condition is not valid`() {
        val initialSuccess = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toSuccess(Uuid.random().toString()) {
                    evaluated = true
                    false
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialSuccess, result)
        }
    }

    @Test
    fun `to success with error condition must return success with initial success if initial success or single error was initiated as success and condition is valid`() {
        val initialSuccess = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toSuccess(Uuid.random().toString()) {
                    evaluated = true
                    true
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialSuccess, result)
        }
    }

    @Test
    fun `to success with error condition must return error if initial success or single error was initiated as error and condition is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .toSuccess(Uuid.random().toString()) {
                    evaluated = true
                    false
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `to success with error condition must return success with alternative success if initial success or single error was initiated as error and condition is valid`() {
        val alternativeSuccess = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .toSuccess(alternativeSuccess) {
                    evaluated = true
                    true
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeSuccess, result)
        }
    }

    @Test
    fun `to success with error condition and alternative success provider must return success with initial success if initial success or single error was initiated as success and condition is not valid`() {
        val initialSuccess = Uuid.random().toString()
        var conditionEvaluated = false
        var alternativeEvaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toSuccess(
                    {
                        alternativeEvaluated = true
                        Uuid.random().toString()
                    },
                    {
                        conditionEvaluated = true
                        false
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { alternativeEvaluated }
            assertFalse { conditionEvaluated }
            assertEquals(initialSuccess, result)
        }
    }

    @Test
    fun `to success with error condition and alternative success provider must return success with initial success if initial success or single error was initiated as success and condition is valid`() {
        val initialSuccess = Uuid.random().toString()
        var alternativeEvaluated = false
        var conditionEvaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toSuccess(
                    {
                        alternativeEvaluated = true
                        Uuid.random().toString()
                    },
                    {
                        conditionEvaluated = true
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { alternativeEvaluated }
            assertFalse { conditionEvaluated }
            assertEquals(initialSuccess, result)
        }
    }

    @Test
    fun `to success with error condition and alternative success provider must return error if initial success or single error was initiated as error and condition is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        var alternativeEvaluated = false
        var conditionEvaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .toSuccess(
                    {
                        alternativeEvaluated = true
                        Uuid.random().toString()
                    },
                    {
                        conditionEvaluated = true
                        false
                    }
                )
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { alternativeEvaluated }
            assertTrue { conditionEvaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `to success with error condition and alternative success provider must return success with alternative success if initial success or single error was initiated as error and condition is valid`() {
        val alternativeSuccess = Uuid.random().toString()
        var alternativeEvaluated = false
        var conditionEvaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .toSuccess(
                    {
                        alternativeEvaluated = true
                        alternativeSuccess
                    },
                    {
                        conditionEvaluated = true
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { alternativeEvaluated }
            assertTrue { conditionEvaluated }
            assertEquals(alternativeSuccess, result)
        }
    }

    @Test
    fun `to success with error type condition must return success with initial success if initial success or single error was initiated as success and type is not valid`() {
        val initialSuccess = Uuid.random().toString()
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toSuccess(
                    Uuid.random().toString(),
                    IllegalArgumentException::class
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialSuccess, result)
    }

    @Test
    fun `to success with error type condition must return success with initial success if initial success or single error was initiated as success and type is valid`() {
        val initialSuccess = Uuid.random().toString()
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toSuccess(
                    Uuid.random().toString(),
                    Throwable::class
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialSuccess, result)
    }

    @Test
    fun `to success with error type condition must return error if initial success or single error was initiated as error and type is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .toSuccess(
                    Uuid.random().toString(),
                    IllegalArgumentException::class
                )
                .toMaybeError()
                .orNull()!!

        assertEquals(initialError, result)
    }

    @Test
    fun `to success with error type condition must return success with alternative success if initial success or single error was initiated as error and type is valid`() {
        val alternativeSuccess = Uuid.random().toString()
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .toSuccess(
                    alternativeSuccess,
                    Exception::class
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeSuccess, result)
    }

    @Test
    fun `to success with error type condition and alternative success provider must return success with initial success if initial success or single error was initiated as success and type is not valid`() {
        val initialSuccess = Uuid.random().toString()
        var alternativeEvaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toSuccess(IllegalArgumentException::class) {
                    alternativeEvaluated = true
                    Uuid.random().toString()
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { alternativeEvaluated }
            assertEquals(initialSuccess, result)
        }
    }

    @Test
    fun `to success with error type condition and alternative success provider must return success with initial success if initial success or single error was initiated as success and type is valid`() {
        val initialSuccess = Uuid.random().toString()
        var alternativeEvaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toSuccess(Throwable::class) {
                    alternativeEvaluated = true
                    Uuid.random().toString()
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { alternativeEvaluated }
            assertEquals(initialSuccess, result)
        }
    }

    @Test
    fun `to success with error type condition and alternative success provider must return error if initial success or single error was initiated as error and type is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        var alternativeEvaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .toSuccess(IllegalArgumentException::class) {
                    alternativeEvaluated = true
                    Uuid.random().toString()
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { alternativeEvaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `to success with error type condition and alternative success provider must return success with alternative success if initial success or single error was initiated as error and type is valid`() {
        val alternativeSuccess = Uuid.random().toString()
        var alternativeEvaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .toSuccess(Exception::class) {
                    alternativeEvaluated = true
                    alternativeSuccess
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { alternativeEvaluated }
            assertEquals(alternativeSuccess, result)
        }
    }

    @Test
    fun `to either must return left either with error value if success or single error was initiated as error`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .toEither()
                .toMaybeLeft()
                .orNull()!!

        assertEquals(initialError, result)
    }

    @Test
    fun `to either must return right either with success value if success or single error was initiated as success`() {
        val initialSuccess = Uuid.random().toString()
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .toEither()
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialSuccess, result)
    }

    @Test
    fun `fold must return mapped error value if success or single error was initiated as error`() {
        val initialError = Exception(Uuid.random().toString())
        val errorResult = Random.nextInt()
        var successMapperEvaluated = false
        var errorMapperEvaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .fold(
                    {
                        successMapperEvaluated = true
                        Random.nextInt()
                    },
                    {
                        errorMapperEvaluated = true
                        errorResult
                    }
                )

        assertSoftly {
            assertFalse { successMapperEvaluated }
            assertTrue { errorMapperEvaluated }
            assertEquals(errorResult, result)
        }
    }

    @Test
    fun `to fold must return mapped success value if success or single error was initiated as success`() {
        val initialSuccess = Uuid.random().toString()
        val successResult = Random.nextInt()
        var successMapperEvaluated = false
        var errorMapperEvaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialSuccess)
                .fold(
                    {
                        successMapperEvaluated = true
                        successResult
                    },
                    {
                        errorMapperEvaluated = true
                        Random.nextInt()
                    }
                )

        assertSoftly {
            assertTrue { successMapperEvaluated }
            assertFalse { errorMapperEvaluated }
            assertEquals(successResult, result)
        }
    }

    @Test
    fun `try as success or single error must return success success or single error if try was initiated as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            Try.seed(initialValue)
                .asSuccessOrSingleError()
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `try as success or single error must return error success or single error if try was initiated as failure`() {
        val initialValue = Exception(Uuid.random().toString())
        val result =
            Try.seed<String>(initialValue)
                .asSuccessOrSingleError()
                .toMaybeError()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `either as success or single error with right as success must return success success or single error if either was initiated as right`() {
        val initialValue = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<Int, _>(initialValue)
                .asSuccessOrSingleErrorWithRightAsSuccess()
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `either as success or single error with right as success must return error success or single error if either was initiated as left`() {
        val initialValue = Exception(Uuid.random().toString())
        val result =
            Either
                .Left
                .of<_, Int>(initialValue)
                .asSuccessOrSingleErrorWithRightAsSuccess()
                .toMaybeError()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `either as success or single error with left as success must return success success or single error if either was initiated as left`() {
        val initialValue = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<_, Int>(initialValue)
                .asSuccessOrSingleErrorWithLeftAsSuccess()
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `either as success or single error with left as success must return error success or single error if either was initiated as right`() {
        val initialValue = Exception(Uuid.random().toString())
        val result =
            Either
                .Right
                .of<Int, _>(initialValue)
                .asSuccessOrSingleErrorWithLeftAsSuccess()
                .toMaybeError()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `success or single error as try must return success if it was initiated as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialValue)
                .asTry()
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `success or single error as try must return failure if it was initiated as error`() {
        val initialValue = Exception(Uuid.random().toString())
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialValue)
                .asTry()
                .toMaybeFailure()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `flat map success with possible error as mapper result must return possible error with initial error if success or single error was initiated as error and mapper result is error`() {
        val initialError = Exception(Uuid.random().toString())
        val newError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .flatMapSuccessToPossibleError {
                    evaluated = true
                    PossibleError.Error.of(newError)
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `flat map success with possible error as mapper result must return possible error with initial error if success or single error was initiated as error and mapper result is success`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .flatMapSuccessToPossibleError {
                    evaluated = true
                    PossibleError.Success.of()
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `flat map success with possible error as mapper result must return possible error with mapper error if success or single error was initiated as success and mapper result is error`() {
        val initialValue = Uuid.random().toString()
        val error = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialValue)
                .flatMapSuccessToPossibleError {
                    evaluated = true
                    PossibleError.Error.of(error)
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(error, result)
        }
    }

    @Test
    fun `flat map success with possible error as mapper result must return possible error success if success or single error was initiated as success and mapper result is success`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialValue)
                .flatMapSuccessToPossibleError {
                    evaluated = true
                    PossibleError.Success.of()
                }
                .error()

        assertSoftly {
            assertTrue { evaluated }
            assertFalse { result }
        }
    }

    @Test
    fun `flat map success with maybe error as mapper result must return maybe with initial error if success or single error was initiated as error and mapper result is not empty`() {
        val initialError = Exception(Uuid.random().toString())
        val newError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .flatMapSuccessToMaybe {
                    evaluated = true
                    Maybe.NotEmpty.of(newError)
                }
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `flat map success with maybe error as mapper result must return maybe with initial error if success or single error was initiated as error and mapper result is empty`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .flatMapSuccessToMaybe {
                    evaluated = true
                    Maybe.Empty.of()
                }
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialError, result)
        }
    }

    @Test
    fun `flat map success with maybe error as mapper result must return maybe with mapper error if success or single error was initiated as success and mapper result is not empty`() {
        val initialValue = Uuid.random().toString()
        val error = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialValue)
                .flatMapSuccessToMaybe {
                    evaluated = true
                    Maybe.NotEmpty.of(error)
                }
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(error, result)
        }
    }

    @Test
    fun `flat map success with maybe error as mapper result must return empty maybe if success or single error was initiated as success and mapper result is empty`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(initialValue)
                .flatMapSuccessToMaybe {
                    evaluated = true
                    Maybe.Empty.of()
                }
                .empty()

        assertSoftly {
            assertTrue { evaluated }
            assertTrue { result }
        }
    }

    @Test
    fun `as possible error must return success possible error if initial success or single error is success`() {
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .asPossibleError()
                .error()

        assertFalse { result }
    }

    @Test
    fun `as possible error must return error possible error if initial success or single error is error`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .asPossibleError()
                .toMaybeError()
                .orNull()!!

        assertEquals(initialError, result)
    }
}