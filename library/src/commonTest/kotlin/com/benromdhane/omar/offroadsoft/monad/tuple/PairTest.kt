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
}