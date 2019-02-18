package com.natpryce.hamkrest

import kotlin.reflect.KFunction1
import kotlin.reflect.KProperty1


/**
 * Syntactic sugar to create a [Matcher.Disjunction]
 */
infix fun <T> Matcher<T>.or(that: Matcher<T>): Matcher<T> = Disjunction(this, that)

/**
 * Syntactic sugar to create a [Matcher.Disjunction]
 */
infix fun <T> KFunction1<T, Boolean>.or(that: Matcher<T>): Matcher<T> = Disjunction(Matcher(this), that)

/**
 * Syntactic sugar to create a [Matcher.Disjunction]
 */
infix fun <T> Matcher<T>.or(that: KFunction1<T, Boolean>): Matcher<T> = Disjunction(this, Matcher(that))

/**
 * Syntactic sugar to create a [Matcher.Disjunction]
 */
infix fun <T> KFunction1<T, Boolean>.or(that: KFunction1<T, Boolean>): Matcher<T> = Disjunction(Matcher(this), Matcher(that))

/**
 * Syntactic sugar to create a [Matcher.Conjunction]
 */
infix fun <T> Matcher<T>.and(that: Matcher<T>): Matcher<T> = Conjunction<T>(this, that)

/**
 * Syntactic sugar to create a [Matcher.Conjunction]
 */
infix fun <T> KFunction1<T, Boolean>.and(that: Matcher<T>): Matcher<T> = Conjunction(Matcher(this), that)

/**
 * Syntactic sugar to create a [Matcher.Conjunction]
 */
infix fun <T> Matcher<T>.and(that: KFunction1<T, Boolean>): Matcher<T> = Conjunction(this, Matcher(that))

/**
 * Syntactic sugar to create a [Matcher.Conjunction]
 */
infix fun <T> KFunction1<T, Boolean>.and(that: KFunction1<T, Boolean>): Matcher<T> = Conjunction(Matcher(this), Matcher(that))

/**
 * Returns a matcher that matches if all of the supplied matchers match.
 */
fun <T> allOf(matchers: List<Matcher<T>>): Matcher<T> = matchers.reducedWith(Matcher<T>::and)

/**
 * Returns a matcher that matches if all of the supplied matchers match.
 */
fun <T> allOf(vararg matchers: Matcher<T>): Matcher<T> = allOf(matchers.asList())

/**
 * Returns a matcher that matches if any of the supplied matchers match.
 */
fun <T> anyOf(matchers: List<Matcher<T>>): Matcher<T> = matchers.reducedWith(Matcher<T>::or)

/**
 * Returns a matcher that matches if any of the supplied matchers match.
 */
fun <T> anyOf(vararg matchers: Matcher<T>): Matcher<T> = anyOf(matchers.asList())


/**
 * Returns a matcher that applies [featureMatcher] to the result of applying [feature] to a value.
 * The description of the matcher uses [name] to describe the [feature].
 *
 * @param name the name to be used to describe [feature]
 * @param feature a function that extracts a feature of a value to be matched by [featureMatcher]
 * @param featureMatcher a matcher applied to the result of the [feature]
 */
fun <T, R> has(name: String, feature: (T) -> R, featureMatcher: Matcher<R>): Matcher<T> = object : PrimitiveMatcher<T>() {
    override fun invoke(actual: T) =
        featureMatcher(feature(actual)).let {
            when (it) {
                is Mismatch -> Mismatch("had ${name} that ${it.description}")
                else -> it
            }
        }
    
    override val description = "has ${name} that ${featureMatcher.description}"
    override val negatedDescription = "does not have ${name} that ${featureMatcher.description}"
}

/**
 * Returns a matcher that applies [propertyMatcher] to the current value of [property] of an object.
 */
fun <T, R> has(property: KProperty1<T, R>, propertyMatcher: Matcher<R>): Matcher<T> =
    has(identifierToDescription(property.name), property, propertyMatcher)


/**
 * Returns a matcher that applies [featureMatcher] to the result of applying [feature] to a value.
 *
 * @param feature a function that extracts a feature of a value to be matched by [featureMatcher]
 * @param featureMatcher a matcher applied to the result of the [feature]
 */
fun <T, R> has(feature: KFunction1<T, R>, featureMatcher: Matcher<R>): Matcher<T> =
    has(identifierToDescription(feature.name), feature, featureMatcher)

fun <T> Matcher<T>.describedBy(fn: () -> String) = object : Matcher<T> by this {
    override val description: String get() = fn()
}

private fun <T> List<Matcher<T>>.reducedWith(op: (Matcher<T>, Matcher<T>) -> Matcher<T>): Matcher<T> = when {
    isEmpty() -> anything
    else -> reduce(op)
}
