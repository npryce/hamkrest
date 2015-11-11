HamKrest - Hamcrest for Kotlin
==============================

A reimplementation of Hamcrest to take advantage of Kotlin language features.

Compared to Java:

 * A decent type system. We have Kotlin to thank for that.
 * Syntactic sugar. You can negate a matcher with the ! operator and compose them with infix `and` and `or` functions.
 * Easier to extend. You can convert named functions into matchers that produced readable diagnostics and, as a
   shortcut, pass named functions to the `assertThat`, `and` and `or` functions.
