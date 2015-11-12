HamKrest - Hamcrest for Kotlin
==============================

A reimplementation of Hamcrest to take advantage of Kotlin language features.

Compared to Java:

 * A decent type system means that developers don't have to worry about getting the variance of generic signatures right.  Variance is defined on the abstract Matcher type and Kotlin makes sure composition and subtyping work together the way you expect.
 * Syntactic sugar. You can negate a matcher with the ! operator and compose matchers with infix `and` and `or` functions.
   For example:
   
          assertThat("xyzzy", startsWith("x") and endsWith("y") and !contains("a"))

 * Easier to extend. You can convert named functions into matchers that produce readable diagnostics and, as a shortcut, pass named functions to the `assertThat`, `and` and `or` functions.



