package com.benromdhane.omar.offroadsoft.monad.evaluation

import io.kotest.assertions.assertSoftly
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class EvaluateTwoElementsTest {

    @Test
    fun `evaluate must return success success or multiple errors if one valid evaluation for first element and one valid evaluation for second was provided`() {
        val firstElementToEvaluate = Uuid.random().toString()
        val secondElementToEvaluate = Random.nextInt()
        val result =
            EvaluateTwoElements
                .first<_, Throwable>(firstElementToEvaluate)
                .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
                .second(secondElementToEvaluate)
                .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
                .evaluate()
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertEquals(firstElementToEvaluate, result.firstElement)
            assertEquals(secondElementToEvaluate, result.secondElement)
        }
    }

    @Test
    fun `evaluate must return success success or multiple errors if multiple valid evaluation for first element and multiple valid evaluation for second was provided`() {
        val firstElementToEvaluate = Uuid.random().toString()
        val secondElementToEvaluate = Random.nextInt()
        val result =
            EvaluateTwoElements
                .first<_, Throwable>(firstElementToEvaluate)
                .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
                .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
                .second(secondElementToEvaluate)
                .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
                .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
                .evaluate()
                .toMaybeSuccess()
                .orNull()!!

        assertSoftly {
            assertEquals(firstElementToEvaluate, result.firstElement)
            assertEquals(secondElementToEvaluate, result.secondElement)
        }
    }

    @Test
    fun `evaluate must return failure success or multiple errors if one invalid evaluation for first element and one invalid evaluation for second was provided`() {
        val firstElementEvaluateError = Exception(Uuid.random().toString())
        val secondElementEvaluateError = Exception(Uuid.random().toString())
        val result =
            EvaluateTwoElements
                .first<_, Throwable>(Uuid.random().toString())
                .addEvaluation({ firstElementEvaluateError }) { false }
                .second(Random.nextInt())
                .addEvaluation({ secondElementEvaluateError }) { false }
                .evaluate()
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, firstElementEvaluateError)
            assertContains(result, secondElementEvaluateError)
        }
    }

    @Test
    fun `evaluate must return failure success or multiple errors if multiple invalid evaluation for first element and multiple invalid evaluation for second was provided`() {
        val firstElementEvaluateError1 = Exception(Uuid.random().toString())
        val firstElementEvaluateError2 = Exception(Uuid.random().toString())
        val secondElementEvaluateError1 = Exception(Uuid.random().toString())
        val secondElementEvaluateError2 = Exception(Uuid.random().toString())
        val result =
            EvaluateTwoElements
                .first<_, Throwable>(Uuid.random().toString())
                .addEvaluation({ firstElementEvaluateError1 }) { false }
                .addEvaluation({ firstElementEvaluateError2 }) { false }
                .second(Random.nextInt())
                .addEvaluation({ secondElementEvaluateError1 }) { false }
                .addEvaluation({ secondElementEvaluateError2 }) { false }
                .evaluate()
                .toErrors()

        assertSoftly {
            assertEquals(4, result.size)
            assertContains(result, firstElementEvaluateError1)
            assertContains(result, firstElementEvaluateError2)
            assertContains(result, secondElementEvaluateError1)
            assertContains(result, secondElementEvaluateError2)
        }
    }

    @Test
    fun `evaluate must return failure success or multiple errors if multiple valid evaluation for first element and multiple invalid evaluation for second was provided`() {
        val firstElementEvaluateError1 = Exception(Uuid.random().toString())
        val firstElementEvaluateError2 = Exception(Uuid.random().toString())
        val secondElementEvaluateError1 = Exception(Uuid.random().toString())
        val secondElementEvaluateError2 = Exception(Uuid.random().toString())
        val result =
            EvaluateTwoElements
                .first<_, Throwable>(Uuid.random().toString())
                .addEvaluation({ firstElementEvaluateError1 }) { true }
                .addEvaluation({ firstElementEvaluateError2 }) { true }
                .second(Random.nextInt())
                .addEvaluation({ secondElementEvaluateError1 }) { false }
                .addEvaluation({ secondElementEvaluateError2 }) { false }
                .evaluate()
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, secondElementEvaluateError1)
            assertContains(result, secondElementEvaluateError2)
        }
    }

    @Test
    fun `evaluate must return failure success or multiple errors if multiple invalid evaluation for first element and multiple valid evaluation for second was provided`() {
        val firstElementEvaluateError1 = Exception(Uuid.random().toString())
        val firstElementEvaluateError2 = Exception(Uuid.random().toString())
        val secondElementEvaluateError1 = Exception(Uuid.random().toString())
        val secondElementEvaluateError2 = Exception(Uuid.random().toString())
        val result =
            EvaluateTwoElements
                .first<_, Throwable>(Uuid.random().toString())
                .addEvaluation({ firstElementEvaluateError1 }) { false }
                .addEvaluation({ firstElementEvaluateError2 }) { false }
                .second(Random.nextInt())
                .addEvaluation({ secondElementEvaluateError1 }) { true }
                .addEvaluation({ secondElementEvaluateError2 }) { true }
                .evaluate()
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, firstElementEvaluateError1)
            assertContains(result, firstElementEvaluateError2)
        }
    }

    @Test
    fun `evaluate must return failure success or multiple errors if one of multiple invalid evaluation for first element and one of multiple valid evaluation for second was provided`() {
        val firstElementEvaluateError1 = Exception(Uuid.random().toString())
        val firstElementEvaluateError2 = Exception(Uuid.random().toString())
        val secondElementEvaluateError1 = Exception(Uuid.random().toString())
        val secondElementEvaluateError2 = Exception(Uuid.random().toString())
        val result =
            EvaluateTwoElements
                .first<_, Throwable>(Uuid.random().toString())
                .addEvaluation({ firstElementEvaluateError1 }) { false }
                .addEvaluation({ firstElementEvaluateError2 }) { true }
                .second(Random.nextInt())
                .addEvaluation({ secondElementEvaluateError1 }) { false }
                .addEvaluation({ secondElementEvaluateError2 }) { true }
                .evaluate()
                .toErrors()

        assertSoftly {
            assertEquals(2, result.size)
            assertContains(result, firstElementEvaluateError1)
            assertContains(result, secondElementEvaluateError1)
        }
    }
}