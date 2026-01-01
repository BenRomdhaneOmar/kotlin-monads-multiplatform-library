package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Either
import com.benromdhane.omar.offroadsoft.monad.Maybe
import kotlin.jvm.JvmName

sealed interface PossibleError<ERROR : Any> {

    fun error(): Boolean
    fun toMaybeError(): Maybe<ERROR>
    fun <NEW_ERROR : Any> map(mapper: (ERROR) -> NEW_ERROR): PossibleError<NEW_ERROR>

    companion object Builder {

        fun of(`try`: Try<Unit>) =
            `try`.toMaybeFailure()
                .map { Error.of<Throwable>(it) }
                .or(Success.of())

        @JvmName("ofEitherLeftError")
        fun <ERROR : Any> of(either: Either<ERROR, Unit>) =
            either.toMaybeLeft()
                .map { Error.of<ERROR>(it) }
                .or(Success.of())

        @JvmName("ofEitherRightError")
        fun <ERROR : Any> of(either: Either<Unit, ERROR>) =
            either.toMaybeRight()
                .map { Error.of<ERROR>(it) }
                .or(Success.of())

        fun <ERROR : Any> of(maybe: Maybe<ERROR>) =
            maybe
                .map { Error.of<ERROR>(it) }
                .or(Success.of())
    }

    @ConsistentCopyVisibility
    private data class Error<ERROR : Any> private constructor(
        val error: ERROR
    ) : PossibleError<ERROR> {

        override fun error() = true
        override fun toMaybeError() = Maybe.NotEmpty.of(this.error)
        override fun <NEW_ERROR : Any> map(mapper: (ERROR) -> NEW_ERROR) = Error(mapper(this.error))

        companion object Builder {

            fun <ERROR : Any> of(error: ERROR): PossibleError<ERROR> = Error(error)
        }
    }

    private class Success<ERROR : Any> private constructor() : PossibleError<ERROR> {

        override fun error() = false
        override fun toMaybeError() = Maybe.Empty.of<ERROR>()
        override fun <NEW_ERROR : Any> map(mapper: (ERROR) -> NEW_ERROR) = Success<NEW_ERROR>()

        override fun toString() = "Success"
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

@JvmName("eitherLeftErrorAsPossibleError")
fun <ERROR : Any> Either<ERROR, Unit>.asPossibleError() = PossibleError.of(this)

@JvmName("eitherRightErrorAsPossibleError")
fun <ERROR : Any> Either<Unit, ERROR>.asPossibleError() = PossibleError.of(this)

fun <ERROR : Any> Maybe<ERROR>.asPossibleError() = PossibleError.of(this)
