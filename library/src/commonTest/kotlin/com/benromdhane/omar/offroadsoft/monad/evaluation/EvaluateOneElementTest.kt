package com.benromdhane.omar.offroadsoft.monad.evaluation

import io.kotest.assertions.assertSoftly
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class EvaluateOneElementTest {

    @Test
    fun `evaluate must return success success or multiple errors if one valid evaluation was provided`() {
        val elementToEvaluate = Uuid.random().toString()
        val result =
            EvaluateElements
                .One
                .element<_, Throwable>(elementToEvaluate)
                .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
                .evaluate()
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(elementToEvaluate, result)
    }

    @Test
    fun `evaluate must return success success or multiple errors if multiple valid evaluations was provided`() {
        val elementToEvaluate = Uuid.random().toString()
        val result =
            EvaluateElements
                .One
                .element<_, Throwable>(elementToEvaluate)
                .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
                .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
                .evaluate()
                .toMaybeSuccess()
                .orNull()!!

        assertEquals(elementToEvaluate, result)
    }

    @Test
    fun `evaluate must return error success or multiple errors if one invalid evaluation was provided`() {
        val elementToEvaluate = Uuid.random().toString()
        val error = Exception(Uuid.random().toString())
        val result =
            EvaluateElements
                .One
                .element<_, Throwable>(elementToEvaluate)
                .addEvaluation({ error }) { false }
                .evaluate()
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, error)
        }
    }

    @Test
    fun `evaluate must return error success or multiple errors if multiple invalid evaluations was provided`() {
        val elementToEvaluate = Uuid.random().toString()
        val error1 = Exception(Uuid.random().toString())
        val error2 = Exception(Uuid.random().toString())
        val result =
            EvaluateElements
                .One
                .element<_, Throwable>(elementToEvaluate)
                .addEvaluation({ error1 }) { false }
                .addEvaluation({ error2 }) { false }
                .evaluate()
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, error1)
            assertContains(result, error2)
        }
    }

    @Test
    fun `evaluate must return error success or multiple errors if multiple evaluations was provided including one invalid`() {
        val elementToEvaluate = Uuid.random().toString()
        val error = Exception(Uuid.random().toString())
        val result =
            EvaluateElements
                .One
                .element<_, Throwable>(elementToEvaluate)
                .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
                .addEvaluation({ error }) { false }
                .evaluate()
                .toErrors()

        assertSoftly {
            assertEquals(1, result.size)
            assertContains(result, error)
        }
    }
}