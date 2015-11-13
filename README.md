HamKrest - Hamcrest for Kotlin
==============================

A reimplementation of Hamcrest to take advantage of [Kotlin](https://kotlinlang.org/) language features.

[![Build Status](https://travis-ci.org/npryce/hamkrest.svg?branch=master)](https://travis-ci.org/npryce/hamkrest)

Compared to Java:

 * A decent type system means that developers don't have to worry about getting the variance of generic signatures right.  Variance is defined on the abstract Matcher type and Kotlin makes sure composition and subtyping work together the way you expect.
 * Syntactic sugar. You can negate a matcher with the ! operator and compose matchers with infix `and` and `or` functions:

          assertThat("xyzzy", startsWith("x") and endsWith("y") and !contains("a"))

 * Easier to extend. You can convert named functions into matchers:

          val isBlank = Matcher(String::isBlank)

          assertThat(input, isBlank)

   As a shortcut, you can pass named functions to the `assertThat`, `and` and `or` functions:

          assertThat(input, String::isBlank)

   You can use function and property references to match features of a value:

          val isLongEnough = has(String::length, greaterThan(8))

          assertThat(password, isLongEnough)

   All of these shortcuts produce good, human-readable diagnostics.
