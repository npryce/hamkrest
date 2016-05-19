@file:JvmName("CollectionMatchers")

package com.natpryce.hamkrest

import kotlin.reflect.KFunction1

/**
 * Matches an [Iterable] if any element is matched by [elementMatcher].
 */
fun <T> anyElement(elementMatcher: Matcher<T>) : Matcher<Iterable<T>> {
    return object : Matcher.Primitive<Iterable<T>>() {
        override fun invoke(actual: Iterable<T>): MatchResult =
                match(actual.any(elementMatcher.asPredicate())) { "was ${describe(actual)}" }

        override val description: String get() = "in which any element ${describe(elementMatcher)}"
        override val negatedDescription : String get() = "in which no element ${describe(elementMatcher)}"
    }
}

/**
 * Matches an [Iterable] if any element is matched by [elementPredicate].
 */
fun <T> anyElement(elementPredicate: KFunction1<T,Boolean>) : Matcher<Iterable<T>> = anyElement(Matcher(elementPredicate))

/**
 * Matches an [Iterable] if all elements are matched by [elementMatcher].
 */
fun <T> allElements(elementMatcher: Matcher<T>) : Matcher<Iterable<T>> {
    return object : Matcher.Primitive<Iterable<T>>() {
        override fun invoke(actual: Iterable<T>): MatchResult =
                match(actual.all(elementMatcher.asPredicate())) { "was ${describe(actual)}" }

        override val description: String get() = "in which all elements ${describe(elementMatcher)}"
        override val negatedDescription : String get() = "in which not all elements ${describe(elementMatcher)}"
    }
}

/**
 * Matches an [Iterable] if all elements are matched by [elementPredicate].
 */
fun <T> allElements(elementPredicate: KFunction1<T,Boolean>) : Matcher<Iterable<T>> = allElements(Matcher(elementPredicate))

/**
 * Matches an empty collection.
 */
val isEmpty = Matcher(Collection<Any>::isEmpty)

/**
 * Matches a collection with a size that matches [sizeMatcher].
 */
fun hasSize(sizeMatcher : Matcher<Int>) = has(Collection<Any>::size, sizeMatcher)
