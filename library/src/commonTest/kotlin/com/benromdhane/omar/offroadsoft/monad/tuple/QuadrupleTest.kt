package com.benromdhane.omar.offroadsoft.monad.tuple

import io.kotest.assertions.assertSoftly
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class QuadrupleTest {

    @Test
    fun `quadruple builder of must return a quadruple with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val result =
            Quadruple
                .of(
                    firstValue,
                    secondValue,
                    thirdValue,
                    fourthValue
                )

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
        }
    }

    @Test
    fun `quadruple builder of with elements providers must return a quadruple with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val result =
            Quadruple
                .of(
                    { firstValue },
                    { secondValue },
                    { thirdValue },
                    { fourthValue }
                )

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
        }
    }

    @Test
    fun `quadruple factory build must return a quadruple with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val result =
            Quadruple
                .Factory
                .instance<String, String, String, String>()
                .first(firstValue)
                .second(secondValue)
                .third(thirdValue)
                .fourth(fourthValue)

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
        }
    }
}