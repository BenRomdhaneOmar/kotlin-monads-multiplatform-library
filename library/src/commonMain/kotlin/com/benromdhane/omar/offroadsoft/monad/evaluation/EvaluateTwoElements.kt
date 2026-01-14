package com.benromdhane.omar.offroadsoft.monad.evaluation

import com.benromdhane.omar.offroadsoft.monad.error.PossibleError
import com.benromdhane.omar.offroadsoft.monad.error.SuccessOrMultipleErrors
import com.benromdhane.omar.offroadsoft.monad.tuple.Pair

class EvaluateTwoElements<FIRST_ELEMENT : Any, ERROR : Any> private constructor(
    private val firstElement: FIRST_ELEMENT
) {

    fun addEvaluation(
        error: () -> ERROR,
        check: (FIRST_ELEMENT) -> Boolean
    ) =
        FirstLevelPreparedEvaluation
            .of(
                this.firstElement,
                error,
                check
            )

    companion object {

        fun <FIRST_ELEMENT : Any, ERROR : Any> first(
            firstElement: FIRST_ELEMENT
        ) =
            EvaluateTwoElements<_, ERROR>(
                firstElement
            )
    }

    class FirstLevelPreparedEvaluation<FIRST_ELEMENT : Any, ERROR : Any> private constructor(
        private val firstElement: FIRST_ELEMENT,
        private val firstElementEvaluations: Set<Evaluation<FIRST_ELEMENT, ERROR>>
    ) {

        fun addEvaluation(
            error: () -> ERROR,
            check: (FIRST_ELEMENT) -> Boolean
        ) =
            FirstLevelPreparedEvaluation(
                firstElement,
                this.firstElementEvaluations
                    .plus(
                        Evaluation
                            .of(
                                error,
                                check
                            )
                    )
            )

        fun <SECOND_ELEMENT : Any> second(
            secondElement: SECOND_ELEMENT
        ) =
            SecondLevelNonPreparedEvaluation
                .of(
                    this.firstElement,
                    this.firstElementEvaluations,
                    secondElement
                )

        internal companion object Builder {

            fun <FIRST_ELEMENT : Any, ERROR : Any> of(
                firstElement: FIRST_ELEMENT,
                error: () -> ERROR,
                check: (FIRST_ELEMENT) -> Boolean
            ) =
                FirstLevelPreparedEvaluation(
                    firstElement,
                    setOf(
                        Evaluation
                            .of(
                                error,
                                check
                            )
                    )
                )
        }

        class SecondLevelNonPreparedEvaluation<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, ERROR : Any> private constructor(
            private val firstElement: FIRST_ELEMENT,
            private val firstElementEvaluations: Set<Evaluation<FIRST_ELEMENT, ERROR>>,
            private val secondElement: SECOND_ELEMENT
        ) {

            fun addEvaluation(
                error: () -> ERROR,
                check: (SECOND_ELEMENT) -> Boolean
            ) =
                SecondLevelPreparedEvaluation
                    .of(
                        this.firstElement,
                        this.firstElementEvaluations,
                        this.secondElement,
                        Evaluation
                            .of(
                                error,
                                check
                            )
                    )

            internal companion object Builder {

                internal fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, ERROR : Any> of(
                    firstElement: FIRST_ELEMENT,
                    firstElementEvaluations: Set<Evaluation<FIRST_ELEMENT, ERROR>>,
                    secondElement: SECOND_ELEMENT
                ) =
                    SecondLevelNonPreparedEvaluation(
                        firstElement,
                        firstElementEvaluations,
                        secondElement
                    )
            }

            class SecondLevelPreparedEvaluation<FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, ERROR : Any> private constructor(
                private val firstElement: FIRST_ELEMENT,
                private val firstElementEvaluations: Set<Evaluation<FIRST_ELEMENT, ERROR>>,
                private val secondElement: SECOND_ELEMENT,
                private val secondElementEvaluations: Set<Evaluation<SECOND_ELEMENT, ERROR>>
            ) {

                fun addEvaluation(
                    error: () -> ERROR,
                    check: (SECOND_ELEMENT) -> Boolean
                ) =
                    SecondLevelPreparedEvaluation(
                        this.firstElement,
                        this.firstElementEvaluations,
                        this.secondElement,
                        this.secondElementEvaluations
                            .plus(
                                Evaluation
                                    .of(
                                        error,
                                        check
                                    )
                            )
                    )

                fun evaluate() =
                    evaluateFirst()
                        .toErrors()
                        .plus(
                            evaluateSecond().toErrors()
                        )
                        .toSet()
                        .toSuccessOrMultipleErrors(
                            Pair.of(
                                this.firstElement,
                                this.secondElement
                            )
                        )

                private fun evaluateFirst() =
                    evaluate(
                        this.firstElementEvaluations,
                        this.firstElement
                    )

                private fun evaluateSecond() =
                    evaluate(
                        this.secondElementEvaluations,
                        this.secondElement
                    )

                internal companion object Builder {

                    internal fun <FIRST_ELEMENT : Any, SECOND_ELEMENT : Any, ERROR : Any> of(
                        firstElement: FIRST_ELEMENT,
                        firstElementEvaluations: Set<Evaluation<FIRST_ELEMENT, ERROR>>,
                        secondElement: SECOND_ELEMENT,
                        secondElementEvaluation: Evaluation<SECOND_ELEMENT, ERROR>
                    ) =
                        SecondLevelPreparedEvaluation(
                            firstElement,
                            firstElementEvaluations,
                            secondElement,
                            setOf(
                                secondElementEvaluation
                            )
                        )
                }
            }
        }
    }

    @ConsistentCopyVisibility
    internal data class Evaluation<ELEMENT : Any, ERROR : Any> private constructor(
        val error: () -> ERROR,
        val check: (ELEMENT) -> Boolean
    ) {
        internal fun evaluate(
            element: ELEMENT
        ) =
            if (this.check(element))
                PossibleError.Success.of()
            else
                PossibleError.Error.of(this.error())

        internal companion object Builder {

            internal fun <ELEMENT : Any, ERROR : Any> of(
                error: () -> ERROR,
                check: (ELEMENT) -> Boolean
            ) =
                Evaluation(
                    error,
                    check
                )
        }
    }
}

private fun <ELEMENT : Any, ERROR : Any> evaluate(
    evaluations: Set<EvaluateTwoElements.Evaluation<ELEMENT, ERROR>>,
    element: ELEMENT
) =
    evaluations
        .map { it.evaluate(element) }
        .filter { it.error() }
        .map { it.toMaybeError() }
        .filter { it.present() }
        .map { it.orNull() }
        .map { it!! }
        .toSuccessOrMultipleErrors(element)

private fun <ELEMENT : Any, ERROR : Any> Collection<ERROR>.toSuccessOrMultipleErrors(element: ELEMENT) =
    if (this.isEmpty())
        SuccessOrMultipleErrors.Success.of(element)
    else
        SuccessOrMultipleErrors.Error
            .of<ELEMENT, ERROR>(this.first())
            .addErrors(this.drop(1))