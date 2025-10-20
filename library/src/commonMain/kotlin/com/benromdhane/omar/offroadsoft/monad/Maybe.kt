package com.benromdhane.omar.offroadsoft.monad

sealed interface Maybe<ELEMENT> {

    fun present(): Boolean
    fun empty() = present().not()
    fun orNull(): ELEMENT?
    fun or(element: ELEMENT): ELEMENT

    class Empty<ELEMENT> private constructor() : Maybe<ELEMENT> {

        override fun present() = false
        override fun orNull(): ELEMENT? = null
        override fun or(element: ELEMENT) = element

        companion object Builder {

            fun <ELEMENT> of(): Maybe<ELEMENT> = Empty()
        }
    }

    @ConsistentCopyVisibility
    data class NotEmpty<ELEMENT> private constructor(
        private val element: ELEMENT
    ) : Maybe<ELEMENT> {

        override fun present() = true
        override fun orNull() = this.element
        override fun or(element: ELEMENT) = this.element

        companion object Builder {

            fun <ELEMENT> of(element: ELEMENT): Maybe<ELEMENT> =
                NotEmpty(
                    element
                )
        }
    }
}