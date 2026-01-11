package com.benromdhane.omar.offroadsoft.monad.tuple


data class Quintuple<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any>(
    val firstElement: FIRST_ELEMENT,
    val secondElement: SECOND_ELEMENT,
    val thirdElement: THIRD_ELEMENT,
    val fourthElement: FOURTH_ELEMENT,
    val fifthElement: FIFTH_ELEMENT
) {

    fun <NEW_FIRST_ELEMENT : Any> mapFirst(
        mapper: (FIRST_ELEMENT) -> NEW_FIRST_ELEMENT
    ) =
        Quintuple(
            mapper(this.firstElement),
            this.secondElement,
            this.thirdElement,
            this.fourthElement,
            this.fifthElement
        )

    fun <NEW_SECOND_ELEMENT : Any> mapSecond(
        mapper: (SECOND_ELEMENT) -> NEW_SECOND_ELEMENT
    ) =
        Quintuple(
            this.firstElement,
            mapper(this.secondElement),
            this.thirdElement,
            this.fourthElement,
            this.fifthElement
        )

    fun <NEW_THIRD_ELEMENT : Any> mapThird(
        mapper: (THIRD_ELEMENT) -> NEW_THIRD_ELEMENT
    ) =
        Quintuple(
            this.firstElement,
            this.secondElement,
            mapper(this.thirdElement),
            this.fourthElement,
            this.fifthElement
        )

    fun <NEW_FOURTH_ELEMENT : Any> mapFourth(
        mapper: (FOURTH_ELEMENT) -> NEW_FOURTH_ELEMENT
    ) =
        Quintuple(
            this.firstElement,
            this.secondElement,
            this.thirdElement,
            mapper(this.fourthElement),
            this.fifthElement
        )

    fun <NEW_FIFTH_ELEMENT : Any> mapFifth(
        mapper: (FIFTH_ELEMENT) -> NEW_FIFTH_ELEMENT
    ) =
        Quintuple(
            this.firstElement,
            this.secondElement,
            this.thirdElement,
            this.fourthElement,
            mapper(this.fifthElement)
        )

    fun switchFirstAndSecondElements() =
        Quintuple(
            this.secondElement,
            this.firstElement,
            this.thirdElement,
            this.fourthElement,
            this.fifthElement
        )

    fun switchFirstAndThirdElements() =
        Quintuple(
            this.thirdElement,
            this.secondElement,
            this.firstElement,
            this.fourthElement,
            this.fifthElement
        )

    fun switchFirstAndFourthElements() =
        Quintuple(
            this.fourthElement,
            this.secondElement,
            this.thirdElement,
            this.firstElement,
            this.fifthElement
        )

    fun switchFirstAndFifthElements() =
        Quintuple(
            this.fifthElement,
            this.secondElement,
            this.thirdElement,
            this.fourthElement,
            this.firstElement
        )

    companion object Builder {

        fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> of(
            firstElement: FIRST_ELEMENT,
            secondElement: SECOND_ELEMENT,
            thirdElement: THIRD_ELEMENT,
            fourthElement: FOURTH_ELEMENT,
            fifthElement: FIFTH_ELEMENT
        ) =
            Quintuple(
                firstElement,
                secondElement,
                thirdElement,
                fourthElement,
                fifthElement
            )

        fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> of(
            firstElement: () -> FIRST_ELEMENT,
            secondElement: () -> SECOND_ELEMENT,
            thirdElement: () -> THIRD_ELEMENT,
            fourthElement: () -> FOURTH_ELEMENT,
            fifthElement: () -> FIFTH_ELEMENT
        ) =
            Quintuple(
                firstElement(),
                secondElement(),
                thirdElement(),
                fourthElement(),
                fifthElement()
            )
    }

    class Factory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> private constructor() {

        fun first(
            firstElement: FIRST_ELEMENT
        ) =
            SecondFactory
                .instance<_, SECOND_ELEMENT, THIRD_ELEMENT, FOURTH_ELEMENT, FIFTH_ELEMENT>(
                    firstElement
                )

        companion object Builder {

            fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> instance() =
                Factory<FIRST_ELEMENT, SECOND_ELEMENT, THIRD_ELEMENT, FOURTH_ELEMENT, FIFTH_ELEMENT>()
        }

        class SecondFactory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> private constructor(
            private val firstElement: FIRST_ELEMENT
        ) {

            fun second(
                secondElement: SECOND_ELEMENT
            ) =
                ThirdFactory
                    .instance<_, _, THIRD_ELEMENT, FOURTH_ELEMENT, FIFTH_ELEMENT>(
                        this.firstElement,
                        secondElement
                    )

            internal companion object Builder {

                fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> instance(
                    firstElement: FIRST_ELEMENT
                ) =
                    SecondFactory<_, SECOND_ELEMENT, THIRD_ELEMENT, FOURTH_ELEMENT, FIFTH_ELEMENT>(
                        firstElement
                    )
            }

            class ThirdFactory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> private constructor(
                private val firstElement: FIRST_ELEMENT,
                private val secondElement: SECOND_ELEMENT
            ) {

                fun third(
                    thirdElement: THIRD_ELEMENT
                ) =
                    FourthFactory
                        .instance<_, _, _, FOURTH_ELEMENT, FIFTH_ELEMENT>(
                            this.firstElement,
                            this.secondElement,
                            thirdElement
                        )

                internal companion object Builder {

                    fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> instance(
                        firstElement: FIRST_ELEMENT,
                        secondElement: SECOND_ELEMENT
                    ) =
                        ThirdFactory<_, _, THIRD_ELEMENT, FOURTH_ELEMENT, FIFTH_ELEMENT>(
                            firstElement,
                            secondElement
                        )
                }

                class FourthFactory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> private constructor(
                    private val firstElement: FIRST_ELEMENT,
                    private val secondElement: SECOND_ELEMENT,
                    private val thirdElement: THIRD_ELEMENT
                ) {

                    fun fourth(
                        fourthElement: FOURTH_ELEMENT
                    ) =
                        FifthFactory
                            .instance<_, _, _, _, FIFTH_ELEMENT>(
                                this.firstElement,
                                this.secondElement,
                                this.thirdElement,
                                fourthElement
                            )

                    internal companion object Builder {

                        fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> instance(
                            firstElement: FIRST_ELEMENT,
                            secondElement: SECOND_ELEMENT,
                            thirdElement: THIRD_ELEMENT
                        ) =
                            FourthFactory<_, _, _, FOURTH_ELEMENT, FIFTH_ELEMENT>(
                                firstElement,
                                secondElement,
                                thirdElement
                            )
                    }

                    class FifthFactory<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> private constructor(
                        private val firstElement: FIRST_ELEMENT,
                        private val secondElement: SECOND_ELEMENT,
                        private val thirdElement: THIRD_ELEMENT,
                        private val fourthElement: FOURTH_ELEMENT,
                    ) {

                        fun fifth(
                            fifthElement: FIFTH_ELEMENT
                        ) =
                            of(
                                this.firstElement,
                                this.secondElement,
                                this.thirdElement,
                                this.fourthElement,
                                fifthElement
                            )

                        internal companion object Builder {

                            fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, THIRD_ELEMENT : Any, FOURTH_ELEMENT : Any, FIFTH_ELEMENT : Any> instance(
                                firstElement: FIRST_ELEMENT,
                                secondElement: SECOND_ELEMENT,
                                thirdElement: THIRD_ELEMENT,
                                fourthElement: FOURTH_ELEMENT
                            ) =
                                FifthFactory<_, _, _, _, FIFTH_ELEMENT>(
                                    firstElement,
                                    secondElement,
                                    thirdElement,
                                    fourthElement
                                )
                        }
                    }
                }
            }
        }
    }
}