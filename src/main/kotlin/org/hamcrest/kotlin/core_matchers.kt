package org.hamcrest.kotlin

import kotlin.reflect.KClass
import kotlin.reflect.jvm.reflect


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

public inline fun <reified T : Any> isA(downcaseMatcher: Matcher<T> ) : Matcher<Any> {
    return object : Matcher.Primitive<Any>() {
        override fun invoke(actual: Any): MatchResult {
            return when(actual) {
                is T -> {
                    downcaseMatcher(actual)
                }
                else -> {
                    MatchResult.Mismatch("was a " + actual.javaClass.kotlin.qualifiedName)
                }
            }
        }

        override fun description(): String {
            return "of type " + T::class.qualifiedName + " & " + downcaseMatcher.description()
        }
    }
}

