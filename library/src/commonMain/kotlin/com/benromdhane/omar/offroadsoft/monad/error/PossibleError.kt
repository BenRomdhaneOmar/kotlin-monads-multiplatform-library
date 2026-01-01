package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Either

sealed interface PossibleError<ERROR : Any> {

    fun error(): Boolean

    companion object Builder {

        fun of(`try`: Try<Unit>) =
            `try`.toMaybeFailure()
                .map { Error.of<Throwable>(it) }
                .or(Success.of())

        fun <ERROR : Any> of(either: Either<ERROR, Unit>) =
            either.toMaybeLeft()
                .map { Error.of<ERROR>(it) }
                .or(Success.of())
    }

    @ConsistentCopyVisibility
    private data class Error<ERROR : Any> private constructor(
        val error: ERROR
    ) : PossibleError<ERROR> {

        override fun error() = true

        companion object Builder {

            fun <ERROR : Any> of(error: ERROR): PossibleError<ERROR> = Error(error)
        }
    }

    private class Success<ERROR : Any> private constructor() : PossibleError<ERROR> {

        override fun error() = false
        override fun toString() = "Success()"
        override fun equals(other: Any?) =
            if (this === other) true
            else if (other == null || this::class != other::class) false
            else true

        override fun hashCode() = this::class.hashCode()

        companion object Builder {

            fun <ERROR : Any> of(): PossibleError<ERROR> = Success()
        }
    }
}

fun Try<Unit>.asPossibleError() = PossibleError.of(this)