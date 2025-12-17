package com.benromdhane.omar.offroadsoft.monad

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

        override fun toRight(alternative: RIGHT) = Right.of<LEFT, _>(alternative)

        companion object Builder {

            fun <LEFT, RIGHT> of(left: LEFT): Either<LEFT, RIGHT> = Left(left)
        }
    }
}