package com.benromdhane.omar.offroadsoft.monad.error

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
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
}