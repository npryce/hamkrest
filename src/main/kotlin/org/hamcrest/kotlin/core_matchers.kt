package org.hamcrest.kotlin


public fun <T> equalTo(expected: T): Matcher<T> =
        object : Matcher.Primitive<T>() {
            override fun invoke(actual: T): MatchResult = match(actual == expected) { delimit(actual) }

            override fun description(): String {
                return "equal to ${delimit(expected)}"
            }
        }


public fun <T> absent(): Matcher<T?> = object : Matcher.Primitive<T?>() {
    override fun invoke(p1: T?): MatchResult = match(p1 == null) { delimit(p1) }

    override fun description(): String = "null"
}


public fun <T> present(valueMatcher : Matcher<T>) : Matcher<T?> =
        object : Matcher.Primitive<T?>() {
            override fun invoke(actual: T?): MatchResult {
                return when(actual) {
                    null -> MatchResult.Mismatch("null")
                    else -> valueMatcher(actual)
                }
            }

            override fun description(): String {
                return "not null & " + valueMatcher.description()
            }
        }

public fun <T> isA(type: Class<T>, downcaseMatcher: Matcher<T> ) : Matcher<Any> {
    return object : Matcher.Primitive<Any>() {
        override fun invoke(actual: Any): MatchResult {
            return when(actual) {
                type.isInstance(actual) -> downcaseMatcher(type.cast(actual))
                else -> MatchResult.Mismatch("of type " + actual.javaClass.name)
            }
        }

        override fun description(): String {
            return "of type " + type.name + " & " + downcaseMatcher.description()
        }
    }
}