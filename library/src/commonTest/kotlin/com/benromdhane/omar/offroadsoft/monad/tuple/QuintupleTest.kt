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

    @Test
    fun `switch first and second elements must return a quintuple with switched initial values`() {
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
                .switchFirstAndSecondElements()

        assertSoftly {
            assertEquals(secondValue, result.firstElement)
            assertEquals(firstValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `switch first and third elements must return a quintuple with switched initial values`() {
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
                .switchFirstAndThirdElements()

        assertSoftly {
            assertEquals(thirdValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(firstValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `switch first and fourth elements must return a quintuple with switched initial values`() {
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
                .switchFirstAndFourthElements()

        assertSoftly {
            assertEquals(fourthValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(firstValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `switch first and fifth elements must return a quintuple with switched initial values`() {
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
                .switchFirstAndFifthElements()

        assertSoftly {
            assertEquals(fifthValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(firstValue, result.fifthElement)
        }
    }

    @Test
    fun `switch second and third elements must return a quintuple with switched initial values`() {
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
                .switchSecondAndThirdElements()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(thirdValue, result.secondElement)
            assertEquals(secondValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `switch second and fourth elements must return a quintuple with switched initial values`() {
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
                .switchSecondAndFourthElements()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(fourthValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(secondValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `switch second and fifth elements must return a quintuple with switched initial values`() {
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
                .switchSecondAndFifthElements()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(fifthValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(secondValue, result.fifthElement)
        }
    }

    @Test
    fun `switch third and fourth elements must return a quintuple with switched initial values`() {
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
                .switchThirdAndFourthElements()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(fourthValue, result.thirdElement)
            assertEquals(thirdValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `switch third and fifth elements must return a quintuple with switched initial values`() {
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
                .switchThirdAndFifthElements()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(fifthValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(thirdValue, result.fifthElement)
        }
    }

    @Test
    fun `switch fourth and fifth elements must return a quintuple with switched initial values`() {
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
                .switchFourthAndFifthElements()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fifthValue, result.fourthElement)
            assertEquals(fourthValue, result.fifthElement)
        }
    }

    @Test
    fun `to quadruple must return quadruple with first elements`() {
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
                .toQuadruple()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
        }
    }

    @Test
    fun `to triple must return triple with first elements`() {
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
                .transform { first, second, third, fourth, fifth ->
                    first.length +
                            second.length +
                            third.length +
                            fourth.length +
                            fifth.length
                }

        assertEquals(
            firstValue.length +
                    secondValue.length +
                    thirdValue.length +
                    fourthValue.length +
                    fifthValue.length,
            result
        )
    }

    @Test
    fun `transform without fifth element must return transformed result`() {
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
    fun `transform without fifth and fourth elements must return transformed result`() {
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

    @Test
    fun `transform without fifth and fourth and third elements must return transformed result`() {
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
                .transform { first, second ->
                    first.length +
                            second.length
                }

        assertEquals(
            firstValue.length +
                    secondValue.length,
            result
        )
    }

    @Test
    fun `quadruple to quintuple must return quintuple with initial elements`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val fifthValue = Uuid.random().toString()
        val result =
            Quadruple
                .of(
                    firstValue,
                    secondValue,
                    thirdValue,
                    fourthValue
                )
                .toQuintuple(fifthValue)

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
            assertEquals(fourthValue, result.fourthElement)
            assertEquals(fifthValue, result.fifthElement)
        }
    }

    @Test
    fun `triple to quintuple must return quintuple with initial elements`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val fifthValue = Uuid.random().toString()
        val result =
            Triple
                .of(
                    firstValue,
                    secondValue,
                    thirdValue
                )
                .toQuintuple(
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
    fun `pair to quintuple must return quintuple with initial elements`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val fourthValue = Uuid.random().toString()
        val fifthValue = Uuid.random().toString()
        val result =
            Pair
                .of(
                    firstValue,
                    secondValue
                )
                .toQuintuple(
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
}