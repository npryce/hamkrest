package com.natpryce.hamkrest

import com.natpryce.hamkrest.internal.match


/**
 * Returns a matcher that reports if a value is equal to an [expected] value.
 */
fun <T> equalTo(expected: T): Matcher<T> =
        object : Matcher.Primitive<T>() {
            override fun invoke(actual: T): MatchResult = match(actual == expected) { "was ${describe(actual)}" }
            override fun description() = "is equal to ${describe(expected)}"
            override fun negatedDescription() = "is not equal to ${describe(expected)}"
        }


/**
 * Returns a matcher that reports if a value is null.
 */
fun <T> absent(): Matcher<T?> = object : Matcher.Primitive<T?>() {
    override fun invoke(actual: T?): MatchResult = match(actual == null) { "was ${describe(actual)}" }
    override fun description(): String = "null"
}

/**
 * Returns a matcher that reports if a value is not null and meets the criteria of the [valueMatcher]
 */
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

/**
 * Returns a matcher that reports if a value of [Any] type is of a type compatible with [downcastMatcher] and, if so,
 * if the value meets its criteria.
 */
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

/**
 * Returns a matcher that reports if a [Comparable] value is greater than [n]
 */
fun <N : Comparable<N>> greaterThan(n: N) = _comparesAs("greater than", n) { it > 0 }

/**
 * Returns a matcher that reports if a [Comparable] value is greater than or equal to [n]
 */
fun <N : Comparable<N>> greaterThanOrEqualTo(n: N) = _comparesAs("greater than or equal to", n) { it >= 0 }

/**
 * Returns a matcher that reports if a [Comparable] value is less than [n]
 */
fun <N : Comparable<N>> lessThan(n: N) = _comparesAs("less than", n) { it < 0 }

/**
 * Returns a matcher that reports if a [Comparable] value is less than or equal to [n]
 */
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

/**
 * Returns a matcher that reports if a [Comparable] value falls within a given [Range]
 */
fun <T : Comparable<T>> isWithin(range: Range<T>) : Matcher<T> {
    fun _isWithin(actual: T, range: Range<T>): Boolean {
        return range.contains(actual)
    }

    return Matcher.Companion(::_isWithin, range)
}

