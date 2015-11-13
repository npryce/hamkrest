package com.natpryce.hamkrest

import com.natpryce.hamkrest.internal.match


fun <T> equalTo(expected: T): Matcher<T> =
        object : Matcher.Primitive<T>() {
            override fun invoke(actual: T): MatchResult = match(actual == expected) { "was ${describe(actual)}" }

            override fun description(): String {
                return "equal to ${describe(expected)}"
            }
        }


fun <T> absent(): Matcher<T?> = object : Matcher.Primitive<T?>() {
    override fun invoke(actual: T?): MatchResult = match(actual == null) { "was ${describe(actual)}" }

    override fun description(): String = "null"
}


fun <T> present(valueMatcher: Matcher<T>): Matcher<T?> =
        object : Matcher.Primitive<T?>() {
            override fun invoke(actual: T?): MatchResult {
                return when (actual) {
                    null -> MatchResult.Mismatch("was null")
                    else -> valueMatcher(actual)
                }
            }

            override fun description(): String {
                return "is not null & " + valueMatcher.description()
            }
        }

inline fun <reified T : Any> cast(downcastMatcher: Matcher<T>): Matcher<Any> {
    return object : Matcher.Primitive<Any>() {
        override fun invoke(actual: Any): MatchResult {
            return when (actual) {
                is T -> {
                    downcastMatcher(actual)
                }
                else -> {
                    MatchResult.Mismatch("had type ${actual.javaClass.kotlin.qualifiedName}")
                }
            }
        }

        override fun description(): String {
            return "has type " + T::class.qualifiedName + " & " + downcastMatcher.description()
        }
    }
}

fun <N : Comparable<N>> greaterThan(n: N) = _comparesAs("greater than", n) { it > 0 }
fun <N : Comparable<N>> greaterThanOrEqualTo(n: N) = _comparesAs("greater than or equal to", n) { it >= 0 }
fun <N : Comparable<N>> lessThan(n: N) = _comparesAs("less than", n) { it < 0 }
fun <N : Comparable<N>> lessThanOrEqualTo(n: N) = _comparesAs("less than or equal to", n) { it <= 0 }

private fun <N : Comparable<N>> _comparesAs(description: String, n: N, expectedSignum: (Int) -> Boolean): Matcher<N> {
    return object : Matcher.Primitive<N>() {
        override fun invoke(actual: N): MatchResult =
                match(expectedSignum(actual.compareTo(n))) { "was ${describe(actual)}" }

        override fun description(): String {
            return "is ${description} ${describe(n)}"
        }
    }
}

fun <T : Comparable<T>> isWithin(range: Range<T>) : Matcher<T> {
    fun _isWithin(actual: T, range: Range<T>): Boolean {
        return range.contains(actual)
    }

    return Matcher.Companion(::_isWithin, range)
}

