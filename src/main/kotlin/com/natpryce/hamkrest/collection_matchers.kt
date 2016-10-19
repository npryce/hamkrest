@file:JvmName("CollectionMatchers")

package com.natpryce.hamkrest

import kotlin.reflect.KFunction1

/**
 * Matches an [Iterable] if any element is matched by [elementMatcher].
 */
fun <T> anyElement(elementMatcher: Matcher<T>) = object : Matcher.Primitive<Iterable<T>>() {
    override fun invoke(actual: Iterable<T>): MatchResult =
        match(actual.any(elementMatcher.asPredicate())) { "was ${describe(actual)}" }
    
    override val description: String get() = "in which any element ${describe(elementMatcher)}"
    override val negatedDescription: String get() = "in which no element ${describe(elementMatcher)}"
}

/**
 * Matches an [Iterable] if any element is matched by [elementPredicate].
 */
fun <T> anyElement(elementPredicate: KFunction1<T, Boolean>) = anyElement(Matcher(elementPredicate))

/**
 * Matches an [Iterable] if all elements are matched by [elementMatcher].
 */
fun <T> allElements(elementMatcher: Matcher<T>) = object : Matcher.Primitive<Iterable<T>>() {
    override fun invoke(actual: Iterable<T>): MatchResult =
        match(actual.all(elementMatcher.asPredicate())) { "was ${describe(actual)}" }
    
    override val description: String get() = "in which all elements ${describe(elementMatcher)}"
    override val negatedDescription: String get() = "in which not all elements ${describe(elementMatcher)}"
}

/**
 * Matches an [Iterable] if all elements are matched by [elementPredicate].
 */
fun <T> allElements(elementPredicate: KFunction1<T, Boolean>) = allElements(Matcher(elementPredicate))

/**
 * Matches an empty collection.
 */
val isEmpty = Matcher(Collection<Any>::isEmpty)

/**
 * Matches a collection with a size that matches [sizeMatcher].
 */
fun hasSize(sizeMatcher: Matcher<Int>) = has(Collection<Any>::size, sizeMatcher)

/**
 * Matches a collection that contains [element]
 *
 * See [Collection::contains]
 */
fun <T> hasElement(element: T): Matcher<Collection<T>> = object : Matcher.Primitive<Collection<T>>() {
    override fun invoke(actual: Collection<T>): MatchResult =
        match(element in actual) { "was ${describe(actual)}" }
    override val description: String get() = "contains ${describe(element)}"
    override val negatedDescription: String get() = "does not contain ${describe(element)}"
}

fun <T> isIn(i: Iterable<T>) : Matcher<T> = object : Matcher.Primitive<T>() {
    override fun invoke(actual: T) = match(actual in i) {"was not in ${describe(i)}"}
    override val description: String get() = "is in ${describe(i)}"
    override val negatedDescription: String get() = "is not in ${describe(i)}"
}

fun <T> isIn(vararg elements: T) = isIn(elements.toList())


fun <T> containsAll(vararg elements: T, matcher: (T) -> Matcher<T> = { equalTo(it) }): Matcher<Iterable<T>> {
    return containsAll(elements.map{ matcher(it) })
}

fun <T> containsAll(elements: Iterable<T>, matcher: (T) -> Matcher<T> = { equalTo(it) }): Matcher<Iterable<T>> {
    return containsAll(elements.map { matcher(it) })
}

fun <T> containsAll(vararg elementMatchers: Matcher<T>): Matcher<Iterable<T>> {
    return containsAll(elementMatchers.toList())
}

/**
 * Matches an [Iterable] if for each element there is one corresponding matcher.
 */
fun <T> containsAll(elementMatchers: Iterable<Matcher<T>>): Matcher<Iterable<T>> {
    return ContainsAllMatcher(elementMatchers)
}

/**
 * Matches an [Iterable] if it contains all the elements in any order
 * and nested [Iterable]s match according to the same rules.
 */
fun <T> containsAllNested(elements: Iterable<T>, leafMatcher: (T) -> Matcher<T> = { equalTo(it) }): Matcher<Iterable<T>> {
    if (elements.count() > 0 && elements.first() is Iterable<*>) {
        @Suppress("UNCHECKED_CAST")
        return containsAll(elements.map { containsAll(it as Iterable<*>) as Matcher<T> })
    } else {
        return containsAll(elements.map { leafMatcher(it) })
    }
}


internal class ContainsAllMatcher<in T>(val matchers: Iterable<Matcher<T>>) : Matcher<Iterable<T>> {

    override fun invoke(actual: Iterable<T>): MatchResult {
        val matching = Matching(matchers)
        for (element in actual) {
            val matchResult = matching.matches(element)
            if (matchResult != MatchResult.Match) {
                return matchResult
            }
        }
        return matching.isFinished(actual)
    }

    override val description: String
        get() = "iterable with elements [${matchers.joinToString{ it.description }}] in any order"


    private class Matching<in S>(matchers: Iterable<Matcher<S>>) {
        private val matchers = matchers.toMutableList()

        fun matches(element: S): MatchResult {
            if (matchers.isEmpty()) {
                return MatchResult.Mismatch("no match for: $element")
            }
            for (matcher in matchers) {
                if (matcher.invoke(element) == MatchResult.Match) {
                    matchers.remove(matcher)
                    return MatchResult.Match
                }
            }
            return MatchResult.Mismatch("not matched: $element")
        }

        fun isFinished(elements: Iterable<S>): MatchResult {
            if (matchers.isEmpty()) {
                return MatchResult.Match
            }
            return MatchResult.Mismatch("no element matches: [${matchers.joinToString{ it.description }}] in [${elements.joinToString()}]")
        }
    }
}
