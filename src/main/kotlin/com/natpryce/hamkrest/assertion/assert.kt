@file:JvmName("Assert")

package com.natpryce.hamkrest.assertion

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.describe
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

/**
 * Asserts that [criteria] matches [actual].
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(actual: T, criteria: Matcher<T>, describer: (Any?) -> String = ::describe) {
    _assertThat(null, actual, criteria, describer)
}

/**
 * Asserts that [criteria] matches [actual].  On failure, the diagnostic is prefixed with [message].
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(message: String, actual: T, criteria: Matcher<T>, describer: (Any?) -> String = ::describe) {
    _assertThat(message, actual, criteria, describer)
}

/**
 * Asserts that [criteria] returns true for [actual].
 *
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(actual: T, criteria: KFunction1<T,Boolean>, describer: (Any?) -> String = ::describe) {
    _assertThat(null, actual, Matcher(criteria), describer)
}

/**
 * Asserts that [criteria] returns true for [actual].  On failure, the diagnostic is prefixed with [message].
 * @throws AssertionError if there is a mismatch
 */
fun <T> assertThat(message: String, actual: T, criteria: KFunction1<T,Boolean>, describer: (Any?) -> String = ::describe) {
    _assertThat(message, actual, Matcher(criteria), describer)
}

/**
 * Asserts that [criteria]([actual], [other]) returns true.  On failure, the diagnostic is prefixed with [message].
 * @throws AssertionError if there is a mismatch
 */
fun <T,U> assertThat(message: String, actual: T, criteria: KFunction2<T,U,Boolean>, other: U, describer: (Any?) -> String = ::describe) {
    _assertThat(message, actual, Matcher(criteria, other), describer)
}

/**
 * Asserts that [criteria]([actual], [other]) returns true.
 * @throws AssertionError if there is a mismatch
 */
fun <T,U> assertThat(actual: T, criteria: KFunction2<T,U,Boolean>, other: U, describer: (Any?) -> String = ::describe) {
    _assertThat(null, actual, Matcher(criteria, other), describer)
}

private fun <T> _assertThat(message: String?, actual: T, criteria: Matcher<T>, describe: (Any?) -> String) {
    criteria(actual).let { judgement ->
        if (judgement is MatchResult.Mismatch) {
            throw AssertionError(
                (message?.let { it + ": " } ?: "") +
                    "expected a value that ${describe(criteria)}\n" +
                    "but it ${describe(judgement)}")
        }
    }
}
