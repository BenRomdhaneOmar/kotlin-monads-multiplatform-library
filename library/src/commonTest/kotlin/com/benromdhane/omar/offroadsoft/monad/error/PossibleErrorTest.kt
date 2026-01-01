package com.benromdhane.omar.offroadsoft.monad.error

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class PossibleErrorTest {

    @Test
    fun `error must return false if possible error is created by try that was initiated as success`() {
        val result =
            PossibleError.of(Try.seed(Unit))
                .error()

        assertFalse { result }
    }

    @Test
    fun `error must return true if possible error is created by try that was initiated as failure`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            PossibleError.of(Try.seed(initialError))
                .error()

        assertTrue { result }
    }

    @Test
    fun `try as possible error must return success if try was initiated as success`() {
        val result =
            Try.trying { }
                .asPossibleError()
                .error()

        assertFalse { result }
    }

    @Test
    fun `try as possible error must return error if try was initiated as failure`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            Try.seed<Unit>(initialError)
                .asPossibleError()
                .error()

        assertTrue { result }
    }
}