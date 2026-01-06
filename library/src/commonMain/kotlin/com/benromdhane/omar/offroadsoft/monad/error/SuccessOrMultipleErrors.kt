package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Maybe

sealed interface SuccessOrMultipleErrors<SUCCESS : Any, ERROR : Any> {

    fun error(): Boolean
    fun success() = error().not()
    fun toMaybeSuccess(): Maybe<SUCCESS>
    fun toErrors(): Collection<ERROR>
    fun <NEW_SUCCESS : Any> mapSuccess(mapper: (SUCCESS) -> NEW_SUCCESS): SuccessOrMultipleErrors<NEW_SUCCESS, ERROR>
    fun addError(error: ERROR): SuccessOrMultipleErrors<SUCCESS, ERROR>
    fun addErrors(errors: Collection<ERROR>): SuccessOrMultipleErrors<SUCCESS, ERROR>
    fun addErrors(vararg errors: ERROR): SuccessOrMultipleErrors<SUCCESS, ERROR>
    fun <NEW_SUCCESS : Any> flatMapSuccess(mapper: (SUCCESS) -> SuccessOrMultipleErrors<NEW_SUCCESS, ERROR>): SuccessOrMultipleErrors<NEW_SUCCESS, ERROR>

    @ConsistentCopyVisibility
    data class Success<SUCCESS : Any, ERROR : Any> private constructor(
        private val success: SUCCESS
    ) : SuccessOrMultipleErrors<SUCCESS, ERROR> {

        override fun error() = false
        override fun toMaybeSuccess() = Maybe.NotEmpty.of(this.success)
        override fun toErrors() = emptySet<ERROR>()
        override fun <NEW_SUCCESS : Any> mapSuccess(mapper: (SUCCESS) -> NEW_SUCCESS) =
            Success<_, ERROR>(mapper(this.success))

        override fun addError(error: ERROR) = this
        override fun addErrors(errors: Collection<ERROR>) = this
        override fun addErrors(vararg errors: ERROR) = this
        override fun <NEW_SUCCESS : Any> flatMapSuccess(mapper: (SUCCESS) -> SuccessOrMultipleErrors<NEW_SUCCESS, ERROR>) =
            mapper(this.success)

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
        override fun toErrors() = this.errors
        override fun <NEW_SUCCESS : Any> mapSuccess(mapper: (SUCCESS) -> NEW_SUCCESS) =
            Error<NEW_SUCCESS, _>(this.errors)

        override fun addError(error: ERROR) = Error<SUCCESS, _>(this.errors.plus(error))
        override fun addErrors(errors: Collection<ERROR>) = Error<SUCCESS, _>(this.errors.plus(errors))
        override fun addErrors(vararg errors: ERROR) = Error<SUCCESS, _>(this.errors.plus(errors))
        override fun <NEW_SUCCESS : Any> flatMapSuccess(mapper: (SUCCESS) -> SuccessOrMultipleErrors<NEW_SUCCESS, ERROR>) =
            Error<NEW_SUCCESS, _>(this.errors)

        companion object Builder {

            fun <SUCCESS : Any, ERROR : Any> of(error: ERROR): SuccessOrMultipleErrors<SUCCESS, ERROR> =
                Error(setOf(error))
        }
    }
}