package com.benromdhane.omar.offroadsoft.monad.evaluate

import com.benromdhane.omar.offroadsoft.monad.Maybe
import com.benromdhane.omar.offroadsoft.monad.asMaybe
import com.benromdhane.omar.offroadsoft.monad.error.PossibleError
import com.benromdhane.omar.offroadsoft.monad.error.SuccessOrMultipleErrors

@ConsistentCopyVisibility
data class EvaluateOneElement<ELEMENT : Any, ERROR : Any> private constructor(
    val element: ELEMENT,
    private val evaluations: Set<Evaluation<ELEMENT, ERROR>> = emptySet()
) {

    fun addEvaluation(
        check: (ELEMENT) -> Boolean,
        error: () -> ERROR
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
            .toSuccessOrMultipleErrors()
            .or { SuccessOrMultipleErrors.Success.of(this.element) }

    private fun Collection<ERROR>.toSuccessOrMultipleErrors() =
        if (this.isEmpty())
            Maybe.Empty.of()
        else
            SuccessOrMultipleErrors.Error
                .of<ELEMENT, ERROR>(this.first())
                .addErrors(this.drop(1))
                .asMaybe()

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

    @ConsistentCopyVisibility
    private data class Evaluation<ELEMENT : Any, ERROR : Any> private constructor(
        val error: () -> ERROR,
        val check: (ELEMENT) -> Boolean = { true }
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
                check: (ELEMENT) -> Boolean = { true }
            ) =
                Evaluation(
                    error,
                    check
                )
        }
    }
}