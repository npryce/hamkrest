package org.hamcrest.kotlin


public fun <T> assertThat(actual: T, criteria: Matcher<T>) {
    _assertThat(null, actual, criteria)
}

public fun <T> assertThat(message: String, actual: T, criteria: Matcher<T>) {
    _assertThat(message, actual, criteria)
}

private fun <T> _assertThat(message: String?, actual: T, criteria: Matcher<T>) {
    criteria(actual).let { judgement ->
        if (judgement is MatchResult.Mismatch) {
            throw AssertionError(
                    (message?.let { it + ": " } ?: "") +
                    "expected a value " + criteria.description() + "\n" +
                    "but it " + judgement.description())
        }
    }
}
