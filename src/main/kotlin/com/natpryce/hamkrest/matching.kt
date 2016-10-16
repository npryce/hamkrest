@file:JvmName("Matchers")

package com.natpryce.hamkrest

import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2
import kotlin.reflect.KProperty1

/**
 * The result of matching some actual value against criteria defined by a [Matcher].
 */
sealed class MatchResult {
    /**
     * Represents that the actual value matched.
     */
    object Match : MatchResult() {
        override fun toString(): String = "Match"
    }

    /**
     * Represents that the actual value did not match, and includes a human-readable description of the reason.
     *
     * @param description human readable text that explains why the value did not match.
     */
    class Mismatch(override val description: String) : MatchResult(), SelfDescribing {
        override fun toString(): String {
            return "Mismatch[${describe(description)}]"
        }
    }
}

/**
 * Acceptability criteria for a value of type [T].  A Matcher reports if a value of type T matches
 * the criteria and describes the criteria in human-readable language.
 *
 * A Matcher is either a "primitive" matcher, that implements the criteria in code, or a logical combination
 * (`not`, `and`, or `or`) of other matchers.
 *
 * To implement your own primitive matcher, create a subclass of [Matcher.Primitive].
 */
interface Matcher<in T> : (T) -> MatchResult, SelfDescribing {

    /**
     * Reports whether the [actual] value meets the criteria and, if not, why it does not match.
     */
    override fun invoke(actual: T): MatchResult

    /**
     * The description of this criteria.
     */
    override val description: String

    /**
     * Describes the negation of this criteria.
     */
    open val negatedDescription: String get() = "not " + description

    /**
     * Returns a matcher that matches the negation of this criteria.
     */
    open operator fun not(): Matcher<T> {
        return Negation(this)
    }

    /**
     * Returns this matcher as a predicate, that can be used for testing, finding and filtering collections
     * and [kotlin.Sequence]s.
     */
    open fun asPredicate(): (T) -> Boolean = { this(it) == MatchResult.Match }

    /**
     * The negation of a matcher.
     *
     * @property negated the matcher to be negated
     */
    class Negation<in T>(private val negated: Matcher<T>) : Matcher<T> {
        override fun invoke(actual: T): MatchResult =
                when (negated(actual)) {
                    MatchResult.Match -> {
                        MatchResult.Mismatch("was: ${describe(actual)}")
                    }
                    is MatchResult.Mismatch -> {
                        MatchResult.Match
                    }
                }

        override val description = negated.negatedDescription
        override val negatedDescription = negated.description
        override operator fun not() = negated
    }

    /**
     * The logican disjunction ("or") of two matchers.  Evaluation is short-cut, so that if the [left]
     * matcher matches, the [right] matcher is never invoked.
     *
     * Use the infix [or] function to combine matchers with a Disjunction.
     *
     * @property left The left operand. This operand is always evaluated.
     * @property right The right operand.  This operand will not be evaluated if the result can be determined from [left].
     */
    class Disjunction<in T>(private val left: Matcher<T>, private val right: Matcher<T>) : Matcher<T> {
        override fun invoke(actual: T): MatchResult =
                left(actual).let { l ->
                    when (l) {
                        MatchResult.Match -> l
                        is MatchResult.Mismatch -> right(actual).let { r ->
                            when (r) {
                                MatchResult.Match -> r
                                is MatchResult.Mismatch -> l
                            }
                        }
                    }
                }

        override val description: String = "${left.description} or ${right.description}"
    }

    /**
     * The logican conjunction ("and") of two matchers.  Evaluation is short-cut, so that if the [left]
     * matcher fails to match, the [right] matcher is never invoked.
     *
     * Use the infix [and] function to combine matchers with a Disjunction.
     *
     * @property left The left operand. This operand is always evaluated.
     * @property right The right operand.  This operand will not be evaluated if the result can be determined from [left].
     */
    class Conjunction<in T>(private val left: Matcher<T>, private val right: Matcher<T>) : Matcher<T> {
        override fun invoke(actual: T): MatchResult =
                left(actual).let { l ->
                    when (l) {
                        MatchResult.Match -> right(actual)
                        is MatchResult.Mismatch -> l
                    }
                }

        override val description: String = "${left.description} and ${right.description}"
    }

    /**
     * Base class of matchers for which the match criteria is coded, not composed.  Subclass this to write
     * your own matchers.
     */
    abstract class Primitive<in T> : Matcher<T>


