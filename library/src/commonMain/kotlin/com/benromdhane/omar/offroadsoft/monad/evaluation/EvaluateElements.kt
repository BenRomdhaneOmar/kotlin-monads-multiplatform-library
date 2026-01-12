package com.benromdhane.omar.offroadsoft.monad.evaluation

import com.benromdhane.omar.offroadsoft.monad.error.PossibleError
import com.benromdhane.omar.offroadsoft.monad.error.SuccessOrMultipleErrors

sealed interface EvaluateElements {

    class One<ELEMENT : Any, ERROR : Any> private constructor(
        private val element: ELEMENT
    ) {

        fun addEvaluation(
            error: () -> ERROR,
            check: (ELEMENT) -> Boolean
        ) =
            PreparedEvaluation
                .of(
                    this.element,
                    error,
                    check
                )

        companion object {

            fun <ELEMENT : Any, ERROR : Any> element(
                element: ELEMENT
            ) =
                One<_, ERROR>(
                    element
                )
        }

        class PreparedEvaluation<ELEMENT : Any, ERROR : Any> private constructor(
            private val element: ELEMENT,
            private val evaluations: Set<Evaluation<ELEMENT, ERROR>>
        ) {

            fun addEvaluation(
                error: () -> ERROR,
                check: (ELEMENT) -> Boolean
            ) =
                PreparedEvaluation(
                    this.element,
                    this.evaluations
                        .plus(
                            Evaluation
                                .of(
                                    error,
                                    check
                                )
                        )
                )

            fun evaluate() =
                this.evaluations
                    .map { it.evaluate(this.element) }
                    .filter { it.error() }
                    .map { it.toMaybeError() }
                    .filter { it.present() }
                    .map { it.orNull() }
                    .map { it!! }
                    .toSuccessOrMultipleErrors(this.element)

            internal companion object Builder {

                internal fun <ELEMENT : Any, ERROR : Any> of(
                    element: ELEMENT,
                    error: () -> ERROR,
                    check: (ELEMENT) -> Boolean
                ) =
                    PreparedEvaluation(
                        element,
                        setOf(
                            Evaluation
                                .of(
                                    error,
                                    check
                                )
                        )
                    )
            }
        }
    }
}

@ConsistentCopyVisibility
private data class Evaluation<ELEMENT : Any, ERROR : Any> private constructor(
    val error: () -> ERROR,
    val check: (ELEMENT) -> Boolean
) {
    fun evaluate(
        element: ELEMENT
    ) =
        if (this.check(element))
            PossibleError.Success.of()
        else
            PossibleError.Error.of(this.error())

    companion object Builder {

        fun <ELEMENT : Any, ERROR : Any> of(
            error: () -> ERROR,
            check: (ELEMENT) -> Boolean
        ) =
            Evaluation(
                error,
                check
            )
    }
}

private fun <ELEMENT : Any, ERROR : Any> Collection<ERROR>.toSuccessOrMultipleErrors(element: ELEMENT) =
    if (this.isEmpty())
        SuccessOrMultipleErrors.Success.of(element)
    else
        SuccessOrMultipleErrors.Error
            .of<ELEMENT, ERROR>(this.first())
            .addErrors(this.drop(1))