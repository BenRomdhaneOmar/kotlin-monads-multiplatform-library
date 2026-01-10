package com.benromdhane.omar.offroadsoft.monad.tuple

data class Triple<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any>(
    val firstElement: FIRST_ELEMENT,
    val secondElement: SECOND_ELEMENT,
    val thirdElement: THIRD_ELEMENT
) {

    fun <NEW_FIRST_ELEMENT : Any> mapFirst(
        mapper: (FIRST_ELEMENT) -> NEW_FIRST_ELEMENT
    ) =
        Triple(
            mapper(this.firstElement),
            this.secondElement,
            this.thirdElement
        )

    fun <NEW_SECOND_ELEMENT : Any> mapSecond(
        mapper: (SECOND_ELEMENT) -> NEW_SECOND_ELEMENT
    ) =
        Triple(
            this.firstElement,
            mapper(this.secondElement),
            this.thirdElement
        )

    fun <NEW_THIRD_ELEMENT : Any> mapThird(
        mapper: (THIRD_ELEMENT) -> NEW_THIRD_ELEMENT
    ) =
        Triple(
            this.firstElement,
            this.secondElement,
            mapper(this.thirdElement)
        )

    fun switchFirstAndSecondElements() =
        Triple(
            this.secondElement,
            this.firstElement,
            this.thirdElement
        )

    fun switchFirstAndThirdElements() =
        Triple(
            this.thirdElement,
            this.secondElement,
            this.firstElement
        )

    companion object Builder {

        fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any> of(
            firstElement: FIRST_ELEMENT,
            secondElement: SECOND_ELEMENT,
            thirdElement: THIRD_ELEMENT
        ) =
            Triple(
                firstElement,
                secondElement,
                thirdElement
            )

        fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any> of(
            firstElement: () -> FIRST_ELEMENT,
            secondElement: () -> SECOND_ELEMENT,
            thirdElement: () -> THIRD_ELEMENT
        ) =
            Triple(
                firstElement(),
                secondElement(),
                thirdElement()
            )
    }

    class Factory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any> private constructor() {

        fun first(
            firstElement: FIRST_ELEMENT
        ) =
            SecondFactory
                .instance<_, SECOND_ELEMENT, THIRD_ELEMENT>(
                    firstElement
                )

        companion object Builder {

            fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any> instance() =
                Factory<FIRST_ELEMENT, SECOND_ELEMENT, THIRD_ELEMENT>()
        }

        class SecondFactory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any> private constructor(
            private val firstElement: FIRST_ELEMENT
        ) {

            fun second(
                secondElement: SECOND_ELEMENT
            ) =
                ThirdFactory
                    .instance<_, _, THIRD_ELEMENT>(
                        this.firstElement,
                        secondElement
                    )

            internal companion object Builder {

                fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any> instance(
                    firstElement: FIRST_ELEMENT
                ) =
                    SecondFactory<_, SECOND_ELEMENT, THIRD_ELEMENT>(
                        firstElement
                    )
            }

            class ThirdFactory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any> private constructor(
                private val firstElement: FIRST_ELEMENT,
                private val secondElement: SECOND_ELEMENT
            ) {

                fun third(
                    thirdElement: THIRD_ELEMENT
                ) =
                    of(
                        this.firstElement,
                        this.secondElement,
                        thirdElement
                    )

                internal companion object Builder {

                    fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any> instance(
                        firstElement: FIRST_ELEMENT,
                        secondElement: SECOND_ELEMENT
                    ) =
                        ThirdFactory<_, _, THIRD_ELEMENT>(
                            firstElement,
                            secondElement
                        )
                }
            }
        }
    }
}