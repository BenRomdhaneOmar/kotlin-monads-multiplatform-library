package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Either
import com.benromdhane.omar.offroadsoft.monad.Maybe
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

    @Test
    fun `error must return false if possible error is created by either error on left or unit on right that was initiated as right`() {
        val result =
            PossibleError.of(Either.Right.of<Throwable, _>(Unit))
                .error()

        assertFalse { result }
    }

    @Test
    fun `error must return true if possible error is created by either error on left or unit on right that was initiated as left`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            PossibleError.of(Either.Left.of(initialError))
                .error()

        assertTrue { result }
    }

    @Test
    fun `error must return false if possible error is created by either error on right or unit on left that was initiated as left`() {
        val result =
            PossibleError.of(Either.Left.of<_, Throwable>(Unit))
                .error()

        assertFalse { result }
    }

    @Test
    fun `error must return true if possible error is created by either error on right or unit on left that was initiated as right`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            PossibleError.of(Either.Right.of(initialError))
                .error()

        assertTrue { result }
    }

    @Test
    fun `either as possible error must return success if try was initiated as right either error on left or unit on right`() {
        val result =
            Either.Right.of<Throwable, _>(Unit)
                .asPossibleError()
                .error()

        assertFalse { result }
    }

    @Test
    fun `either as possible error must return error if try was initiated as left either error on left or unit on right`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            Either.Left.of<_, Unit>(initialError)
                .asPossibleError()
                .error()

        assertTrue { result }
    }

    @Test
    fun `either as possible error must return success if try was initiated as left either error on right or unit on left`() {
        val result =
            Either.Left.of<_, Throwable>(Unit)
                .asPossibleError()
                .error()

        assertFalse { result }
    }

    @Test
    fun `either as possible error must return error if try was initiated as right either error on right or unit on left`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            Either.Right.of<Unit, _>(initialError)
                .asPossibleError()
                .error()

        assertTrue { result }
    }

    @Test
    fun `error must return false if possible error is created by maybe that was initiated as empty`() {
        val result =
            PossibleError.of(Maybe.Empty.of<Throwable>())
                .error()

        assertFalse { result }
    }

    @Test
    fun `error must return true if possible error is created by maybe that was initiated as non empty`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            PossibleError.of(Maybe.NotEmpty.of(initialError))
                .error()

        assertTrue { result }
    }

    @Test
    fun `maybe as possible error must return success if try was initiated as empty`() {
        val result =
            Maybe.Empty.of<Throwable>()
                .asPossibleError()
                .error()

        assertFalse { result }
    }

    @Test
    fun `maybe as possible error must return error if try was initiated as non empty`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            Maybe.NotEmpty.of(initialError)
                .asPossibleError()
                .error()

        assertTrue { result }
    }
}