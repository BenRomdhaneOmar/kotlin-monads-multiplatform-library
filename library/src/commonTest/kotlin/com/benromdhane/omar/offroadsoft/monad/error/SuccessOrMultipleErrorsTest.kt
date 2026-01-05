package com.benromdhane.omar.offroadsoft.monad.error

import io.kotest.assertions.assertSoftly
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
}