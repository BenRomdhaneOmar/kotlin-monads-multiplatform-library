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
}