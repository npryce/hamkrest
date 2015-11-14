package com.natpryce.hamkrest.assertion

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.describe
import kotlin.reflect.KFunction1


fun <T> assertThat(actual: T, criteria: KFunction1<T,Boolean>) {
    _assertThat(null, actual, Matcher.Companion(criteria))
}

fun <T> assertThat(message: String, actual: T, criteria: KFunction1<T,Boolean>) {
    _assertThat(message, actual, Matcher.Companion(criteria))
}

fun <T> assertThat(actual: T, criteria: Matcher<T>) {
    _assertThat(null, actual, criteria)
}

fun <T> assertThat(message: String, actual: T, criteria: Matcher<T>) {
    _assertThat(message, actual, criteria)
}

private fun <T> _assertThat(message: String?, actual: T, criteria: Matcher<T>) {
    criteria(actual).let { judgement ->
        if (judgement is MatchResult.Mismatch) {
            throw AssertionError(
                    (message?.let { it + ": " } ?: "") +
                    "expected a value that ${describe(criteria)}\n" +
                    "but it ${describe(judgement)}")
        }
    }
}
