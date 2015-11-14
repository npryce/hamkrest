package com.natpryce.hamkrest

import kotlin.reflect.KFunction1


fun <T> assertThat(actual: T, criteria: KFunction1<T,Boolean>) {
    _assertThat(null, actual, Matcher(criteria))
}

fun <T> assertThat(message: String, actual: T, criteria: KFunction1<T,Boolean>) {
    _assertThat(message, actual, Matcher(criteria))
}

fun <T> assertThat(actual: T, criteria: Matcher<T>) {
    _assertThat(null, actual, criteria)
}

fun <T> assertThat(message: String, actual: T, criteria: Matcher<T>) {
    _assertThat(message, actual, criteria)
}

inline fun <reified T> T.shouldMatch(matcher: Matcher<T>) {
    assertThat(this, matcher)
}

inline fun <reified T> T.shouldMatch(f: KFunction1<T, Boolean>) {
    this.shouldMatch(Matcher(f))
}

private fun <T> _assertThat(message: String?, actual: T, criteria: Matcher<T>) {
    criteria(actual).let { judgement ->
        if (judgement is MatchResult.Mismatch) {
            throw AssertionError(
                    (message?.let { it + ": " } ?: "") +
                    "expected a value that " + criteria.description() + "\n" +
                    "but it " + judgement.description())
        }
    }
}
