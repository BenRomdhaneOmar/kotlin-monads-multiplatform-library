package com.benromdhane.omar.offroadsoft.monad.error

sealed interface Try<SUCCESS, EXCEPTION : Throwable> {

    fun success(): Boolean
    fun failure() = success().not()

    companion object Of {

        fun <SUCCESS> seed(seed: SUCCESS) = Success.of<SUCCESS, Throwable>(seed)
        fun <SUCCESS> seed(seed: Throwable) = Failure.of<SUCCESS, Throwable>(seed)
        fun <SUCCESS> trying(provider: () -> SUCCESS): Try<SUCCESS, Throwable> =
            try {
                Success.of(provider())
            } catch (throwable: Throwable) {
                Failure.of(throwable)
            }
    }

    @ConsistentCopyVisibility
    private data class Success<SUCCESS, EXCEPTION : Throwable> private constructor(
        private val success: SUCCESS
    ) : Try<SUCCESS, EXCEPTION> {

        override fun success() = true

        companion object Builder {

            fun <SUCCESS, EXCEPTION : Throwable> of(
                success: SUCCESS
            ): Try<SUCCESS, EXCEPTION> =
                Success(
                    success
                )
        }
    }

    @ConsistentCopyVisibility
    private data class Failure<SUCCESS, EXCEPTION : Throwable> private constructor(
        private val failure: EXCEPTION
    ) : Try<SUCCESS, EXCEPTION> {

        override fun success() = false

        companion object Builder {

            fun <SUCCESS, EXCEPTION : Throwable> of(
                failure: EXCEPTION
            ): Try<SUCCESS, EXCEPTION> =
                Failure(
                    failure
                )
        }
    }
}
