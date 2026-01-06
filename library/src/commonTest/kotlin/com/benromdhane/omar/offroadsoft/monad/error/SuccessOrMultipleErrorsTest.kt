package com.benromdhane.omar.offroadsoft.monad.error

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainAll
import kotlin.test.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SuccessOrMultipleErrorsTest {

    @Test
    fun `error must return true if success or multiple errors created as error`() {
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .error()

        assertTrue { result }
    }

    @Test
    fun `error must return false if success or multiple errors created as success`() {
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .error()

        assertFalse { result }
    }

    @Test
    fun `success must return false if success or multiple errors created as error`() {
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .success()

        assertFalse { result }
    }

    @Test
    fun `success must return true if success or multiple errors created as success`() {
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .success()

        assertTrue { result }
    }

    @Test
    fun `to maybe success must return empty maybe if success or multiple errors created as error`() {
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .toMaybeSuccess()
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to maybe success must return maybe with initial value if success or multiple errors created as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to errors must return errors collection contain initial error if success or multiple errors created as error`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `to errors must return empty collection if success or multiple errors created as success`() {
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .toErrors()
                .isEmpty()

        assertTrue { result }
    }

    @Test
    fun `map success must be ignored if success or multiple errors created as error`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .mapSuccess {
                    evaluated = true
                    it.length
                }
                .toErrors()

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `map success must transform initial value if success or multiple errors created as success`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .mapSuccess {
                    evaluated = true
                    it.length
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(initialValue.length, result)
        }
    }

    @Test
    fun `add error must append the error to the initial errors if success or multiple errors created as error`() {
        val initialError = Exception(Uuid.random().toString())
        val additionalError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(additionalError)
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, additionalError)
        }
    }

    @Test
    fun `add error must be ignored if success or multiple errors created as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .addError(Exception(Uuid.random().toString()))
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `add errors with collection must append the errors to the initial errors if success or multiple errors created as error`() {
        val initialError = Exception(Uuid.random().toString())
        val additionalErrors = listOf(Exception(Uuid.random().toString()))
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addErrors(additionalErrors)
                .toErrors()

        assertSoftly {
            assertEquals(1 + additionalErrors.size, result.size)
            assertContains(result, initialError)
            result.shouldContainAll(additionalErrors)
        }
    }

    @Test
    fun `add error with collection must be ignored if success or multiple errors created as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .addErrors(listOf(Exception(Uuid.random().toString())))
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `add errors with varargs must append the errors to the initial errors if success or multiple errors created as error`() {
        val initialError = Exception(Uuid.random().toString())
        val additionalErrors = listOf(Exception(Uuid.random().toString()))
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addErrors(*additionalErrors.toTypedArray())
                .toErrors()

        assertSoftly {
            assertEquals(1 + additionalErrors.size, result.size)
            assertContains(result, initialError)
            result.shouldContainAll(additionalErrors)
        }
    }

    @Test
    fun `add error with varargs must be ignored if success or multiple errors created as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .addErrors(*listOf(Exception(Uuid.random().toString())).toTypedArray())
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `flat map success must return initial errors if success or multiple errors initiated as error and mapping result is error`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess {
                    SuccessOrMultipleErrors
                        .Error
                        .of(Exception(Uuid.random().toString()))
                }
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `flat map success must return initial errors if success or multiple errors initiated as error and mapping result is success`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess {
                    SuccessOrMultipleErrors
                        .Success
                        .of(Uuid.random().toString())
                }
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `flat map success must return mapping errors if success or multiple errors initiated as success and mapping result is error`() {
        val mappingError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .flatMapSuccess {
                    SuccessOrMultipleErrors
                        .Error
                        .of<String, _>(mappingError)
                }
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, mappingError)
        }
    }

    @Test
    fun `flat map success must return mapped success if success or multiple errors initiated as success and mapping result is success`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .flatMapSuccess {
                    SuccessOrMultipleErrors
                        .Success
                        .of(it.length)
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue.length, result)
    }

    @Test
    fun `filter success must return initial errors if success or multiple errors initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccess(
                    Exception(Uuid.random().toString())
                ) {
                    false
                }
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success must return initial errors if success or multiple errors initiated as error and filter is valid`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccess(
                    Exception(Uuid.random().toString())
                ) {
                    true
                }
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success must return success if success or multiple errors initiated as success and filter is valid`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .filterSuccess(
                    Exception(Uuid.random().toString())
                )
                {
                    true
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `filter success must return alternative error if success or multiple errors initiated as success and filter is not valid`() {
        val alternativeError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .filterSuccess(
                    alternativeError
                ) {
                    false
                }
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, alternativeError)
        }
    }

    @Test
    fun `filter success with alternative error provider must return initial errors if success or multiple errors initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccess(
                    {
                        Exception(Uuid.random().toString())
                    },
                    {
                        false
                    }
                )
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success with alternative error provider must return initial errors if success or multiple errors initiated as error and filter is valid`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccess(
                    {
                        Exception(Uuid.random().toString())
                    },
                    {
                        true
                    }
                )
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success with alternative error provider must return success if success or multiple errors initiated as success and filter is valid`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .filterSuccess(
                    {
                        Exception(Uuid.random().toString())
                    },
                    {
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `filter success with alternative error provider must return alternative error if success or multiple errors initiated as success and filter is not valid`() {
        val alternativeError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .filterSuccess(
                    {
                        alternativeError
                    },
                    {
                        false
                    }
                )
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, alternativeError)
        }
    }

    @Test
    fun `filter success not must return initial errors if success or multiple errors initiated as error and filter is valid`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccessNot(
                    Exception(Uuid.random().toString())
                ) {
                    true
                }
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success not must return initial errors if success or multiple errors initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccessNot(
                    Exception(Uuid.random().toString())
                ) {
                    false
                }
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success not must return success if success or multiple errors initiated as success and filter is not valid`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .filterSuccessNot(
                    Exception(Uuid.random().toString())
                )
                {
                    false
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `filter success not must return alternative error if success or multiple errors initiated as success and filter is valid`() {
        val alternativeError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .filterSuccessNot(
                    alternativeError
                ) {
                    true
                }
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, alternativeError)
        }
    }

    @Test
    fun `filter success not with alternative error provider must return initial errors if success or multiple errors initiated as error and filter is valid`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccessNot(
                    {
                        Exception(Uuid.random().toString())
                    },
                    {
                        true
                    }
                )
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success not with alternative error provider must return initial errors if success or multiple errors initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccessNot(
                    {
                        Exception(Uuid.random().toString())
                    },
                    {
                        false
                    }
                )
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success not with alternative error provider must return success if success or multiple errors initiated as success and filter is not valid`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .filterSuccessNot(
                    {
                        Exception(Uuid.random().toString())
                    },
                    {
                        false
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `filter success not with alternative error provider must return alternative error if success or multiple errors initiated as success and filter is valid`() {
        val alternativeError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .filterSuccessNot(
                    {
                        alternativeError
                    },
                    {
                        true
                    }
                )
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, alternativeError)
        }
    }

    @Test
    fun `to success must be ignored if success or multiple errors initiated as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccess(Uuid.random().toString())
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success must return alternative success if success or multiple errors initiated as error`() {
        val alternativeValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .toSuccess(alternativeValue)
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeValue, result)
    }

    @Test
    fun `to success with provider must be ignored if success or multiple errors initiated as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccess { Uuid.random().toString() }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success with provider must return alternative success if success or multiple errors initiated as error`() {
        val alternativeValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .toSuccess { alternativeValue }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeValue, result)
    }

    @Test
    fun `to success if all errors must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAllErrors(
                    Uuid.random().toString()
                ) {
                    false
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if all errors must return success with initial success if initial success or multiple errors initiated as success and condition is valid for all errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAllErrors(
                    Uuid.random().toString()
                ) {
                    true
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if all errors must return error if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfAllErrors(
                    Uuid.random().toString()
                ) {
                    it is IllegalArgumentException
                }
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if all errors must return success with alternative success if initial success or multiple errors initiated as error and condition is valid for all errors`() {
        val alternativeSuccess = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(Exception(Uuid.random().toString()))
                .toSuccessIfAllErrors(
                    alternativeSuccess
                ) {
                    true
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeSuccess, result)
    }

    @Test
    fun `to success if all errors with alternative success provider must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAllErrors(
                    {
                        Uuid.random().toString()
                    },
                    {
                        false
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if all errors with alternative success provider must return success with initial success if initial success or multiple errors initiated as success and condition is valid for all errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAllErrors(
                    {
                        Uuid.random().toString()
                    },
                    {
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if all errors with alternative success provider must return error if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfAllErrors(
                    {
                        Uuid.random().toString()
                    },
                    {
                        it is IllegalArgumentException
                    }
                )
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if all error with alternative success provider must return success with alternative success if initial success or multiple errors initiated as error and condition is valid for all errors`() {
        val alternativeSuccess = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(Exception(Uuid.random().toString()))
                .toSuccessIfAllErrors(
                    {
                        alternativeSuccess
                    },
                    {
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeSuccess, result)
    }

    @Test
    fun `to success if any error must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAnyError(
                    Uuid.random().toString()
                ) {
                    false
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if any error must return success with initial success if initial success or multiple errors initiated as success and condition is valid for at least one of errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAnyError(
                    Uuid.random().toString()
                ) {
                    true
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if any error must return error if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfAnyError(
                    Uuid.random().toString()
                ) {
                    false
                }
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if any error must return success with alternative success if initial success or multiple errors initiated as error and condition is valid for at least one of errors`() {
        val alternativeSuccess = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(IllegalArgumentException(Uuid.random().toString()))
                .toSuccessIfAnyError(
                    alternativeSuccess
                ) {
                    it is IllegalArgumentException
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeSuccess, result)
    }

    @Test
    fun `to success if any error with success alternative provider must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAnyError(
                    {
                        Uuid.random().toString()
                    },
                    {
                        false
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if any error with success alternative provider must return success with initial success if initial success or multiple errors initiated as success and condition is valid for at least one of errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAnyError(
                    {
                        Uuid.random().toString()
                    },
                    {
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if any error with success alternative provider must return error if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfAnyError(
                    {
                        Uuid.random().toString()
                    },
                    {
                        false
                    }
                )
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if any error with success alternative provider must return success with alternative success if initial success or multiple errors initiated as error and condition is valid for at least one of errors`() {
        val alternativeSuccess = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(IllegalArgumentException(Uuid.random().toString()))
                .toSuccessIfAnyError(
                    {
                        alternativeSuccess
                    },
                    {
                        it is IllegalArgumentException
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeSuccess, result)
    }

    @Test
    fun `to success if none of errors must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfNoneOfErrors(
                    Uuid.random().toString()
                ) {
                    false
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if none of errors must return success with initial success if initial success or multiple errors initiated as success and condition is valid for at least one of errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfNoneOfErrors(
                    Uuid.random().toString()
                ) {
                    true
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if none of errors must return error if initial success or multiple errors initiated as error and condition is not valid for at least one of errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfNoneOfErrors(
                    Uuid.random().toString()
                ) {
                    it is IllegalArgumentException
                }
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if none of errors must return success with alternative success if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val alternativeSuccess = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(Exception(Uuid.random().toString()))
                .toSuccessIfNoneOfErrors(
                    alternativeSuccess
                ) {
                    false
                }
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeSuccess, result)
    }

    @Test
    fun `to success if none of errors with success alternative provider must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfNoneOfErrors(
                    {
                        Uuid.random().toString()
                    },
                    {
                        false
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if none of errors with success alternative provider must return success with initial success if initial success or multiple errors initiated as success and condition is valid for at least one of errors`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfNoneOfErrors(
                    {
                        Uuid.random().toString()
                    },
                    {
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `to success if none of errors with success alternative provider must return error if initial success or multiple errors initiated as error and condition is not valid for at least one of errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfNoneOfErrors(
                    {
                        Uuid.random().toString()
                    },
                    {
                        it is IllegalArgumentException
                    }
                )
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if none of errors with success alternative provider must return success with alternative success if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val alternativeSuccess = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(Exception(Uuid.random().toString()))
                .toSuccessIfNoneOfErrors(
                    {
                        alternativeSuccess
                    },
                    {
                        false
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(alternativeSuccess, result)
    }

    @Test
    fun `to either must return left either with errors if initial success or multiple errors initiated as error`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toEither()
                .toMaybeLeft()
                .orNull()!!

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to either must return right either with success value if initial success or multiple errors initiated as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toEither()
                .toMaybeRight()
                .orNull()!!

        assertEquals(initialValue, result)
    }
}