    companion object {
        /**
         * Converts a unary predicate into a Matcher. The description is derived from the name of the predicate.
         *
         * @param fn the predicate to convert into a [Matcher]<T>.
         */
        operator fun <T> invoke(fn: KFunction1<T, Boolean>): Matcher<T> = object : Matcher<T> {
            override fun invoke(actual: T): MatchResult = match(fn(actual)) { "was: ${describe(actual)}" }
            override val description = identifierToDescription(fn.name)
            override val negatedDescription = identifierToNegatedDescription(fn.name)
            override fun asPredicate(): (T) -> Boolean = fn
        }

        /**
         * Converts a binary predicate and second argument into a Matcher that receives the first argument.
         * The description is derived from the name of the predicate.
         *
         * @param fn The predicate to convert into a [Matcher]<T>
         * @param cmp The second argument to be passed to [fn]
         */
        public operator fun <T, U> invoke(fn: KFunction2<T, U, Boolean>, cmp: U): Matcher<T> = object : Matcher.Primitive<T>() {
            override fun invoke(actual: T): MatchResult = match(fn(actual, cmp)) { "was: ${describe(actual)}" }
            override val description: String = "${identifierToDescription(fn.name)} ${describe(cmp)}"
            override val negatedDescription: String = "${identifierToNegatedDescription(fn.name)} ${describe(cmp)}"
        }

        /**
         * Converts a binary predicate into a factory function that receives the second argument of the predicate and
         * returns a Matcher that receives the first argument. The description of the matcher is derived from the name
         * of the predicate.
         *
         * @param fn The predicate to convert into a [Matcher]<T>
         */
        operator fun <T, U> invoke(fn: KFunction2<T, U, Boolean>): (U) -> Matcher<T> = { Matcher(fn, it) }
    }
}

/**
 * Syntactic sugar to create a [Matcher.Disjunction]
 */
infix fun <T> Matcher<T>.or(that: Matcher<T>): Matcher<T> = Matcher.Disjunction(this, that)

/**
 * Syntactic sugar to create a [Matcher.Disjunction]
 */
infix fun <T> KFunction1<T, Boolean>.or(that: Matcher<T>): Matcher<T> = Matcher.Disjunction(Matcher(this), that)

/**
 * Syntactic sugar to create a [Matcher.Disjunction]
 */
infix fun <T> Matcher<T>.or(that: KFunction1<T, Boolean>): Matcher<T> = Matcher.Disjunction(this, Matcher(that))

/**
 * Syntactic sugar to create a [Matcher.Disjunction]
 */
infix fun <T> KFunction1<T, Boolean>.or(that: KFunction1<T, Boolean>): Matcher<T> = Matcher.Disjunction(Matcher(this), Matcher(that))

/**
 * Syntactic sugar to create a [Matcher.Conjunction]
 */
infix fun <T> Matcher<T>.and(that: Matcher<T>): Matcher<T> = Matcher.Conjunction<T>(this, that)

/**
 * Syntactic sugar to create a [Matcher.Conjunction]
 */
infix fun <T> KFunction1<T, Boolean>.and(that: Matcher<T>): Matcher<T> = Matcher.Conjunction(Matcher(this), that)

/**
 * Syntactic sugar to create a [Matcher.Conjunction]
 */
infix fun <T> Matcher<T>.and(that: KFunction1<T, Boolean>): Matcher<T> = Matcher.Conjunction(this, Matcher(that))

/**
 * Syntactic sugar to create a [Matcher.Conjunction]
 */
infix fun <T> KFunction1<T, Boolean>.and(that: KFunction1<T, Boolean>): Matcher<T> = Matcher.Conjunction(Matcher(this), Matcher(that))


/**
 * Returns a matcher that applies [featureMatcher] to the result of applying [feature] to a value.
 * The description of the matcher uses [name] to describe the [feature].
 *
 * @param name the name to be used to describe [feature]
 * @param feature a function that extracts a feature of a value to be matched by [featureMatcher]
 * @param featureMatcher a matcher applied to the result of the [feature]
 */
fun <T, R> has(name: String, feature: (T) -> R, featureMatcher: Matcher<R>): Matcher<T> = object : Matcher.Primitive<T>() {
    override fun invoke(actual: T) =
            featureMatcher(feature(actual)).let {
                when (it) {
                    is MatchResult.Mismatch -> MatchResult.Mismatch("had ${name} that ${it.description}")
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
        has(identifierToDescription(property.name), property.getter, propertyMatcher)


/**
 * Returns a matcher that applies [featureMatcher] to the result of applying [feature] to a value.
 *
 * @param feature a function that extracts a feature of a value to be matched by [featureMatcher]
 * @param featureMatcher a matcher applied to the result of the [feature]
 */
fun <T, R> has(feature: KFunction1<T, R>, featureMatcher: Matcher<R>): Matcher<T> =
        has(identifierToDescription(feature.name), feature, featureMatcher)

fun <T> Matcher<T>.describedBy(fn: ()->String) = object : Matcher<T> by this {
    override val description : String get() = fn()
}
