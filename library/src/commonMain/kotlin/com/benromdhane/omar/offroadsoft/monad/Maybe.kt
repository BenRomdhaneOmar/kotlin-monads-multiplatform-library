package com.benromdhane.omar.offroadsoft.monad

sealed interface Maybe<ELEMENT> {

    fun present(): Boolean
    fun empty() = present().not()

    class Empty<ELEMENT> private constructor() : Maybe<ELEMENT> {

        override fun present() = false

        companion object Builder {

            fun <ELEMENT> of(): Maybe<ELEMENT> = Empty()
        }
    }

    @ConsistentCopyVisibility
    data class NotEmpty<ELEMENT> private constructor(
        private val element: ELEMENT
    ) : Maybe<ELEMENT> {

        override fun present() = true

        companion object Builder {

            fun <ELEMENT> of(element: ELEMENT): Maybe<ELEMENT> =
                NotEmpty(
                    element
                )
        }
    }
}