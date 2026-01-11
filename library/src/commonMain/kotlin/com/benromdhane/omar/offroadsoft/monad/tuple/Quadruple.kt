package com.benromdhane.omar.offroadsoft.monad.tuple

data class Quadruple<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any>(
    val firstElement: FIRST_ELEMENT,
    val secondElement: SECOND_ELEMENT,
    val thirdElement: THIRD_ELEMENT,
    val fourthElement: FOURTH_ELEMENT
) {

    fun <NEW_FIRST_ELEMENT : Any> mapFirst(
        mapper: (FIRST_ELEMENT) -> NEW_FIRST_ELEMENT
    ) =
        Quadruple(
            mapper(this.firstElement),
            this.secondElement,
            this.thirdElement,
            this.fourthElement
        )

    fun <NEW_SECOND_ELEMENT : Any> mapSecond(
        mapper: (SECOND_ELEMENT) -> NEW_SECOND_ELEMENT
    ) =
        Quadruple(
            this.firstElement,
            mapper(this.secondElement),
            this.thirdElement,
            this.fourthElement
        )

    fun <NEW_THIRD_ELEMENT : Any> mapThird(
        mapper: (THIRD_ELEMENT) -> NEW_THIRD_ELEMENT
    ) =
        Quadruple(
            this.firstElement,
            this.secondElement,
            mapper(this.thirdElement),
            this.fourthElement
        )

    fun <NEW_FOURTH_ELEMENT : Any> mapFourth(
        mapper: (FOURTH_ELEMENT) -> NEW_FOURTH_ELEMENT
    ) =
        Quadruple(
            this.firstElement,
            this.secondElement,
            this.thirdElement,
            mapper(this.fourthElement)
        )

    fun switchFirstAndSecondElements() =
        Quadruple(
            this.secondElement,
            this.firstElement,
            this.thirdElement,
            this.fourthElement
        )

    fun switchFirstAndThirdElements() =
        Quadruple(
            this.thirdElement,
            this.secondElement,
            this.firstElement,
            this.fourthElement
        )

    fun switchFirstAndFourthElements() =
        Quadruple(
            this.fourthElement,
            this.secondElement,
            this.thirdElement,
            this.firstElement
        )

    fun switchSecondAndThirdElements() =
        Quadruple(
            this.firstElement,
            this.thirdElement,
            this.secondElement,
            this.fourthElement
        )

    fun switchSecondAndFourthElements() =
        Quadruple(
            this.firstElement,
            this.fourthElement,
            this.thirdElement,
            this.secondElement
        )

    fun switchThirdAndFourthElements() =
        Quadruple(
            this.firstElement,
            this.secondElement,
            this.fourthElement,
            this.thirdElement
        )

    fun toTriple() =
        Triple.of(
            this.firstElement,
            this.secondElement,
            this.thirdElement
        )

    fun toPair() =
        Pair.of(
            this.firstElement,
            this.secondElement
        )

    fun <RESULT> transform(
        transformer: (FIRST_ELEMENT, SECOND_ELEMENT, THIRD_ELEMENT, FOURTH_ELEMENT) -> RESULT
    ) =
        transformer(
            this.firstElement,
            this.secondElement,
            this.thirdElement,
            this.fourthElement
        )

    companion object Builder {

        fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any> of(
            firstElement: FIRST_ELEMENT,
            secondElement: SECOND_ELEMENT,
            thirdElement: THIRD_ELEMENT,
            fourthElement: FOURTH_ELEMENT
        ) =
            Quadruple(
                firstElement,
                secondElement,
                thirdElement,
                fourthElement
            )

        fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any> of(
            firstElement: () -> FIRST_ELEMENT,
            secondElement: () -> SECOND_ELEMENT,
            thirdElement: () -> THIRD_ELEMENT,
            fourthElement: () -> FOURTH_ELEMENT
        ) =
            Quadruple(
                firstElement(),
                secondElement(),
                thirdElement(),
                fourthElement()
            )
    }

    class Factory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any> private constructor() {

        fun first(
            firstElement: FIRST_ELEMENT
        ) =
            SecondFactory
                .instance<_, SECOND_ELEMENT, THIRD_ELEMENT, FOURTH_ELEMENT>(
                    firstElement
                )

        companion object Builder {

            fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any> instance() =
                Factory<FIRST_ELEMENT, SECOND_ELEMENT, THIRD_ELEMENT, FOURTH_ELEMENT>()
        }

        class SecondFactory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any> private constructor(
            private val firstElement: FIRST_ELEMENT
        ) {

            fun second(
                secondElement: SECOND_ELEMENT
            ) =
                ThirdFactory
                    .instance<_, _, THIRD_ELEMENT, FOURTH_ELEMENT>(
                        this.firstElement,
                        secondElement
                    )

            internal companion object Builder {

                fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any> instance(
                    firstElement: FIRST_ELEMENT
                ) =
                    SecondFactory<_, SECOND_ELEMENT, THIRD_ELEMENT, FOURTH_ELEMENT>(
                        firstElement
                    )
            }

            class ThirdFactory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any> private constructor(
                private val firstElement: FIRST_ELEMENT,
                private val secondElement: SECOND_ELEMENT
            ) {

                fun third(
                    thirdElement: THIRD_ELEMENT
                ) =
                    FourthFactory
                        .instance<_, _, _, FOURTH_ELEMENT>(
                            this.firstElement,
                            this.secondElement,
                            thirdElement
                        )

                internal companion object Builder {

                    fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any> instance(
                        firstElement: FIRST_ELEMENT,
                        secondElement: SECOND_ELEMENT
                    ) =
                        ThirdFactory<_, _, THIRD_ELEMENT, FOURTH_ELEMENT>(
                            firstElement,
                            secondElement
                        )
                }

                class FourthFactory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any> private constructor(
                    private val firstElement: FIRST_ELEMENT,
                    private val secondElement: SECOND_ELEMENT,
                    private val thirdElement: THIRD_ELEMENT
                ) {

                    fun fourth(
                        fourthElement: FOURTH_ELEMENT
                    ) =
                        of(
                            this.firstElement,
                            this.secondElement,
                            this.thirdElement,
                            fourthElement
                        )

                    internal companion object Builder {

                        fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any> instance(
                            firstElement: FIRST_ELEMENT,
                            secondElement: SECOND_ELEMENT,
                            thirdElement: THIRD_ELEMENT
                        ) =
                            FourthFactory<_, _, _, FOURTH_ELEMENT>(
                                firstElement,
                                secondElement,
                                thirdElement
                            )
                    }
                }
            }
        }
    }
}