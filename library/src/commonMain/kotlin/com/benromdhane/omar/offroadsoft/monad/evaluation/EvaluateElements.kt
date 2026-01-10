package com.benromdhane.omar.offroadsoft.monad.evaluation

import com.benromdhane.omar.offroadsoft.monad.error.PossibleError
import com.benromdhane.omar.offroadsoft.monad.error.SuccessOrMultipleErrors

@ConsistentCopyVisibility
data class EvaluateOneElement<ELEMENT : Any, ERROR : Any> private constructor(
    private val element: ELEMENT,
    private val evaluations: Set<Evaluation<ELEMENT, ERROR>> = emptySet()
) {

    fun addEvaluation(
        error: () -> ERROR,
        check: (ELEMENT) -> Boolean
    ) =
        EvaluateOneElement(
            this.element,
            this.evaluations
                .plus(
                    Evaluation.of(
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

    companion object Builder {

        fun <ELEMENT : Any, ERROR : Any> instance(
            element: ELEMENT
        ) =
            EvaluateOneElement<_, ERROR>(
                element
            )

        fun <ELEMENT : Any, ERROR : Any> instance(
            element: () -> ELEMENT
        ) =
            EvaluateOneElement<_, ERROR>(
                element()
            )
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