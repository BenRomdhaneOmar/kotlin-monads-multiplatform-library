package com.benromdhane.omar.offroadsoft.monad.tuple

import io.kotest.assertions.assertSoftly
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class PairTest {

    @Test
    fun `pair builder of must return a pair with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val result =
            Pair
                .of(
                    firstValue,
                    secondValue
                )

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
        }
    }

    @Test
    fun `pair builder of with elements providers must return a pair with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val result =
            Pair
                .of(
                    { firstValue },
                    { secondValue }
                )

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
        }
    }

    @Test
    fun `pair factory build must return a pair with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val result =
            Pair
                .Factory
                .instance<String, String>()
                .first(firstValue)
                .second(secondValue)

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
        }
    }

    @Test
    fun `map first must return a pair with mapped initial first values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val result =
            Pair
                .of(
                    firstValue,
                    secondValue
                )
                .mapFirst { it.length }

        assertSoftly {
            assertEquals(firstValue.length, result.firstElement)
            assertEquals(secondValue, result.secondElement)
        }
    }

    @Test
    fun `map second must return a pair with mapped initial second values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val result =
            Pair
                .of(
                    firstValue,
                    secondValue
                )
                .mapSecond { it.length }

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue.length, result.secondElement)
        }
    }

    @Test
    fun `switch first and second elements must return a pair with initial first element as second element and initial second element as first element`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val result =
            Pair
                .of(
                    firstValue,
                    secondValue
                )
                .switchFirstAndSecondElements()

        assertSoftly {
            assertEquals(secondValue, result.firstElement)
            assertEquals(firstValue, result.secondElement)
        }
    }
}