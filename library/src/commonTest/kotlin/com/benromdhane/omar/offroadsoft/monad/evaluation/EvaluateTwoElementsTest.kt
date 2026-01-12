package com.benromdhane.omar.offroadsoft.monad.evaluation

import io.kotest.assertions.assertSoftly
import kotlin.random.Random
import kotlin.test.Test
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
            EvaluateElements
                .Two
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

//    multiple valid for first and one valid for second
//    one valid for first and multiple valid for second
//    multiple valid for first and multiple valid for second
//    one invalid first and one invalid second
//    multiple invalid first and one invalid second
//    one invalid first and multiple invalid second
//    multiple invalid first and multiple invalid second
//    one valid first and one invalid second
//    one invalid first and one valid second
//    multiple valid first and one invalid second
//    multiple valid first and multiple invalid second
//    one valid first and multiple invalid second
//    multiple invalid first and one valid second
//    multiple invalid first and multiple valid second
//    one invalid first and multiple valid second
//    multiple first including one invalid and one second valid
//    one first valid and multiple second including one invalid
//    multiple first including one invalid and multiple second valid
//    multiple first valid and multiple second including one invalid
//    multiple first including one invalid and multiple second including one invalid
//    multiple first including one invalid  and multiple second including one invalid
}