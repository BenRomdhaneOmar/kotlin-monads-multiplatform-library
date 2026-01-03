package com.benromdhane.omar.offroadsoft.monad.error

sealed interface SuccessOrSingleError<SUCCESS : Any, ERROR : Any> {

    fun success(): Boolean
    fun error() = success().not()

    @ConsistentCopyVisibility
    data class Success<SUCCESS : Any, ERROR : Any> private constructor(
        val success: SUCCESS
    ) : SuccessOrSingleError<SUCCESS, ERROR> {

        override fun success() = true

        companion object Builder {

            fun <SUCCESS : Any, ERROR : Any> of(success: SUCCESS) = Success<SUCCESS, ERROR>(success)
        }
    }

    @ConsistentCopyVisibility
    data class Error<SUCCESS : Any, ERROR : Any> private constructor(
        val error: ERROR
    ) : SuccessOrSingleError<SUCCESS, ERROR> {

        override fun success() = false

        companion object Builder {

            fun <SUCCESS : Any, ERROR : Any> of(error: ERROR) = Error<SUCCESS, ERROR>(error)
        }
    }
}