package com.benromdhane.omar.offroadsoft.monad

import kotlin.jvm.JvmName

sealed interface Either<LEFT, RIGHT> {

    fun right(): Boolean
    fun left() = right().not()
    fun toMaybeRight(): Maybe<RIGHT>
    fun toMaybeLeft(): Maybe<LEFT>
    fun <NEW_RIGHT> mapRight(mapper: (RIGHT) -> NEW_RIGHT): Either<LEFT, NEW_RIGHT>
    fun <NEW_LEFT> mapLeft(mapper: (LEFT) -> NEW_LEFT): Either<NEW_LEFT, RIGHT>
    fun filterRight(leftAlternative: LEFT, condition: (RIGHT) -> Boolean): Either<LEFT, RIGHT>
    fun filterRight(leftAlternative: () -> LEFT, condition: (RIGHT) -> Boolean): Either<LEFT, RIGHT>
    fun filterLeft(rightAlternative: RIGHT, condition: (LEFT) -> Boolean): Either<LEFT, RIGHT>
    fun filterLeft(rightAlternative: () -> RIGHT, condition: (LEFT) -> Boolean): Either<LEFT, RIGHT>
    fun toRight(alternative: RIGHT): Either<LEFT, RIGHT>
    fun toRight(alternative: () -> RIGHT): Either<LEFT, RIGHT>
    fun toLeft(alternative: LEFT): Either<LEFT, RIGHT>
    fun toLeft(alternative: () -> LEFT): Either<LEFT, RIGHT>
    fun <NEW_RIGHT> flatMapRight(mapper: (RIGHT) -> Either<LEFT, NEW_RIGHT>): Either<LEFT, NEW_RIGHT>
    fun <NEW_LEFT> flatMapLeft(mapper: (LEFT) -> Either<NEW_LEFT, RIGHT>): Either<NEW_LEFT, RIGHT>
    fun switch(): Either<RIGHT, LEFT>
    fun <RESULT> fold(rightMapper: (RIGHT) -> RESULT, leftMapper: (LEFT) -> RESULT): RESULT

    @ConsistentCopyVisibility
    data class Right<LEFT, RIGHT> private constructor(
        private val right: RIGHT
    ) : Either<LEFT, RIGHT> {

        override fun right() = true
        override fun toMaybeRight() = Maybe.NotEmpty.of(this.right)
        override fun toMaybeLeft() = Maybe.Empty.of<LEFT>()
        override fun <NEW_RIGHT> mapRight(mapper: (RIGHT) -> NEW_RIGHT) = Right<LEFT, _>(mapper(this.right))
        override fun <NEW_LEFT> mapLeft(mapper: (LEFT) -> NEW_LEFT) = Right<NEW_LEFT, _>(this.right)
        override fun filterRight(
            leftAlternative: LEFT,
            condition: (RIGHT) -> Boolean
        ) =
            filterRight(
                { leftAlternative },
                condition
            )

        override fun filterRight(
            leftAlternative: () -> LEFT,
            condition: (RIGHT) -> Boolean
        ) =
            if (condition(this.right))
                this
            else
                Left.of(leftAlternative())

        override fun filterLeft(rightAlternative: RIGHT, condition: (LEFT) -> Boolean) = this
        override fun filterLeft(rightAlternative: () -> RIGHT, condition: (LEFT) -> Boolean) = this
        override fun toRight(alternative: RIGHT) = this
        override fun toRight(alternative: () -> RIGHT) = this
        override fun toLeft(alternative: LEFT) = toLeft { alternative }
        override fun toLeft(alternative: () -> LEFT) = Left.of<_, RIGHT>(alternative())
        override fun <NEW_RIGHT> flatMapRight(mapper: (RIGHT) -> Either<LEFT, NEW_RIGHT>) = mapper(this.right)
        override fun <NEW_LEFT> flatMapLeft(mapper: (LEFT) -> Either<NEW_LEFT, RIGHT>) = Right<NEW_LEFT, _>(this.right)
        override fun switch() = Left.of<_, LEFT>(this.right)
        override fun <RESULT> fold(rightMapper: (RIGHT) -> RESULT, leftMapper: (LEFT) -> RESULT) =
            rightMapper(this.right)

        companion object Builder {

            fun <LEFT, RIGHT> of(right: RIGHT): Either<LEFT, RIGHT> = Right(right)
        }
    }

