package com.benromdhane.omar.offroadsoft.monad

import kotlin.jvm.JvmName

sealed interface Maybe<ELEMENT : Any> {

    fun present(): Boolean
    fun empty() = present().not()
    fun orNull(): ELEMENT?
    infix fun or(element: ELEMENT): ELEMENT
    infix fun or(element: () -> ELEMENT): ELEMENT

    @Throws(EmptyMaybeException::class)
    fun orThrow(): ELEMENT
    fun <NEW_ELEMENT : Any, NULLABLE_NEW_ELEMENT : NEW_ELEMENT?> map(mapper: (ELEMENT) -> NULLABLE_NEW_ELEMENT): Maybe<NEW_ELEMENT>
    fun <NEW_ELEMENT : Any> flatMap(mapper: (ELEMENT) -> Maybe<NEW_ELEMENT>): Maybe<NEW_ELEMENT>
    fun filter(condition: (ELEMENT) -> Boolean): Maybe<ELEMENT>
    fun filterNot(condition: (ELEMENT) -> Boolean): Maybe<ELEMENT>

    class Empty<ELEMENT : Any> private constructor() : Maybe<ELEMENT> {

        override fun present() = false
        override fun orNull(): ELEMENT? = null
        override infix fun or(element: ELEMENT) = element
        override fun or(element: () -> ELEMENT) = element()

        override fun orThrow() = throw EmptyMaybeException()
        override fun <NEW_ELEMENT : Any, NULLABLE_NEW_ELEMENT : NEW_ELEMENT?> map(mapper: (ELEMENT) -> NULLABLE_NEW_ELEMENT) =
            Empty<NEW_ELEMENT>()

        override fun <NEW_ELEMENT : Any> flatMap(mapper: (ELEMENT) -> Maybe<NEW_ELEMENT>) = Empty<NEW_ELEMENT>()
        override fun filter(condition: (ELEMENT) -> Boolean) = this
        override fun filterNot(condition: (ELEMENT) -> Boolean) = this

        companion object Builder {

            fun <ELEMENT : Any> of(): Maybe<ELEMENT> = Empty()
        }
    }

    @ConsistentCopyVisibility
    data class NotEmpty<ELEMENT : Any>
    private constructor(
        private val element: ELEMENT
    ) : Maybe<ELEMENT> {

        override fun present() = true
        override fun orNull() = this.element
        override infix fun or(element: ELEMENT) = this.element
        override fun or(element: () -> ELEMENT) = this.element

        override fun orThrow() = this.element
        override fun <NEW_ELEMENT : Any, NULLABLE_NEW_ELEMENT : NEW_ELEMENT?> map(mapper: (ELEMENT) -> NULLABLE_NEW_ELEMENT) =
            mapper(this.element)
                ?.let { NotEmpty<NEW_ELEMENT>(it) }
                ?: Empty.of()

        override fun <NEW_ELEMENT : Any> flatMap(mapper: (ELEMENT) -> Maybe<NEW_ELEMENT>) = mapper(this.element)
        override fun filter(condition: (ELEMENT) -> Boolean) =
            if (condition(this.element))
                this
            else
                Empty.of()

        override fun filterNot(condition: (ELEMENT) -> Boolean) =
            if (condition(this.element).not())
                this
            else
                Empty.of()

        companion object Builder {

            fun <ELEMENT : Any> of(element: ELEMENT): Maybe<ELEMENT> = NotEmpty(element)
        }
    }

    class EmptyMaybeException : Throwable()
}

fun <ELEMENT : Any> Maybe<Maybe<ELEMENT>>.flatten() = this.flatMap { it }

@JvmName("nonNullableAsMaybe")
fun <ELEMENT : Any> ELEMENT.asMaybe() = Maybe.NotEmpty.of(this)

@JvmName("nullableAsMaybe")
fun <ELEMENT : Any, NULLABLE_ELEMENT : ELEMENT?> NULLABLE_ELEMENT.asMaybe() =
    this
        ?.let { Maybe.NotEmpty.of<ELEMENT>(it) }
        ?: Maybe.Empty.of()