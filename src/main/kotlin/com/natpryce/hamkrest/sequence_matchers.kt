package com.natpryce.hamkrest

import com.natpryce.hamkrest.internal.match
import kotlin.reflect.KFunction1


fun <T> containsAny(elementPredicate: KFunction1<T,Boolean>) : Matcher<Iterable<T>> = containsAny(Matcher(elementPredicate))
fun <T> containsAll(elementPredicate: KFunction1<T,Boolean>) : Matcher<Iterable<T>> = containsAll(Matcher(elementPredicate))

fun <T> containsAny(elementMatcher: Matcher<T>) : Matcher<Iterable<T>> {
    return object : Matcher.Primitive<Iterable<T>>() {
        override fun invoke(actual: Iterable<T>): MatchResult =
                match(actual.any(elementMatcher.asPredicate())) {"was ${describe(actual)}"}

        override fun description(): String = "in which any element ${describe(elementMatcher)}"
        override fun negatedDescription() : String = "in which no element ${describe(elementMatcher)}"
    }
}

fun <T> containsAll(elementMatcher: Matcher<T>) : Matcher<Iterable<T>> {
    return object : Matcher.Primitive<Iterable<T>>() {
        override fun invoke(actual: Iterable<T>): MatchResult =
                match(actual.all(elementMatcher.asPredicate())) {"was ${describe(actual)}"}

        override fun description(): String = "in which all elements ${describe(elementMatcher)}"
        override fun negatedDescription() : String = "in which not all elements ${describe(elementMatcher)}"
    }
}