    @ConsistentCopyVisibility
    data class Left<LEFT, RIGHT> private constructor(
        private val left: LEFT
    ) : Either<LEFT, RIGHT> {

        override fun right() = false
        override fun toMaybeRight() = Maybe.Empty.of<RIGHT>()
        override fun toMaybeLeft() = Maybe.NotEmpty.of(this.left)
        override fun <NEW_RIGHT> mapRight(mapper: (RIGHT) -> NEW_RIGHT) = Left<_, NEW_RIGHT>(this.left)
        override fun <NEW_LEFT> mapLeft(mapper: (LEFT) -> NEW_LEFT) = Left<_, RIGHT>(mapper(this.left))
        override fun filterRight(leftAlternative: LEFT, condition: (RIGHT) -> Boolean) = this
        override fun filterRight(leftAlternative: () -> LEFT, condition: (RIGHT) -> Boolean) = this
        override fun filterLeft(
            rightAlternative: RIGHT,
            condition: (LEFT) -> Boolean
        ) =
            filterLeft(
                { rightAlternative },
                condition
            )

        override fun filterLeft(
            rightAlternative: () -> RIGHT,
            condition: (LEFT) -> Boolean
        ) =
            if (condition(this.left))
                this
            else
                Right.of(rightAlternative())

        override fun toRight(alternative: RIGHT) = toRight { alternative }
        override fun toRight(alternative: () -> RIGHT) = Right.of<LEFT, _>(alternative())
        override fun toLeft(alternative: LEFT) = this
        override fun toLeft(alternative: () -> LEFT) = this
        override fun <NEW_RIGHT> flatMapRight(mapper: (RIGHT) -> Either<LEFT, NEW_RIGHT>) =
            Left<_, NEW_RIGHT>(this.left)

        override fun <NEW_LEFT> flatMapLeft(mapper: (LEFT) -> Either<NEW_LEFT, RIGHT>) = mapper(this.left)
        override fun switch() = Right.of<RIGHT, _>(this.left)
        override fun <RESULT> fold(rightMapper: (RIGHT) -> RESULT, leftMapper: (LEFT) -> RESULT) = leftMapper(this.left)

        companion object Builder {

            fun <LEFT, RIGHT> of(left: LEFT): Either<LEFT, RIGHT> = Left(left)
        }
    }
}

@JvmName("flattenRight")
fun <LEFT, RIGHT> Either<LEFT, Either<LEFT, RIGHT>>.flatten() = this.flatMapRight { it }

@JvmName("flattenLeft")
fun <LEFT, RIGHT> Either<Either<LEFT, RIGHT>, RIGHT>.flatten() = this.flatMapLeft { it }
fun <LEFT, RIGHT> Either<Either<LEFT, RIGHT>, Either<LEFT, RIGHT>>.flatten() =
    (
            if (this.right()) this.toMaybeRight()
            else this.toMaybeLeft()
            )
        .orNull()!!

fun <RESULT> Either<RESULT, RESULT>.fold() =
    (
            if (this.right()) this.toMaybeRight()
            else this.toMaybeLeft()
            )
        .orNull()!!

fun <ELEMENT, RESULT> Either<ELEMENT, ELEMENT>.fold(mapper: (ELEMENT) -> RESULT) =
    mapper(
        (
                if (this.right()) this.toMaybeRight()
                else this.toMaybeLeft()
                )
            .orNull()!!
    )

fun <LEFT, RIGHT> Either<LEFT, RIGHT>.toFilteredMaybeRight(condition: (RIGHT) -> Boolean): Maybe<RIGHT> =
    this.toMaybeRight()
        .filter(condition)

fun <LEFT, RIGHT> Either<LEFT, RIGHT>.toFilteredMaybeLeft(condition: (LEFT) -> Boolean): Maybe<LEFT> =
    this.toMaybeLeft()
        .filter(condition)

fun <ELEMENT> Either<ELEMENT, ELEMENT>.toFilteredMaybe(condition: (ELEMENT) -> Boolean) =
    (
            if (this.right()) this.toMaybeRight()
            else this.toMaybeLeft()
            )
        .filter(condition)

fun <ELEMENT> Either<ELEMENT, ELEMENT>.toMaybe() =
    if (this.right()) this.toMaybeRight()
    else this.toMaybeLeft()

fun <LEFT, ELEMENT> ELEMENT.asRightEither() = Either.Right.of<LEFT, _>(this)