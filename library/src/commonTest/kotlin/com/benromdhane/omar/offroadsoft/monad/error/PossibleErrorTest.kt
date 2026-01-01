package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Either
import com.benromdhane.omar.offroadsoft.monad.Maybe
import io.kotest.assertions.assertSoftly
import kotlin.test.Test
import kotlin.test.assertEquals
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

    @Test
    fun `to maybe error must return empty maybe if possible error is created as success`() {
        val result =
            PossibleError.of(Try.seed(Unit))
                .toMaybeError()
                .empty()

        assertTrue { result }
    }

    @Test
    fun `to maybe error must return not empty maybe with initial value if possible error is created as failure`() {
        val initialError = Exception(Uuid.random().toString())
        val result =
            PossibleError.of(Try.seed(initialError))
                .toMaybeError()
                .orNull()!!

        assertEquals(initialError, result)
    }

    @Test
    fun `map must be ignored if possible error is created as success`() {
        var evaluated = false
        val mapper: (Throwable) -> Throwable = {
            evaluated = true
            Exception(Uuid.random().toString())
        }
        val result =
            PossibleError.of(Try.seed(Unit))
                .map(mapper)
                .error()

        assertSoftly {
            assertFalse { evaluated }
            assertFalse { result }
        }
    }

    @Test
    fun `map must transform initial error if possible error is created as error`() {
        val initialError = Exception(Uuid.random().toString())
        var evaluated = false
        val mappedError = Exception(Uuid.random().toString())
        val mapper: (Throwable) -> Throwable = {
            evaluated = true
            mappedError
        }
        val result =
            PossibleError.of(Try.seed(initialError))
                .map(mapper)
                .toMaybeError()
                .orNull()!!

        assertSoftly {
            assertTrue { evaluated }
            assertEquals(mappedError, result)
        }
    }

    @Test
    fun `to string must return success if possible error is created as success`() {
        val result =
            PossibleError.of(Try.seed(Unit))
                .toString()

        assertEquals("Success", result)
    }

    @Test
    fun `equals must return true if two possible errors are success`() {
        val result =
            PossibleError.of(Try.seed(Unit))
                .equals(PossibleError.of(Try.seed(Unit)))

        assertTrue { result }
    }

    @Test
    fun `equals must return true if possible errors compared to itself`() {
        val possibleError = PossibleError.of(Try.seed(Unit))
        val result =
            possibleError
                .equals(possibleError)

        assertTrue { result }
    }

    @Test
    fun `equals must return false if two possible errors are not both success`() {
        val result =
            PossibleError.of(Try.seed(Unit))
                .equals(
                    PossibleError.of(
                        Try.seed(
                            Exception(Uuid.random().toString())
                        )
                    )
                )

        assertFalse { result }
    }

    @Test
    fun `equals must return false if possible errors is compared to another object`() {
        val result =
            PossibleError.of(Try.seed(Unit))
                .equals(Uuid.random().toString())

        assertFalse { result }
    }

    @Test
    fun `equals must return false if possible errors is compared to null`() {
        val result =
            PossibleError.of(Try.seed(Unit))
                .equals(null)

        assertFalse { result }
    }
}