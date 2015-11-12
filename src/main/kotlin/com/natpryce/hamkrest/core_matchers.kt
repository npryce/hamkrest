package com.natpryce.hamkrest


fun <T> equalTo(expected: T): com.natpryce.hamkrest.Matcher<T> =
        object : com.natpryce.hamkrest.Matcher.Primitive<T>() {
            override fun invoke(actual: T): com.natpryce.hamkrest.MatchResult = com.natpryce.hamkrest.internal.match(actual == expected) { "was ${com.natpryce.hamkrest.delimit(actual)}" }

            override fun description(): String {
                return "equal to ${com.natpryce.hamkrest.delimit(expected)}"
            }
        }


fun <T> absent(): com.natpryce.hamkrest.Matcher<T?> = object : com.natpryce.hamkrest.Matcher.Primitive<T?>() {
    override fun invoke(p1: T?): com.natpryce.hamkrest.MatchResult = com.natpryce.hamkrest.internal.match(p1 == null) { "was ${com.natpryce.hamkrest.delimit(p1)}" }

    override fun description(): String = "null"
}


fun <T> present(valueMatcher: com.natpryce.hamkrest.Matcher<T>): com.natpryce.hamkrest.Matcher<T?> =
        object : com.natpryce.hamkrest.Matcher.Primitive<T?>() {
            override fun invoke(actual: T?): com.natpryce.hamkrest.MatchResult {
                return when (actual) {
                    null -> com.natpryce.hamkrest.MatchResult.Mismatch("was null")
                    else -> valueMatcher(actual)
                }
            }

            override fun description(): String {
                return "is not null & " + valueMatcher.description()
            }
        }

inline fun <reified T : Any> cast(downcastMatcher: com.natpryce.hamkrest.Matcher<T>): com.natpryce.hamkrest.Matcher<Any> {
    return object : com.natpryce.hamkrest.Matcher.Primitive<Any>() {
        override fun invoke(actual: Any): com.natpryce.hamkrest.MatchResult {
            return when (actual) {
                is T -> {
                    downcastMatcher(actual)
                }
                else -> {
                    com.natpryce.hamkrest.MatchResult.Mismatch("had type ${actual.javaClass.kotlin.qualifiedName}")
                }
            }
        }

        override fun description(): String {
            return "has type " + T::class.qualifiedName + " & " + downcastMatcher.description()
        }
    }
}

fun <N : Comparable<N>> greaterThan(n: N) = com.natpryce.hamkrest._comparesAs("greater than", n) { it > 0 }
fun <N : Comparable<N>> greaterThanOrEqualTo(n: N) = com.natpryce.hamkrest._comparesAs("greater than or equal to", n) { it >= 0 }
fun <N : Comparable<N>> lessThan(n: N) = com.natpryce.hamkrest._comparesAs("less than", n) { it < 0 }
fun <N : Comparable<N>> lessThanOrEqualTo(n: N) = com.natpryce.hamkrest._comparesAs("less than or equal to", n) { it <= 0 }

private fun <N : Comparable<N>> _comparesAs(description: String, n: N, expectedSignum: (Int) -> Boolean): com.natpryce.hamkrest.Matcher<N> {
    return object : com.natpryce.hamkrest.Matcher.Primitive<N>() {
        override fun invoke(actual: N): com.natpryce.hamkrest.MatchResult =
                com.natpryce.hamkrest.internal.match(expectedSignum(actual.compareTo(n))) { "was ${com.natpryce.hamkrest.delimit(actual)}" }

        override fun description(): String {
            return "is ${description} ${com.natpryce.hamkrest.delimit(n)}"
        }
    }
}

fun <T : Comparable<T>> isWithin(range: Range<T>) : com.natpryce.hamkrest.Matcher<T> {
    fun _isWithin(actual: T, range: Range<T>): Boolean {
        return range.contains(actual)
    }

    return com.natpryce.hamkrest.Matcher.Companion(::_isWithin, range)
}

