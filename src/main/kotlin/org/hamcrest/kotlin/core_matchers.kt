package org.hamcrest.kotlin

import org.hamcrest.kotlin.internal.match


public fun <T> equalTo(expected: T): Matcher<T> =
        object : Matcher.Primitive<T>() {
            override fun invoke(actual: T): MatchResult = match(actual == expected) { "was ${delimit(actual)}" }

            override fun description(): String {
                return "equal to ${delimit(expected)}"
            }
        }


public fun <T> absent(): Matcher<T?> = object : Matcher.Primitive<T?>() {
    override fun invoke(p1: T?): MatchResult = match(p1 == null) { "was ${delimit(p1)}" }

    override fun description(): String = "null"
}


public fun <T> present(valueMatcher : Matcher<T>) : Matcher<T?> =
        object : Matcher.Primitive<T?>() {
            override fun invoke(actual: T?): MatchResult {
                return when(actual) {
                    null -> MatchResult.Mismatch("was null")
                    else -> valueMatcher(actual)
                }
            }

            override fun description(): String {
                return "not null & " + valueMatcher.description()
            }
        }

public inline fun <reified T : Any> cast(downcastMatcher: Matcher<T> ) : Matcher<Any> {
    return object : Matcher.Primitive<Any>() {
        override fun invoke(actual: Any): MatchResult {
            return when(actual) {
                is T -> {
                    downcastMatcher(actual)
                }
                else -> {
                    MatchResult.Mismatch("was a " + actual.javaClass.kotlin.qualifiedName)
                }
            }
        }

        override fun description(): String {
            return "of type " + T::class.qualifiedName + " & " + downcastMatcher.description()
        }
    }
}

public fun <N :Comparable<N>> greaterThan(n:N) = _comparesAs("greater than", n) {it > 0}
public fun <N :Comparable<N>> greaterThanOrEqualTo(n:N) = _comparesAs("greater than or equal to", n) {it >= 0}
public fun <N :Comparable<N>> lessThan(n:N) = _comparesAs("less than", n) {it < 0}
public fun <N :Comparable<N>> lessThanOrEqualTo(n:N) = _comparesAs("less than or equal to", n) {it <= 0}

private fun <N :Comparable<N>> _comparesAs(description: String, n: N, expectedSignum: (Int) -> Boolean) : Matcher<N> {
    return object : Matcher.Primitive<N>() {
        override fun invoke(actual: N): MatchResult =
                match(expectedSignum(actual.compareTo(n))) {"was ${delimit(actual)}"}

        override fun description(): String {
            return "${description} ${delimit(n)}"
        }
    }
}
