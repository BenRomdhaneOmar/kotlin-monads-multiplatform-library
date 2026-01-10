package com.benromdhane.omar.offroadsoft.monad.tuple

import io.kotest.assertions.assertSoftly
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TripleTest {

    @Test
    fun `triple builder of must return a triple with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val result =
            Triple
                .of(
                    firstValue,
                    secondValue,
                    thirdValue
                )

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
        }
    }

    @Test
    fun `triple builder of with elements providers must return a triple with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val result =
            Triple
                .of(
                    { firstValue },
                    { secondValue },
                    { thirdValue }
                )

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
        }
    }

    @Test
    fun `triple factory build must return a triple with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val result =
            Triple
                .Factory
                .instance<String, String, String>()
                .first(firstValue)
                .second(secondValue)
                .third(thirdValue)

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
        }
    }

    @Test
    fun `map first must return a pair with mapped initial first values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val result =
            Triple
                .of(
                    firstValue,
                    secondValue,
                    thirdValue
                )
                .mapFirst { it.length }

        assertSoftly {
            assertEquals(firstValue.length, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
        }
    }
}