package com.benromdhane.omar.offroadsoft.monad.evaluation

import io.kotest.assertions.assertSoftly
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class EvaluateThreeElementsTest {

    @Test
    fun `evaluate must return success success or multiple errors if one valid evaluation for first element and one valid evaluation for second and one valid evaluation for third was provided`() {
        val firstElementToEvaluate = Uuid.random().toString()
        val secondElementToEvaluate = Random.nextInt()
        val thirdElementToEvaluate = Random.nextLong()
        val result = EvaluateElements
            .Three
            .first<_, Throwable>(firstElementToEvaluate)
            .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
            .second(secondElementToEvaluate)
            .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
            .third(thirdElementToEvaluate)
            .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
            .evaluate()
            .toMaybeSuccess()
            .orNull()!!

        assertSoftly {
            assertEquals(firstElementToEvaluate, result.firstElement)
            assertEquals(secondElementToEvaluate, result.secondElement)
            assertEquals(thirdElementToEvaluate, result.thirdElement)
        }
    }

    @Test
    fun `evaluate must return success success or multiple errors if multiple valid evaluations for first element and multiple valid evaluations for second and multiple valid evaluations for third was provided`() {
        val firstElementToEvaluate = Uuid.random().toString()
        val secondElementToEvaluate = Random.nextInt()
        val thirdElementToEvaluate = Random.nextLong()
        val result = EvaluateElements
            .Three
            .first<_, Throwable>(firstElementToEvaluate)
            .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
            .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
            .second(secondElementToEvaluate)
            .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
            .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
            .third(thirdElementToEvaluate)
            .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
            .addEvaluation({ Exception(Uuid.random().toString()) }) { true }
            .evaluate()
            .toMaybeSuccess()
            .orNull()!!

        assertSoftly {
            assertEquals(firstElementToEvaluate, result.firstElement)
            assertEquals(secondElementToEvaluate, result.secondElement)
            assertEquals(thirdElementToEvaluate, result.thirdElement)
        }
    }

    @Test
    fun `evaluate must return failure success or multiple errors if one of multiple evaluations for first element is invalid and one of multiple evaluations for second is invalid and one of multiple evaluations for third is invalid was provided`() {
        val firstElementError1 = Exception(Uuid.random().toString())
        val firstElementError2 = Exception(Uuid.random().toString())
        val secondElementError1 = Exception(Uuid.random().toString())
        val secondElementError2 = Exception(Uuid.random().toString())
        val thirdElementError1 = Exception(Uuid.random().toString())
        val thirdElementError2 = Exception(Uuid.random().toString())
        val result = EvaluateElements
            .Three
            .first<_, Throwable>(Uuid.random().toString())
            .addEvaluation({ firstElementError1 }) { true }
            .addEvaluation({ firstElementError2 }) { false }
            .second(Random.nextInt())
            .addEvaluation({ secondElementError1 }) { false }
            .addEvaluation({ secondElementError2 }) { true }
            .third(Random.nextLong())
            .addEvaluation({ thirdElementError1 }) { true }
            .addEvaluation({ thirdElementError2 }) { false }
            .evaluate()
            .toErrors()

        assertSoftly {
            assertEquals(3, result.size)
            assertContains(result, firstElementError2)
            assertContains(result, secondElementError1)
            assertContains(result, thirdElementError2)
        }
    }
}