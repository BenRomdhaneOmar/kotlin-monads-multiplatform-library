package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Maybe

sealed interface SuccessOrSingleError<SUCCESS : Any, ERROR : Any> {

    fun success(): Boolean
    fun error() = success().not()
    fun toMaybeSuccess(): Maybe<SUCCESS>
    fun toMaybeError(): Maybe<ERROR>

    @ConsistentCopyVisibility
    data class Success<SUCCESS : Any, ERROR : Any> private constructor(
        val success: SUCCESS
    ) : SuccessOrSingleError<SUCCESS, ERROR> {

        override fun success() = true
        override fun toMaybeSuccess() = Maybe.NotEmpty.of(this.success)
        override fun toMaybeError() = Maybe.Empty.of<ERROR>()

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

        companion object Builder {

            fun <SUCCESS : Any, ERROR : Any> of(error: ERROR) = Error<SUCCESS, ERROR>(error)
        }
    }
}