package com.benromdhane.omar.offroadsoft.monad

import kotlin.test.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MaybeTest {

    @Test
    fun `present must return true if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .present()

        assertTrue { result }
    }

    @Test
    fun `empty must return false if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .empty()

        assertFalse { result }
    }

    @Test
    fun `present must return false if maybe was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Any>()
                .present()

        assertFalse { result }
    }

    @Test
    fun `empty must return true if maybe was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Any>()
                .empty()

        assertTrue { result }
    }

    @Test
    fun `orNull must return null if maybe was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Any>()
                .orNull()

        assertNull(result)
    }

    @Test
    fun `orNull must return the initial element if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `or with element parameter must return the provided element if maybe was initiated as empty`() {
        val element = Uuid.random().toString()
        val result =
            Maybe
                .Empty
                .of<String>()
                .or(element)

        assertEquals(element, result)
    }

    @Test
    fun `or with element parameter must return the initial element if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val secondElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .or(secondElement)

        assertEquals(initialElement, result)
    }

    @Test
    fun `or throw must throw empty maybe exception if maybe was initiated as empty`() {
        assertFailsWith<Maybe.EmptyMaybeException> {
            Maybe
                .Empty
                .of<String>()
                .orThrow()
        }
    }

    @Test
    fun `or throw must return the initial element if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .orThrow()

        assertEquals(initialElement, result)
    }

    @Test
    fun `map must be ignored if maybe was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<String>()
                .map { it.length }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `map must transform the initial element if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .map { it.length }
                .orNull()!!

        assertEquals(initialElement.length, result)
    }

    @Test
    fun `map must transform the previous statue if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .map { it.length }
                .map { it.times(10) }
                .orNull()!!

        assertEquals(initialElement.length.times(10), result)
    }

    @Test
    fun `flat map must transform to empty if the mapping result is empty maybe and if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .flatMap { Maybe.Empty.of<Int>() }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `flat map must transform the previous statue if the mapping result is non empty maybe and if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .flatMap { Maybe.NotEmpty.of(it.length) }
                .orNull()!!

        assertEquals(initialElement.length, result)
    }

    @Test
    fun `flat map must return empty if the mapping result is non empty maybe and if maybe was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Int>()
                .flatMap { Maybe.NotEmpty.of(Uuid.random().toString()) }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `flat map must return empty if the mapping result is empty maybe and if maybe was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Int>()
                .flatMap { Maybe.Empty.of<String>() }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `flatten must return empty if the inner maybe is empty and if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .map { Maybe.Empty.of<Int>() }
                .flatten()
                .empty()

        assertTrue { result }
    }

    @Test
    fun `flatten must return non empty single level maybe if the inner maybe is not empty and if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .map { Maybe.NotEmpty.of(it.length) }
                .flatten()
                .orNull()!!

        assertEquals(initialElement.length, result)
    }

    @Test
    fun `flatten must return empty maybe if the inner maybe is non empty and if maybe was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Int>()
                .flatMap { Maybe.NotEmpty.of(Uuid.random().toString()) }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `flatten must return empty maybe if the inner maybe is empty and if maybe was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Int>()
                .map { Maybe.Empty.of<String>() }
                .flatten()
                .empty()

        assertTrue { result }
    }

    @Test
    fun `filter must return empty maybe if maybe was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Int>()
                .filter { true }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `filter must return empty maybe if the filter does not apply on the actual value and if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .filter { it.isEmpty() }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `filter must return non empty maybe if the filter apply on the actual value and if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .filter { it.isNotEmpty() }
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `filter not must return empty maybe if maybe was initiated as empty`() {
        val result =
            Maybe
                .Empty
                .of<Int>()
                .filterNot { true }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `filter not must return non empty maybe if the filter does not apply on the actual value and if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .filterNot { it.isEmpty() }
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `filter not must return empty maybe if the filter apply on the actual value and if maybe was initiated as non empty`() {
        val initialElement = Uuid.random().toString()
        val result =
            Maybe
                .NotEmpty
                .of(initialElement)
                .filterNot { it.isNotEmpty() }
                .empty()

        assertTrue { result }
    }

    @Test
    fun `as maybe must return not empty maybe with the initial element for non nullable element`() {
        val initialElement = Uuid.random().toString()
        val result =
            initialElement.asMaybe()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `as maybe must return not empty maybe with the initial element for nullable non null element`() {
        @Suppress("RedundantNullableReturnType")
        val initialElement: String? = Uuid.random().toString()
        val result =
            initialElement.asMaybe()
                .orNull()!!

        assertEquals(initialElement, result)
    }

    @Test
    fun `as maybe must return empty maybe for nullable null element`() {
        val initialElement: String? = null
        val result =
            initialElement.asMaybe()
                .empty()

        assertTrue { result }
    }
}