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

    @Test
    fun `map first must return a quadruple with mapped initial first values`() {
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
                .mapFirst { it.length }

        assertSoftly {
            assertEquals(firstValue.length, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
        }
    }

    @Test
    fun `map second must return a quadruple with mapped initial second values`() {
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
                .mapSecond { it.length }

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue.length, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
        }
    }

    @Test
    fun `map third must return a quadruple with mapped initial third values`() {
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
                .mapThird { it.length }

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue.length, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
        }
    }

    @Test
    fun `map fourth must return a quadruple with mapped initial fourth values`() {
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
                .mapFourth { it.length }

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue.length, result.fourthElement)
        }
    }

    @Test
    fun `switch first and second elements must return a quadruple with switched initial values`() {
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
                .switchFirstAndSecondElements()

        assertSoftly {
            assertEquals(secondValue, result.firstElement)
            assertEquals(firstValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
        }
    }

    @Test
    fun `switch first and third elements must return a quadruple with switched initial values`() {
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
                .switchFirstAndThirdElements()

        assertSoftly {
            assertEquals(thirdValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(firstValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
        }
    }

    @Test
    fun `switch first and fourth elements must return a quadruple with switched initial values`() {
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
                .switchFirstAndFourthElements()

        assertSoftly {
            assertEquals(fourthValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(firstValue, result.fourthElement)
        }
    }

    @Test
    fun `switch second and third elements must return a quadruple with switched initial values`() {
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
                .switchSecondAndThirdElements()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(thirdValue, result.secondElement)
            assertEquals(secondValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
        }
    }

    @Test
    fun `switch second and fourth elements must return a quadruple with switched initial values`() {
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
                .switchSecondAndFourthElements()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(fourthValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(secondValue, result.fourthElement)
        }
    }

    @Test
    fun `switch third and fourth elements must return a quadruple with switched initial values`() {
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
                .switchThirdAndFourthElements()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(fourthValue, result.thirdElement)
            assertEquals(thirdValue, result.fourthElement)
        }
    }

    @Test
    fun `to triple must return triple with first elements`() {
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
                .toTriple()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
        }
    }

    @Test
    fun `to pair must return pair with first elements`() {
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
                .toPair()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
        }
    }

    @Test
    fun `transform must return transformed result`() {
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
                .transform { first, second, third, fourth ->
                    first.length +
                            second.length +
                            third.length +
                            fourth.length
                }

        assertEquals(
            firstValue.length +
                    secondValue.length +
                    thirdValue.length +
                    fourthValue.length,
            result
        )
    }

    @Test
    fun `transform without fourth element must return transformed result`() {
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
                .transform { first, second, third ->
                    first.length +
                            second.length +
                            third.length
                }

        assertEquals(
            firstValue.length +
                    secondValue.length +
                    thirdValue.length,
            result
        )
    }
}