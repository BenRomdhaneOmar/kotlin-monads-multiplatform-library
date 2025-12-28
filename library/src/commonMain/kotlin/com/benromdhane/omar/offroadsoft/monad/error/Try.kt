package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Maybe

sealed interface Try<SUCCESS> {

    fun success(): Boolean
    fun failure() = success().not()
    fun toMaybeSuccess(): Maybe<SUCCESS>
    fun toMaybeFailure(): Maybe<Throwable>
    fun <NEW_SUCCESS> mapSuccess(mapper: (SUCCESS) -> NEW_SUCCESS): Try<NEW_SUCCESS>
    fun mapFailure(mapper: (Throwable) -> Throwable): Try<SUCCESS>
    fun recover(alternative: SUCCESS): Try<SUCCESS>
    fun recover(alternative: () -> SUCCESS): Try<SUCCESS>
    fun recover(alternative: SUCCESS, condition: (Throwable) -> Boolean): Try<SUCCESS>

    companion object Of {

        fun <SUCCESS> seed(seed: SUCCESS) = Success.of(seed)
        fun <SUCCESS> seed(seed: Throwable) = Failure.of<SUCCESS>(seed)
        fun <SUCCESS> trying(provider: () -> SUCCESS) =
            try {
                Success.of(provider())
            } catch (throwable: Throwable) {
                Failure.of(throwable)
            }
    }

    @ConsistentCopyVisibility
    private data class Success<SUCCESS> private constructor(
        private val success: SUCCESS
    ) : Try<SUCCESS> {

        override fun success() = true
        override fun toMaybeSuccess() = Maybe.NotEmpty.of(this.success)
        override fun toMaybeFailure() = Maybe.Empty.of<Throwable>()
        override fun <NEW_SUCCESS> mapSuccess(mapper: (SUCCESS) -> NEW_SUCCESS) =
            try {
                Success(
                    mapper(this.success)
                )
            } catch (throwable: Throwable) {
                Failure.of(throwable)
            }

        override fun mapFailure(mapper: (Throwable) -> Throwable) = this
        override fun recover(alternative: SUCCESS) = this
        override fun recover(alternative: () -> SUCCESS) = this
        override fun recover(alternative: SUCCESS, condition: (Throwable) -> Boolean) = this

        companion object Builder {

            fun <SUCCESS> of(
                success: SUCCESS
            ): Try<SUCCESS> =
                Success(
                    success
                )
        }
    }

    @ConsistentCopyVisibility
    private data class Failure<SUCCESS> private constructor(
        private val failure: Throwable
    ) : Try<SUCCESS> {

        override fun success() = false
        override fun toMaybeSuccess() = Maybe.Empty.of<SUCCESS>()
        override fun toMaybeFailure() = Maybe.NotEmpty.of(this.failure)
        override fun <NEW_SUCCESS> mapSuccess(mapper: (SUCCESS) -> NEW_SUCCESS) = Failure<NEW_SUCCESS>(this.failure)
        override fun mapFailure(mapper: (Throwable) -> Throwable) = Failure<SUCCESS>(mapper(this.failure))
        override fun recover(alternative: SUCCESS) = recover { alternative }
        override fun recover(alternative: () -> SUCCESS) = Success.of(alternative())
        override fun recover(
            alternative: SUCCESS,
            condition: (Throwable) -> Boolean
        ) =
            if (condition(this.failure))
                Success.of(alternative)
            else
                this

        companion object Builder {

            fun <SUCCESS> of(
                failure: Throwable
            ): Try<SUCCESS> =
                Failure(
                    failure
                )
        }
    }
}
