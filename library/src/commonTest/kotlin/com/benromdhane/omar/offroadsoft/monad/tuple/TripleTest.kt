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
    fun `map first must return a triple with mapped initial first values`() {
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

    @Test
    fun `map second must return a triple with mapped initial second values`() {
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
                .mapSecond { it.length }

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue.length, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
        }
    }

    @Test
    fun `map third must return a triple with mapped initial third values`() {
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
                .mapThird { it.length }

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue.length, result.thirdElement)
        }
    }

    @Test
    fun `switch first and second elements must return a triple with switched initial values`() {
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
                .switchFirstAndSecondElements()

        assertSoftly {
            assertEquals(secondValue, result.firstElement)
            assertEquals(firstValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
        }
    }

    @Test
    fun `switch first and third elements must return a triple with switched initial values`() {
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
                .switchFirstAndThirdElements()

        assertSoftly {
            assertEquals(thirdValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(firstValue, result.thirdElement)
        }
    }

    @Test
    fun `switch second and third elements must return a triple with switched initial values`() {
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
                .switchSecondAndThirdElements()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(thirdValue, result.secondElement)
            assertEquals(secondValue, result.thirdElement)
        }
    }

    @Test
    fun `to pair must return pair with triple first and second elements`() {
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
                .toPair()

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
        }
    }

    @Test
    fun `pair to triplet must return triple with initial elements`() {
        val firstValue = Uuid.random().toString()
        val secondValue = Uuid.random().toString()
        val thirdValue = Uuid.random().toString()
        val result =
            Pair
                .of(
                    firstValue,
                    secondValue
                )
                .toTriple(
                    thirdValue
                )

        assertSoftly {
            assertEquals(firstValue, result.firstElement)
            assertEquals(secondValue, result.secondElement)
            assertEquals(thirdValue, result.thirdElement)
        }
    }

    @Test
    fun `transform must return transformed result`() {
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
                .transform { first, second, third ->
                    first.length + second.length + third.length
                }

        assertEquals(firstValue.length + secondValue.length + thirdValue.length, result)
    }
}