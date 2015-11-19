HamKrest - Hamcrest for Kotlin
==============================

A reimplementation of Hamcrest to take advantage of [Kotlin](https://kotlinlang.org/) language features.

[![Build Status](https://travis-ci.org/npryce/hamkrest.svg?branch=master)](https://travis-ci.org/npryce/hamkrest)
[![Maven Central](https://img.shields.io/maven-central/v/com.natpryce/hamkrest.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.natpryce%22%20AND%20a%3A%22hamkrest%22)

Compared to Java:

 * A decent type system means that developers don't have to worry about getting the variance of generic signatures right.  Variance is defined on the abstract Matcher type and Kotlin makes sure composition and subtyping work together the way you expect.
 * Syntactic sugar. You can negate a matcher with the ! operator and compose matchers with infix `and` and `or` functions:

          assertThat("xyzzy", startsWith("x") and endsWith("y") and !contains("a"))

 * Easier to extend. You can convert named unary predicates into matchers.

          val isBlank = Matcher(String::isBlank)

          assertThat(input, isBlank)

   As a shortcut, you can pass named functions to the `assertThat`, `and`, `or` and many other functions that take a matcher.

          assertThat(input, String::isBlank)

   You can also convert a named binary predicate and the second argument to a matcher for first argument, which works well for extension methods.

          fun String.hasLength(n: Int): Boolean = this.length == n

          val isTheRightLength = Matcher(String::hasLength, 8)

          assertThat(secretCode, isTheRightLength)

   You can use function and property references to match features of a value:

          val isLongEnough = has(String::length, greaterThan(8))

          assertThat(password, isLongEnough)

   If you like sugar (or don't like punctuation), you can use

          password shouldMatch isLongEnough
          password shouldMatch ::myPasswordFunction

   All of these shortcuts produce good, human-readable diagnostics.
