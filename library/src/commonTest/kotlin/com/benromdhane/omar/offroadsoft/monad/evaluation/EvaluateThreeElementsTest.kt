package com.benromdhane.omar.offroadsoft.monad.evaluation

import io.kotest.assertions.assertSoftly
import kotlin.random.Random
import kotlin.test.Test
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
}