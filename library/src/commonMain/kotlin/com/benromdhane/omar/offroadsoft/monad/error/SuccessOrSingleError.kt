package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Maybe

sealed interface SuccessOrSingleError<SUCCESS : Any, ERROR : Any> {

    fun success(): Boolean
    fun error() = success().not()
    fun toMaybeSuccess(): Maybe<SUCCESS>
    fun toMaybeError(): Maybe<ERROR>
    fun <NEW_SUCCESS : Any> mapSuccess(mapper: (SUCCESS) -> NEW_SUCCESS): SuccessOrSingleError<NEW_SUCCESS, ERROR>
    fun <NEW_ERROR : Any> mapError(mapper: (ERROR) -> NEW_ERROR): SuccessOrSingleError<SUCCESS, NEW_ERROR>
    fun <NEW_SUCCESS : Any> flatMapSuccess(mapper: (SUCCESS) -> SuccessOrSingleError<NEW_SUCCESS, ERROR>): SuccessOrSingleError<NEW_SUCCESS, ERROR>
    fun filterSuccess(alternativeError: ERROR, condition: (SUCCESS) -> Boolean): SuccessOrSingleError<SUCCESS, ERROR>
    fun filterSuccess(
        alternativeError: () -> ERROR,
        condition: (SUCCESS) -> Boolean
    ): SuccessOrSingleError<SUCCESS, ERROR>

    fun filterSuccessNot(alternativeError: ERROR, condition: (SUCCESS) -> Boolean): SuccessOrSingleError<SUCCESS, ERROR>

    fun filterSuccessNot(
        alternativeError: () -> ERROR,
        condition: (SUCCESS) -> Boolean
    ): SuccessOrSingleError<SUCCESS, ERROR>

    fun toSuccess(alternativeSuccess: SUCCESS): SuccessOrSingleError<SUCCESS, ERROR>

    @ConsistentCopyVisibility
    data class Success<SUCCESS : Any, ERROR : Any> private constructor(
        val success: SUCCESS
    ) : SuccessOrSingleError<SUCCESS, ERROR> {

        override fun success() = true
        override fun toMaybeSuccess() = Maybe.NotEmpty.of(this.success)
        override fun toMaybeError() = Maybe.Empty.of<ERROR>()
        override fun <NEW_SUCCESS : Any> mapSuccess(mapper: (SUCCESS) -> NEW_SUCCESS) =
            Success<_, ERROR>(mapper(this.success))

        override fun <NEW_ERROR : Any> mapError(mapper: (ERROR) -> NEW_ERROR) = Success<_, NEW_ERROR>(this.success)
        override fun <NEW_SUCCESS : Any> flatMapSuccess(mapper: (SUCCESS) -> SuccessOrSingleError<NEW_SUCCESS, ERROR>) =
            mapper(this.success)

        override fun filterSuccess(
            alternativeError: ERROR,
            condition: (SUCCESS) -> Boolean
        ) =
            filterSuccess(
                { alternativeError },
                condition
            )

        override fun filterSuccess(
            alternativeError: () -> ERROR,
            condition: (SUCCESS) -> Boolean
        ) =
            if (condition(this.success))
                this
            else
                Error.of(alternativeError())

        override fun filterSuccessNot(
            alternativeError: ERROR,
            condition: (SUCCESS) -> Boolean
        ) =
            filterSuccessNot(
                { alternativeError },
                condition
            )

        override fun filterSuccessNot(
            alternativeError: () -> ERROR,
            condition: (SUCCESS) -> Boolean
        ) =
            if (condition(this.success).not())
                this
            else
                Error.of(alternativeError())

        override fun toSuccess(alternativeSuccess: SUCCESS) = this

        companion object Builder {

            fun <SUCCESS : Any, ERROR : Any> of(success: SUCCESS) = Success<SUCCESS, ERROR>(success)
        }
    }

    @ConsistentCopyVisibility
    data class Error<SUCCESS : Any, ERROR : Any> private constructor(
        val error: ERROR
    ) : SuccessOrSingleError<SUCCESS, ERROR> {

        override fun success() = false
        override fun toMaybeSuccess() = Maybe.Empty.of<SUCCESS>()
        override fun toMaybeError() = Maybe.NotEmpty.of(this.error)
        override fun <NEW_SUCCESS : Any> mapSuccess(mapper: (SUCCESS) -> NEW_SUCCESS) =
            Error<NEW_SUCCESS, _>(this.error)

        override fun <NEW_ERROR : Any> mapError(mapper: (ERROR) -> NEW_ERROR) = Error<SUCCESS, _>(mapper(this.error))
        override fun <NEW_SUCCESS : Any> flatMapSuccess(mapper: (SUCCESS) -> SuccessOrSingleError<NEW_SUCCESS, ERROR>) =
            Error<NEW_SUCCESS, ERROR>(this.error)

        override fun filterSuccess(alternativeError: ERROR, condition: (SUCCESS) -> Boolean) = this
        override fun filterSuccess(alternativeError: () -> ERROR, condition: (SUCCESS) -> Boolean) = this
        override fun filterSuccessNot(alternativeError: ERROR, condition: (SUCCESS) -> Boolean) = this
        override fun filterSuccessNot(alternativeError: () -> ERROR, condition: (SUCCESS) -> Boolean) = this
        override fun toSuccess(alternativeSuccess: SUCCESS) = Success.of<_, ERROR>(alternativeSuccess)

        companion object Builder {

            fun <SUCCESS : Any, ERROR : Any> of(error: ERROR) = Error<SUCCESS, ERROR>(error)
        }
    }
}