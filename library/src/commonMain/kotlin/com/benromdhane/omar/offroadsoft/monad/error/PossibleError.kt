package com.benromdhane.omar.offroadsoft.monad.error

import com.benromdhane.omar.offroadsoft.monad.Either
import com.benromdhane.omar.offroadsoft.monad.Maybe
import com.benromdhane.omar.offroadsoft.monad.error.PossibleError.Success
import kotlin.jvm.JvmName

sealed interface PossibleError<ERROR : Any> {

    fun error(): Boolean
    fun toMaybeError(): Maybe<ERROR>
    fun <NEW_ERROR : Any> map(mapper: (ERROR) -> NEW_ERROR): PossibleError<NEW_ERROR>

    @ConsistentCopyVisibility
    data class Error<ERROR : Any> private constructor(
        private val error: ERROR
    ) : PossibleError<ERROR> {

        override fun error() = true
        override fun toMaybeError() = Maybe.NotEmpty.of(this.error)
        override fun <NEW_ERROR : Any> map(mapper: (ERROR) -> NEW_ERROR) = Error(mapper(this.error))

        companion object Builder {

            fun <ERROR : Any> of(error: ERROR): PossibleError<ERROR> = Error(error)
        }
    }

    class Success<ERROR : Any> private constructor() : PossibleError<ERROR> {

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

fun Try<Unit>.asPossibleError() =
    this
        .toMaybeFailure()
        .map { PossibleError.Error.of(it) }
        .or { Success.of() }

@JvmName("eitherLeftErrorAsPossibleError")
fun <ERROR : Any> Either<ERROR, Unit>.asPossibleError() =
    this
        .toMaybeLeft()
        .map { PossibleError.Error.of(it) }
        .or { Success.of() }

@JvmName("eitherRightErrorAsPossibleError")
fun <ERROR : Any> Either<Unit, ERROR>.asPossibleError() =
    this
        .toMaybeRight()
        .map { PossibleError.Error.of(it) }
        .or { Success.of() }

fun <ERROR : Any> Maybe<ERROR>.asPossibleError() =
    this
        .map { PossibleError.Error.of(it) }
        .or { Success.of() }
