package com.benromdhane.omar.offroadsoft.monad.error

sealed interface Try<SUCCESS, EXCEPTION : Throwable> {

    fun success(): Boolean

    companion object Of {

        fun <SUCCESS, EXCEPTION : Throwable> seed(seed: SUCCESS) = Success.of<SUCCESS, EXCEPTION>(seed)
        fun <SUCCESS, EXCEPTION : Throwable> seed(seed: EXCEPTION) = Failure.of<SUCCESS, EXCEPTION>(seed)
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
