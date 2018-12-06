@file:JvmName("Assert")

package com.natpryce.hamkrest.assertion

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.describe
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

private fun noMessage() = ""

class Assertion(val valueDescriber: (Any?) -> String) {
    fun <T> that(actual: T, criteria: Matcher<T>, message: () -> String = ::noMessage) {
        val judgement = criteria(actual)
        if (judgement is MatchResult.Mismatch) {
            throw AssertionError(
                message().let { if (it.isEmpty()) it else "$it: " } +
                    "expected: a value that ${valueDescriber(criteria)}\n" +
                    "but ${valueDescriber(judgement)}")
        }
    }
}

fun <T> Assertion.that(actual: T, criteria: KFunction1<T, Boolean>, message: () -> String = ::noMessage) {
    that(actual, Matcher(criteria), message)
}

fun <T, U> Assertion.that(actual: T, criteria: KFunction2<T, U, Boolean>, other: U, message: () -> String = ::noMessage) {
    that(actual, Matcher(criteria, other), message)
}


/**
 * An Assertion that uses the Hamkrest's `describe` function to describe values.
 */
val assert = Assertion(::describe)


/**
 * Asserts that [criteria] matches [actual].
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(actual: T, criteria: Matcher<T>) {
    assert.that(actual, criteria)
}


/**
 * Asserts that [criteria] matches [actual].  On failure, the diagnostic is prefixed with [message].
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(message: String, actual: T, criteria: Matcher<T>) {
    assert.that(actual, criteria, { message })
}

/**
 * Asserts that [criteria] returns true for [actual].
 *
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(actual: T, criteria: KFunction1<T, Boolean>) {
    assert.that(actual, criteria)
}

/**
 * Asserts that [criteria] returns true for [actual].  On failure, the diagnostic is prefixed with [message].
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(message: String, actual: T, criteria: KFunction1<T, Boolean>) {
    assert.that(actual, criteria, { message })
}

/**
 * Asserts that [criteria]([actual], [other]) returns true.  On failure, the diagnostic is prefixed with [message].
 * @throws AssertionError if there is a mismatch
 */
fun <T, U> assertThat(message: String, actual: T, criteria: KFunction2<T, U, Boolean>, other: U) {
    assert.that(actual, criteria, other, { message })
}

/**
 * Asserts that [criteria]([actual], [other]) returns true.
 * @throws AssertionError if there is a mismatch
 */
fun <T, U> assertThat(actual: T, criteria: KFunction2<T, U, Boolean>, other: U) {
    assert.that(actual, criteria, other)
}

