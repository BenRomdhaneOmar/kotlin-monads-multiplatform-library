package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Maybe

sealed interface SuccessOrMultipleErrors<SUCCESS : Any, ERROR : Any> {

    fun error(): Boolean
    fun success() = error().not()
    fun toMaybeSuccess(): Maybe<SUCCESS>

    @ConsistentCopyVisibility
    data class Success<SUCCESS : Any, ERROR : Any> private constructor(
        private val success: SUCCESS
    ) : SuccessOrMultipleErrors<SUCCESS, ERROR> {

        override fun error() = false
        override fun toMaybeSuccess() = Maybe.NotEmpty.of(this.success)

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
        override fun toMaybeSuccess() = Maybe.Empty.of<SUCCESS>()

        companion object Builder {

            fun <SUCCESS : Any, ERROR : Any> of(error: ERROR): SuccessOrMultipleErrors<SUCCESS, ERROR> =
                Error(setOf(error))
        }
    }
}