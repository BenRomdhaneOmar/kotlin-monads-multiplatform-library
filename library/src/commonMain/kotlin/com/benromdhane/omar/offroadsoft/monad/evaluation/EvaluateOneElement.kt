package com.benromdhane.omar.offroadsoft.monad.evaluation

import com.benromdhane.omar.offroadsoft.monad.error.PossibleError
import com.benromdhane.omar.offroadsoft.monad.error.SuccessOrMultipleErrors

class EvaluateOneElement<ELEMENT : Any, ERROR : Any> private constructor(
    private val element: ELEMENT
) {

    fun addEvaluation(
        error: () -> ERROR,
        check: (ELEMENT) -> Boolean
    ) =
        PreparedEvaluation
            .of(
                this.element,
                Evaluation
                    .of(
                        error,
                        check
                    )
            )

    companion object {

        fun <ELEMENT : Any, ERROR : Any> element(
            element: ELEMENT
        ) =
            EvaluateOneElement<_, ERROR>(
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
            evaluate(
                this.evaluations,
                this.element
            )

        internal companion object Builder {

            internal fun <ELEMENT : Any, ERROR : Any> of(
                element: ELEMENT,
                evaluation: Evaluation<ELEMENT, ERROR>
            ) =
                PreparedEvaluation(
                    element,
                    setOf(
                        evaluation
                    )
                )
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
    evaluations: Set<EvaluateOneElement.Evaluation<ELEMENT, ERROR>>,
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