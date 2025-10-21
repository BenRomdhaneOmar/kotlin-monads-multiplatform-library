package com.benromdhane.omar.offroadsoft.monad

sealed interface Either<LEFT, RIGHT> {

    fun right(): Boolean
    fun left() = right().not()

    @ConsistentCopyVisibility
    data class Right<LEFT, RIGHT> private constructor(
        private val right: RIGHT
    ) : Either<LEFT, RIGHT> {

        override fun right() = true

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

        companion object Builder {

            fun <LEFT, RIGHT> of(left: LEFT) =
                Left<LEFT, RIGHT>(
                    left
                )
        }
    }
}