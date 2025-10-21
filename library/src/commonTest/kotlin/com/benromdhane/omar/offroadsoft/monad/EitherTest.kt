package com.benromdhane.omar.offroadsoft.monad

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class EitherTest {

    @Test
    fun `right must return true if either was initiated as right`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Right
                .of<String, Any>(initialElement)
                .right()

        assertTrue { result }
    }

    @Test
    fun `right must return false if either was initiated as left`() {
        val initialElement = Uuid.random().toString()
        val result =
            Either
                .Left
                .of<Any, String>(initialElement)
                .right()

        assertFalse { result }
    }
}