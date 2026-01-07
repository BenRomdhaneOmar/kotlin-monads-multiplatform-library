package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Maybe
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
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess {
                    evaluated = true
                    SuccessOrMultipleErrors
                        .Error
                        .of(Exception(Uuid.random().toString()))
                }
                .toErrors()

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `flat map success must return initial errors if success or multiple errors initiated as error and mapping result is success`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess {
                    evaluated = false
                    SuccessOrMultipleErrors
                        .Success
                        .of(Uuid.random().toString())
                }
                .toErrors()

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `flat map success must return mapping errors if success or multiple errors initiated as success and mapping result is error`() {
        val mappingError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .flatMapSuccess {
                    evaluated = true
                    SuccessOrMultipleErrors
                        .Error
                        .of<String, _>(mappingError)
                }
                .toErrors()

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(1, result.size)
            assertContains(result, mappingError)
        }
    }

    @Test
    fun `flat map success must return mapped success if success or multiple errors initiated as success and mapping result is success`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .flatMapSuccess {
                    evaluated = true
                    SuccessOrMultipleErrors
                        .Success
                        .of(it.length)
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(initialValue.length, result)
        }
    }

    @Test
    fun `filter success must return initial errors if success or multiple errors initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccess(
                    Exception(Uuid.random().toString())
                ) {
                    evaluated = true
                    false
                }
                .toErrors()

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success must return initial errors if success or multiple errors initiated as error and filter is valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccess(
                    Exception(Uuid.random().toString())
                ) {
                    evaluated = true
                    true
                }
                .toErrors()

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success must return success if success or multiple errors initiated as success and filter is valid`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .filterSuccess(
                    Exception(Uuid.random().toString())
                )
                {
                    evaluated = true
                    true
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `filter success must return alternative error if success or multiple errors initiated as success and filter is not valid`() {
        val alternativeError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .filterSuccess(
                    alternativeError
                ) {
                    evaluated = true
                    false
                }
                .toErrors()

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(1, result.size)
            assertContains(result, alternativeError)
        }
    }

    @Test
    fun `filter success with alternative error provider must return initial errors if success or multiple errors initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccess(
                    {
                        evaluatedAlternative = true
                        Exception(Uuid.random().toString())
                    },
                    {
                        evaluatedCondition = true
                        false
                    }
                )
                .toErrors()

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertFalse { evaluatedCondition }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success with alternative error provider must return initial errors if success or multiple errors initiated as error and filter is valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccess(
                    {
                        evaluatedAlternative = true
                        Exception(Uuid.random().toString())
                    },
                    {
                        evaluatedCondition = true
                        true
                    }
                )
                .toErrors()

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertFalse { evaluatedCondition }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success with alternative error provider must return success if success or multiple errors initiated as success and filter is valid`() {
        val initialValue = Uuid.random().toString()
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .filterSuccess(
                    {
                        evaluatedAlternative = true
                        Exception(Uuid.random().toString())
                    },
                    {
                        evaluatedCondition = true
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertTrue { evaluatedCondition }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `filter success with alternative error provider must return alternative error if success or multiple errors initiated as success and filter is not valid`() {
        val alternativeError = Exception(Uuid.random().toString())
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .filterSuccess(
                    {
                        evaluatedAlternative = true
                        alternativeError
                    },
                    {
                        evaluatedCondition = true
                        false
                    }
                )
                .toErrors()

        assertSoftly {
            assertTrue { evaluatedAlternative }
            assertTrue { evaluatedCondition }
            assertEquals(1, result.size)
            assertContains(result, alternativeError)
        }
    }

    @Test
    fun `filter success not must return initial errors if success or multiple errors initiated as error and filter is valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccessNot(
                    Exception(Uuid.random().toString())
                ) {
                    evaluated = true
                    true
                }
                .toErrors()

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success not must return initial errors if success or multiple errors initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccessNot(
                    Exception(Uuid.random().toString())
                ) {
                    evaluated = true
                    false
                }
                .toErrors()

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success not must return success if success or multiple errors initiated as success and filter is not valid`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .filterSuccessNot(
                    Exception(Uuid.random().toString())
                )
                {
                    evaluated = true
                    false
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `filter success not must return alternative error if success or multiple errors initiated as success and filter is valid`() {
        val alternativeError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .filterSuccessNot(
                    alternativeError
                ) {
                    evaluated = true
                    true
                }
                .toErrors()

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(1, result.size)
            assertContains(result, alternativeError)
        }
    }

    @Test
    fun `filter success not with alternative error provider must return initial errors if success or multiple errors initiated as error and filter is valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccessNot(
                    {
                        evaluatedAlternative = true
                        Exception(Uuid.random().toString())
                    },
                    {
                        evaluatedCondition = true
                        true
                    }
                )
                .toErrors()

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertFalse { evaluatedCondition }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success not with alternative error provider must return initial errors if success or multiple errors initiated as error and filter is not valid`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .filterSuccessNot(
                    {
                        evaluatedAlternative = true
                        Exception(Uuid.random().toString())
                    },
                    {
                        evaluatedCondition = true
                        false
                    }
                )
                .toErrors()

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertFalse { evaluatedCondition }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `filter success not with alternative error provider must return success if success or multiple errors initiated as success and filter is not valid`() {
        val initialValue = Uuid.random().toString()
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .filterSuccessNot(
                    {
                        evaluatedAlternative = true
                        Exception(Uuid.random().toString())
                    },
                    {
                        evaluatedCondition = true
                        false
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertTrue { evaluatedCondition }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `filter success not with alternative error provider must return alternative error if success or multiple errors initiated as success and filter is valid`() {
        val alternativeError = Exception(Uuid.random().toString())
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .filterSuccessNot(
                    {
                        evaluatedAlternative = true
                        alternativeError
                    },
                    {
                        evaluatedCondition = true
                        true
                    }
                )
                .toErrors()

        assertSoftly {
            assertTrue { evaluatedAlternative }
            assertTrue { evaluatedCondition }
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
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccess {
                    evaluated = true
                    Uuid.random().toString()
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success with provider must return alternative success if success or multiple errors initiated as error`() {
        val alternativeValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .toSuccess {
                    evaluated = true
                    alternativeValue
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeValue, result)
        }
    }

    @Test
    fun `to success if all errors must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAllErrors(
                    Uuid.random().toString()
                ) {
                    evaluated = true
                    false
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if all errors must return success with initial success if initial success or multiple errors initiated as success and condition is valid for all errors`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAllErrors(
                    Uuid.random().toString()
                ) {
                    evaluated = true
                    true
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if all errors must return error if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfAllErrors(
                    Uuid.random().toString()
                ) {
                    evaluated = true
                    it is IllegalArgumentException
                }
                .toErrors()

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if all errors must return success with alternative success if initial success or multiple errors initiated as error and condition is valid for all errors`() {
        val alternativeSuccess = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(Exception(Uuid.random().toString()))
                .toSuccessIfAllErrors(
                    alternativeSuccess
                ) {
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
    fun `to success if all errors with alternative success provider must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAllErrors(
                    {
                        evaluatedAlternative = true
                        Uuid.random().toString()
                    },
                    {
                        evaluatedCondition = true
                        false
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertFalse { evaluatedCondition }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if all errors with alternative success provider must return success with initial success if initial success or multiple errors initiated as success and condition is valid for all errors`() {
        val initialValue = Uuid.random().toString()
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAllErrors(
                    {
                        evaluatedAlternative = true
                        Uuid.random().toString()
                    },
                    {
                        evaluatedCondition = true
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertFalse { evaluatedCondition }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if all errors with alternative success provider must return error if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfAllErrors(
                    {
                        evaluatedAlternative = true
                        Uuid.random().toString()
                    },
                    {
                        evaluatedCondition = true
                        it is IllegalArgumentException
                    }
                )
                .toErrors()

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertTrue { evaluatedCondition }
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if all error with alternative success provider must return success with alternative success if initial success or multiple errors initiated as error and condition is valid for all errors`() {
        val alternativeSuccess = Uuid.random().toString()
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(Exception(Uuid.random().toString()))
                .toSuccessIfAllErrors(
                    {
                        evaluatedAlternative = true
                        alternativeSuccess
                    },
                    {
                        evaluatedCondition = true
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluatedAlternative }
            assertTrue { evaluatedCondition }
            assertEquals(alternativeSuccess, result)
        }
    }

    @Test
    fun `to success if any error must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAnyError(
                    Uuid.random().toString()
                ) {
                    evaluated = true
                    false
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if any error must return success with initial success if initial success or multiple errors initiated as success and condition is valid for at least one of errors`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAnyError(
                    Uuid.random().toString()
                ) {
                    evaluated = true
                    true
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if any error must return error if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfAnyError(
                    Uuid.random().toString()
                ) {
                    evaluated = true
                    false
                }
                .toErrors()

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if any error must return success with alternative success if initial success or multiple errors initiated as error and condition is valid for at least one of errors`() {
        val alternativeSuccess = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(IllegalArgumentException(Uuid.random().toString()))
                .toSuccessIfAnyError(
                    alternativeSuccess
                ) {
                    evaluated = true
                    it is IllegalArgumentException
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeSuccess, result)
        }
    }

    @Test
    fun `to success if any error with success alternative provider must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAnyError(
                    {
                        evaluatedAlternative = true
                        Uuid.random().toString()
                    },
                    {
                        evaluatedCondition = true
                        false
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertFalse { evaluatedCondition }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if any error with success alternative provider must return success with initial success if initial success or multiple errors initiated as success and condition is valid for at least one of errors`() {
        val initialValue = Uuid.random().toString()
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfAnyError(
                    {
                        evaluatedAlternative = true
                        Uuid.random().toString()
                    },
                    {
                        evaluatedCondition = true
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertFalse { evaluatedCondition }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if any error with success alternative provider must return error if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfAnyError(
                    {
                        evaluatedAlternative = true
                        Uuid.random().toString()
                    },
                    {
                        evaluatedCondition = true
                        false
                    }
                )
                .toErrors()

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertTrue { evaluatedCondition }
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if any error with success alternative provider must return success with alternative success if initial success or multiple errors initiated as error and condition is valid for at least one of errors`() {
        val alternativeSuccess = Uuid.random().toString()
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(IllegalArgumentException(Uuid.random().toString()))
                .toSuccessIfAnyError(
                    {
                        evaluatedAlternative = true
                        alternativeSuccess
                    },
                    {
                        evaluatedCondition = true
                        it is IllegalArgumentException
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluatedAlternative }
            assertTrue { evaluatedCondition }
            assertEquals(alternativeSuccess, result)
        }
    }

    @Test
    fun `to success if none of errors must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfNoneOfErrors(
                    Uuid.random().toString()
                ) {
                    evaluated = true
                    false
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if none of errors must return success with initial success if initial success or multiple errors initiated as success and condition is valid for at least one of errors`() {
        val initialValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfNoneOfErrors(
                    Uuid.random().toString()
                ) {
                    evaluated = true
                    true
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if none of errors must return error if initial success or multiple errors initiated as error and condition is not valid for at least one of errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfNoneOfErrors(
                    Uuid.random().toString()
                ) {
                    evaluated = true
                    it is IllegalArgumentException
                }
                .toErrors()

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if none of errors must return success with alternative success if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val alternativeSuccess = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(Exception(Uuid.random().toString()))
                .toSuccessIfNoneOfErrors(
                    alternativeSuccess
                ) {
                    evaluated = true
                    false
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(alternativeSuccess, result)
        }
    }

    @Test
    fun `to success if none of errors with success alternative provider must return success with initial success if initial success or multiple errors initiated as success and condition is not valid for all errors`() {
        val initialValue = Uuid.random().toString()
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfNoneOfErrors(
                    {
                        evaluatedAlternative = true
                        Uuid.random().toString()
                    },
                    {
                        evaluatedCondition = true
                        false
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertFalse { evaluatedCondition }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if none of errors with success alternative provider must return success with initial success if initial success or multiple errors initiated as success and condition is valid for at least one of errors`() {
        val initialValue = Uuid.random().toString()
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .toSuccessIfNoneOfErrors(
                    {
                        evaluatedAlternative = true
                        Uuid.random().toString()
                    },
                    {
                        evaluatedCondition = true
                        true
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertFalse { evaluatedCondition }
            assertEquals(initialValue, result)
        }
    }

    @Test
    fun `to success if none of errors with success alternative provider must return error if initial success or multiple errors initiated as error and condition is not valid for at least one of errors`() {
        val initialError = Exception(Uuid.random().toString())
        val secondError = IllegalArgumentException(Uuid.random().toString())
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .addError(secondError)
                .toSuccessIfNoneOfErrors(
                    {
                        evaluatedAlternative = true
                        Uuid.random().toString()
                    },
                    {
                        evaluatedCondition = true
                        it is IllegalArgumentException
                    }
                )
                .toErrors()

        assertSoftly {
            assertFalse { evaluatedAlternative }
            assertTrue { evaluatedCondition }
            assertEquals(2, result.size)
            assertContains(result, initialError)
            assertContains(result, secondError)
        }
    }

    @Test
    fun `to success if none of errors with success alternative provider must return success with alternative success if initial success or multiple errors initiated as error and condition is not valid for all errors`() {
        val alternativeSuccess = Uuid.random().toString()
        var evaluatedAlternative = false
        var evaluatedCondition = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .addError(Exception(Uuid.random().toString()))
                .toSuccessIfNoneOfErrors(
                    {
                        evaluatedAlternative = true
                        alternativeSuccess
                    },
                    {
                        evaluatedCondition = true
                        false
                    }
                )
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluatedAlternative }
            assertTrue { evaluatedCondition }
            assertEquals(alternativeSuccess, result)
        }
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

    @Test
    fun `fold must return errors mapper result if initial success or multiple errors initiated as error`() {
        var evaluatedSuccessMapper = false
        var evaluatedErrorMapper = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(Exception(Uuid.random().toString()))
                .fold(
                    {
                        evaluatedSuccessMapper = true
                        0
                    },
                    {
                        evaluatedErrorMapper = true
                        it.size
                    }
                )

        assertSoftly {
            assertTrue { evaluatedErrorMapper }
            assertFalse { evaluatedSuccessMapper }
            assertEquals(1, result)
        }
    }

    @Test
    fun `fold must return success mapper result if initial success or multiple errors initiated as success`() {
        val initialValue = Uuid.random().toString()
        var evaluatedSuccessMapper = false
        var evaluatedErrorMapper = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .fold(
                    {
                        evaluatedSuccessMapper = true
                        it.length
                    },
                    {
                        evaluatedErrorMapper = true
                        it.size
                    }
                )

        assertSoftly {
            assertFalse { evaluatedErrorMapper }
            assertTrue { evaluatedSuccessMapper }
            assertEquals(initialValue.length, result)
        }
    }

    @Test
    fun `as success or single error must return error success or single error with errors if initial success or multiple errors initiated as error`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .asSuccessOrSingleError()
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `as success or single error must return success success or single error with success value if initial success or multiple errors initiated as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .asSuccessOrSingleError()
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(initialValue, result)
    }

    @Test
    fun `success or single error flat map success extension function with success or multiple errors mapping result must return error success or multiple errors with initial error if initial success or single error is error and success or multiple errors mapping result is error`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess<_, _, _> {
                    evaluated = true
                    SuccessOrMultipleErrors
                        .Error
                        .of<Int, _>(Exception(Uuid.random().toString()))
                }
                .toErrors()

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `success or single error flat map success extension function with success or multiple errors mapping result must return error success or multiple errors with initial error if initial success or single error is error and success or multiple errors mapping result is success`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess<_, _, _> {
                    evaluated = true
                    SuccessOrMultipleErrors
                        .Success
                        .of(Uuid.random().toString())
                }
                .toErrors()

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `success or single error flat map success extension function with success or multiple errors mapping result must return error success or multiple errors with mapping result error if initial success or single error is success and success or multiple errors mapping result is error`() {
        val mappingError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .flatMapSuccess<_, _, _> {
                    evaluated = true
                    SuccessOrMultipleErrors
                        .Error
                        .of<Int, _>(mappingError)
                }
                .toErrors()

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(1, result.size)
            assertContains(result, mappingError)
        }
    }

    @Test
    fun `success or single error flat map success extension function with success or multiple errors mapping result must return success success or multiple errors with mapping result success if initial success or single error is success and success or multiple errors mapping result is success`() {
        val mappingValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrSingleError
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .flatMapSuccess<_, _, _> {
                    evaluated = true
                    SuccessOrMultipleErrors
                        .Success
                        .of(mappingValue)
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(mappingValue, result)
        }
    }

    @Test
    fun `success or multiple errors flat map success extension function with success or single error mapping result must return error success or single error with initial error if initial success or multiple errors is error and success or single error mapping result is error`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess<_, _, _> {
                    evaluated = true
                    SuccessOrSingleError
                        .Error
                        .of<String, _>(
                            listOf(
                                Exception(Uuid.random().toString())
                            )
                        )
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `success or multiple errors flat map success extension function with success or single error mapping result must return error success or single error with initial error if initial success or multiple errors is error and success or single error mapping result is success`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess<_, _, _> {
                    evaluated = true
                    SuccessOrSingleError
                        .Success
                        .of(Uuid.random().toString())
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `success or multiple errors flat map success extension function with success or single error mapping result must return error success or single error with mapping result error if initial success or multiple errors is success and success or single error mapping result is error`() {
        val mappingError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .flatMapSuccess<_, _, _> {
                    evaluated = true
                    SuccessOrSingleError
                        .Error
                        .of<String, _>(
                            listOf(
                                mappingError
                            )
                        )
                }
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(1, result.size)
            assertContains(result, mappingError)
        }
    }

    @Test
    fun `success or multiple errors flat map success extension function with success or single error mapping result must return success success or single error with mapping result success if initial success or multiple errors is success and success or single error mapping result is success`() {
        val mappingValue = Uuid.random().toString()
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .flatMapSuccess<_, _, _> {
                    evaluated = true
                    SuccessOrSingleError
                        .Success
                        .of(mappingValue)
                }
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(mappingValue, result)
        }
    }

    @Test
    fun `success or multiple errors flat map success extension function with maybe errors mapping result must return not empty maybe with initial errors if initial success or multiple errors is error and maybe mapping is not empty`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess<_, _> {
                    evaluated = true
                    Maybe
                        .NotEmpty
                        .of(
                            listOf(
                                Exception(Uuid.random().toString())
                            )
                        )
                }
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `success or multiple errors flat map success extension function with maybe errors mapping result must return not empty maybe with initial errors if initial success or multiple errors is error and maybe mapping is empty`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .flatMapSuccess<_, _> {
                    evaluated = true
                    Maybe.Empty.of()
                }
                .orNull()!!

        assertSoftly {
            assertFalse { evaluated }
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `success or multiple errors flat map success extension function with maybe errors mapping result must return not empty maybe with mapping errors if initial success or multiple errors is success and maybe mapping is not empty`() {
        val mappingError = Exception(Uuid.random().toString())
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .flatMapSuccess<_, _> {
                    evaluated = true
                    Maybe
                        .NotEmpty
                        .of(
                            listOf(
                                mappingError
                            )
                        )
                }
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(1, result.size)
            assertContains(result, mappingError)
        }
    }

    @Test
    fun `success or multiple errors flat map success extension function with maybe errors mapping result must return empty maybe if initial success or multiple errors is success and maybe mapping is empty`() {
        var evaluated = false
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(Uuid.random().toString())
                .flatMapSuccess<_, _> {
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
    fun `as possible error must return error possible error with initial errors if initial success or multiple errors initiated as error`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            SuccessOrMultipleErrors
                .Error
                .of<String, _>(initialError)
                .asPossibleError()
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, initialError)
        }
    }

    @Test
    fun `as possible error must return non error possible error if initial success or multiple errors initiated as success`() {
        val initialValue = Uuid.random().toString()
        val result =
            SuccessOrMultipleErrors
                .Success
                .of<_, Throwable>(initialValue)
                .asPossibleError()
                .error()

        assertFalse { result }
    }
}