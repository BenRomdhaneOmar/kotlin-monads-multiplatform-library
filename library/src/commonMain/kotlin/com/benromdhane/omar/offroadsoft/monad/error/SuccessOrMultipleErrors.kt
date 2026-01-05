package com.benromdhane.omar.offroadsoft.monad.error

sealed interface SuccessOrMultipleErrors<SUCCESS : Any, ERROR : Any> {

    fun error(): Boolean

    @ConsistentCopyVisibility
    data class Success<SUCCESS : Any, ERROR : Any> private constructor(
        private val success: SUCCESS
    ) : SuccessOrMultipleErrors<SUCCESS, ERROR> {

        override fun error() = false

        companion object Builder {

            fun <SUCCESS : Any, ERROR : Any> of(success: SUCCESS): SuccessOrMultipleErrors<SUCCESS, ERROR> =
                Success(success)
        }
    }

    @ConsistentCopyVisibility
    data class Error<SUCCESS : Any, ERROR : Any> private constructor(
        private val errors: Set<ERROR>
    ) : SuccessOrMultipleErrors<SUCCESS, ERROR> {

        override fun error() = true

        companion object Builder {

            fun <SUCCESS : Any, ERROR : Any> of(error: ERROR): SuccessOrMultipleErrors<SUCCESS, ERROR> =
                Error(setOf(error))
        }
    }
}