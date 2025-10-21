package com.benromdhane.omar.offroadsoft.monad

sealed interface Either<LEFT, RIGHT> {

    fun right(): Boolean
    fun left() = right().not()
    fun toMaybeRight(): Maybe<RIGHT>
    fun toMaybeLeft(): Maybe<LEFT>
    fun <NEW_RIGHT> mapRight(mapper: (RIGHT) -> NEW_RIGHT): Either<LEFT, NEW_RIGHT>

    @ConsistentCopyVisibility
    data class Right<LEFT, RIGHT> private constructor(
        private val right: RIGHT
    ) : Either<LEFT, RIGHT> {

        override fun right() = true
        override fun toMaybeRight() = Maybe.NotEmpty.of(this.right)
        override fun toMaybeLeft() = Maybe.Empty.of<LEFT>()
        override fun <NEW_RIGHT> mapRight(mapper: (RIGHT) -> NEW_RIGHT) = Right<LEFT, _>(mapper(this.right))

        companion object Builder {

            fun <LEFT, RIGHT> of(right: RIGHT) =
                Right<LEFT, RIGHT>(
                    right
                )
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

        companion object Builder {

            fun <LEFT, RIGHT> of(left: LEFT) =
                Left<LEFT, RIGHT>(
                    left
                )
        }
    }
}