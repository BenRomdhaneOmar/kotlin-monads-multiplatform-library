package com.benromdhane.omar.offroadsoft.monad.tuple

data class Pair<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any>(
    val firstElement: FIRST_ELEMENT,
    val secondElement: SECOND_ELEMENT
) {

    companion object Builder {

        fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any> of(
            firstElement: FIRST_ELEMENT,
            secondElement: SECOND_ELEMENT
        ) =
            Pair(
                firstElement,
                secondElement
            )

        fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any> of(
            firstElement: () -> FIRST_ELEMENT,
            secondElement: () -> SECOND_ELEMENT
        ) =
            Pair(
                firstElement(),
                secondElement()
            )
    }

    class Factory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any> private constructor() {

        fun first(
            firstElement: FIRST_ELEMENT
        ) =
            SecondFactory.instance<_, SECOND_ELEMENT>(
                firstElement
            )

        companion object Builder {

            fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any> instance() =
                Factory<FIRST_ELEMENT, SECOND_ELEMENT>()
        }

        class SecondFactory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any> private constructor(
            private val firstElement: FIRST_ELEMENT
        ) {

            fun second(
                secondElement: SECOND_ELEMENT
            ) =
                of(
                    this.firstElement,
                    secondElement
                )

            companion object Builder {

                fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any> instance(
                    firstElement: FIRST_ELEMENT
                ) =
                    SecondFactory<_, SECOND_ELEMENT>(
                        firstElement
                    )
            }
        }
    }
}