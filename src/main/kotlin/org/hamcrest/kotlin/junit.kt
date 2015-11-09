package org.hamcrest.kotlin


public fun <T> assertThat(message: String? = null, actual: T, criteria: Matcher<T>) {
    criteria(actual).let { judgement ->
        if (judgement is MatchResult.Mismatch) {
            throw AssertionError(
                    (if (message == null) "" else message + ": ") +
                            "expected a value " + criteria.description() + "\n" +
                            "but it " + judgement.description())
        }
    }
}
