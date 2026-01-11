package com.benromdhane.omar.offroadsoft.monad.tuple

import io.kotest.assertions.assertSoftly
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class QuintupleTest {

    @Test
    fun `quintuple builder of must return a quintuple with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val fifthValue = Uuid.random().toString()
        val result =
            Quintuple
                .of(
                    firstValue,
                    secondValue,
                    thirdValue,
                    fourthValue,
                    fifthValue
                )

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `quintuple builder of with elements providers must return a quintuple with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val fifthValue = Uuid.random().toString()
        val result =
            Quintuple
                .of(
                    { firstValue },
                    { secondValue },
                    { thirdValue },
                    { fourthValue },
                    { fifthValue }
                )

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `quintuple factory build must return a quintuple with initial values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val fifthValue = Uuid.random().toString()
        val result =
            Quintuple
                .Factory
                .instance<String, String, String, String, String>()
                .first(firstValue)
                .second(secondValue)
                .third(thirdValue)
                .fourth(fourthValue)
                .fifth(fifthValue)

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `map first must return a quintuple with mapped initial first values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val fifthValue = Uuid.random().toString()
        val result =
            Quintuple
                .of(
                    firstValue,
                    secondValue,
                    thirdValue,
                    fourthValue,
                    fifthValue
                )
                .mapFirst { it.length }

        assertSoftly {
            assertEquals(firstValue.length, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `map second must return a quintuple with mapped initial second values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val fifthValue = Uuid.random().toString()
        val result =
            Quintuple
                .of(
                    firstValue,
                    secondValue,
                    thirdValue,
                    fourthValue,
                    fifthValue
                )
                .mapSecond { it.length }

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue.length, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `map third must return a quintuple with mapped initial third values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val fifthValue = Uuid.random().toString()
        val result =
            Quintuple
                .of(
                    firstValue,
                    secondValue,
                    thirdValue,
                    fourthValue,
                    fifthValue
                )
                .mapThird { it.length }

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue.length, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `map fourth must return a quintuple with mapped initial fourth values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val fifthValue = Uuid.random().toString()
        val result =
            Quintuple
                .of(
                    firstValue,
                    secondValue,
                    thirdValue,
                    fourthValue,
                    fifthValue
                )
                .mapFourth { it.length }

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue.length, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `map fifth must return a quintuple with mapped initial fourth values`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val fifthValue = Uuid.random().toString()
        val result =
            Quintuple
                .of(
                    firstValue,
                    secondValue,
                    thirdValue,
                    fourthValue,
                    fifthValue
                )
                .mapFifth { it.length }

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(fifthValue.length, result.fifthElement)
        }
    }
}