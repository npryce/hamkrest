package com.natpryce.hamkrest.assertion

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.Mismatch
import com.natpryce.hamkrest.describe
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

private fun noMessage() = ""

/**
 * Asserts that [criteria] matches [actual].
 * On failure, the diagnostic is prefixed with the result of calling [message].
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(actual: T, criteria: Matcher<T>, message: () -> String = ::noMessage) {
    val judgement = criteria(actual)
    if (judgement is Mismatch) {
        throw AssertionError(
            message().let { if (it.isEmpty()) it else "$it: " } +
                "expected: a value that ${describe(criteria)}\n" +
                "but ${describe(judgement)}")
    }
}

/**
 * Asserts that [criteria] matches [actual].
 * On failure, the diagnostic is prefixed with the result of calling [message].
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(actual: T, criteria: KFunction1<T, Boolean>, message: () -> String = ::noMessage) {
    assertThat(actual, Matcher(criteria), message)
}

/**
 * Asserts that [criteria]([actual], [other]) returns true.
 * On failure, the diagnostic is prefixed with the result of calling [message].
 * @throws AssertionError if there is a mismatch
 */

fun <T, U> assertThat(actual: T, criteria: KFunction2<T, U, Boolean>, other: U, message: () -> String = ::noMessage) {
    assertThat(actual, Matcher(criteria, other), message)
}


/**
 * Asserts that [criteria] matches [actual].  On failure, the diagnostic is prefixed with [message].
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(message: String, actual: T, criteria: Matcher<T>) {
    assertThat(actual, criteria, { message })
}

/**
 * Asserts that [criteria] returns true for [actual].  On failure, the diagnostic is prefixed with [message].
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(message: String, actual: T, criteria: KFunction1<T, Boolean>) {
    assertThat(actual, criteria, { message })
}

/**
 * Asserts that [criteria]([actual], [other]) returns true.  On failure, the diagnostic is prefixed with [message].
 * @throws AssertionError if there is a mismatch
 */
fun <T, U> assertThat(message: String, actual: T, criteria: KFunction2<T, U, Boolean>, other: U) {
    assertThat(actual, criteria, other, { message })
}

