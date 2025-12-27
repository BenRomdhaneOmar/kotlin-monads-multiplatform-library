package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Maybe

sealed interface Try<SUCCESS> {

    fun success(): Boolean
    fun failure() = success().not()
    fun toMaybeSuccess(): Maybe<SUCCESS>

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
