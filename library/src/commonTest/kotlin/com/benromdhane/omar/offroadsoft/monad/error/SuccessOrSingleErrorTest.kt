package com.benromdhane.omar.offroadsoft.monad.error

import kotlin.test.Test
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
}