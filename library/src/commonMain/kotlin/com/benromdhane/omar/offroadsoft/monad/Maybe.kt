package com.benromdhane.omar.offroadsoft.monad

sealed interface Maybe<ELEMENT> {

    fun present(): Boolean
    fun empty() = present().not()
    fun orNull(): ELEMENT?
    fun or(element: ELEMENT): ELEMENT

    @Throws(EmptyMaybeException::class)
    fun orThrow(): ELEMENT
    fun <NEW_ELEMENT> map(mapper: (ELEMENT) -> NEW_ELEMENT): Maybe<NEW_ELEMENT>
    fun <NEW_ELEMENT> flatMap(mapper: (ELEMENT) -> Maybe<NEW_ELEMENT>): Maybe<NEW_ELEMENT>
    fun filter(condition: (ELEMENT) -> Boolean): Maybe<ELEMENT>

    class Empty<ELEMENT> private constructor() : Maybe<ELEMENT> {

        override fun present() = false
        override fun orNull(): ELEMENT? = null
        override fun or(element: ELEMENT) = element
        override fun orThrow() = throw EmptyMaybeException()
        override fun <NEW_ELEMENT> map(mapper: (ELEMENT) -> NEW_ELEMENT) = Empty<NEW_ELEMENT>()
        override fun <NEW_ELEMENT> flatMap(mapper: (ELEMENT) -> Maybe<NEW_ELEMENT>) = Empty<NEW_ELEMENT>()
        override fun filter(condition: (ELEMENT) -> Boolean) = this

        companion object Builder {

            fun <ELEMENT> of(): Maybe<ELEMENT> = Empty()
        }
    }

    @ConsistentCopyVisibility
    data class NotEmpty<ELEMENT>
    private constructor(
        private val element: ELEMENT
    ) : Maybe<ELEMENT> {

        override fun present() = true
        override fun orNull() = this.element
        override fun or(element: ELEMENT) = this.element
        override fun orThrow() = this.element
        override fun <NEW_ELEMENT> map(mapper: (ELEMENT) -> NEW_ELEMENT) = NotEmpty(mapper(this.element))

        override fun <NEW_ELEMENT> flatMap(mapper: (ELEMENT) -> Maybe<NEW_ELEMENT>) = mapper(this.element)
        override fun filter(condition: (ELEMENT) -> Boolean) =
            if (condition(this.element))
                this
            else
                Empty.of()

        companion object Builder {

            fun <ELEMENT> of(element: ELEMENT): Maybe<ELEMENT> = NotEmpty(element)
        }
    }

    class EmptyMaybeException : Throwable()
}

fun <ELEMENT> Maybe<Maybe<ELEMENT>>.flatten() = this.flatMap { it }