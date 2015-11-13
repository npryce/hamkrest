package com.natpryce.hamkrest

import com.natpryce.hamkrest.internal.identifierToDescription
import com.natpryce.hamkrest.internal.identifierToNegatedDescription
import com.natpryce.hamkrest.internal.match
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2
import kotlin.reflect.KProperty1


sealed class MatchResult {
    object Match : MatchResult() {
        override fun toString(): String = "Match"
    }

    class Mismatch(private val description: String) : MatchResult() {
        fun description(): String {
            return description;
        }

        override fun toString(): String {
            return "Mismatch[${delimit(description)}]"
        }
    }
}


public sealed class Matcher<in T> : (T) -> MatchResult {
    abstract override fun invoke(actual: T): MatchResult
    abstract fun description(): String
    open fun negatedDescription(): String = "not " + description()

    open operator fun not(): Matcher<T> {
        return Negation(this)
    }

    open fun asPredicate(): (T) -> Boolean = { this(it) == MatchResult.Match }

    class Negation<in T>(private val negated: Matcher<T>) : Matcher<T>() {
        override fun invoke(actual: T): MatchResult =
                when (negated(actual)) {
                    MatchResult.Match -> {
                        MatchResult.Mismatch("was ${delimit(actual)}")
                    }
                    is MatchResult.Mismatch -> {
                        MatchResult.Match
                    }
                }

        override fun description(): String = negated.negatedDescription()
        override fun negatedDescription(): String = negated.description()
    }

    class Disjunction<in T>(private val left: Matcher<T>, private val right: Matcher<T>) : Matcher<T>() {
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

        override fun description(): String = "${left.description()} or ${right.description()}"
    }

    class Conjunction<in T>(private val left: Matcher<T>, private val right: Matcher<T>) : Matcher<T>() {
        override fun invoke(actual: T): MatchResult =
                left(actual).let { l ->
                    when (l) {
                        MatchResult.Match -> right(actual)
                        is MatchResult.Mismatch -> l
                    }
                }

        override fun description(): String = "${left.description()} and ${right.description()}"
    }

    abstract class Primitive<in T> : Matcher<T>()


    companion object {
        public operator fun <T> invoke(fn: KFunction1<T, Boolean>): Matcher<T> = object : Matcher.Primitive<T>() {
            override fun invoke(actual: T): MatchResult = match(fn(actual)) { "was ${delimit(actual)}" }
            override fun description(): String = identifierToDescription(fn.name)
            override fun negatedDescription() : String = identifierToNegatedDescription(fn.name)
            override fun asPredicate(): (T) -> Boolean = fn
        }

        public operator fun <T, U> invoke(fn: KFunction2<T, U, Boolean>, cmp: U): Matcher<T> = object : Matcher.Primitive<T>() {
            override fun invoke(actual: T): MatchResult = match(fn(actual, cmp)) { "was ${delimit(actual)}" }
            override fun description(): String = "${identifierToDescription(fn.name)} ${delimit(cmp)}"
            override fun negatedDescription(): String = "${identifierToNegatedDescription(fn.name)} ${delimit(cmp)}"
        }

        public operator fun <T, U> invoke(fn: KFunction2<T, U, Boolean>): (U) -> Matcher<T> = { Matcher(fn, it) }
    }
}

infix fun <T> Matcher<T>.or(that: Matcher<T>): Matcher<T> = Matcher.Disjunction(this, that)
infix fun <T> KFunction1<T, Boolean>.or(that: Matcher<T>): Matcher<T> = Matcher.Disjunction(Matcher(this), that)
infix fun <T> Matcher<T>.or(that: KFunction1<T, Boolean>): Matcher<T> = Matcher.Disjunction(this, Matcher(that))
infix fun <T> KFunction1<T, Boolean>.or(that: KFunction1<T, Boolean>): Matcher<T> = Matcher.Disjunction(Matcher(this), Matcher(that))

infix fun <T> Matcher<T>.and(that: Matcher<T>): Matcher<T> = Matcher.Conjunction<T>(this, that)
infix fun <T> KFunction1<T, Boolean>.and(that: Matcher<T>): Matcher<T> = Matcher.Conjunction(Matcher(this), that)
infix fun <T> Matcher<T>.and(that: KFunction1<T, Boolean>): Matcher<T> = Matcher.Conjunction(this, Matcher(that))
infix fun <T> KFunction1<T, Boolean>.and(that: KFunction1<T, Boolean>): Matcher<T> = Matcher.Conjunction(Matcher(this), Matcher(that))


fun <T, R> has(name: String, projection: (T) -> R, rMatcher: Matcher<R>): Matcher<T> = object : Matcher.Primitive<T>() {
    override fun invoke(actual: T) =
            rMatcher(projection(actual)).let {
                when (it) {
                    is MatchResult.Mismatch -> MatchResult.Mismatch("had ${name} that ${it.description()}")
                    else -> it
                }
            }

    override fun description() = "has ${name} that ${rMatcher.description()}"
    override fun negatedDescription() = "does not have ${name} that ${rMatcher.description()}"
}

fun <T, R> has(property: KProperty1<T, R>, rMatcher: Matcher<R>): Matcher<T> = has(property.name, property.getter, rMatcher)

fun <T, R> has(projection: KFunction1<T,R>, rMatcher: Matcher<R>) : Matcher<T> = has(projection.name, projection, rMatcher)


val anything = object : Matcher.Primitive<Any>() {
    override fun invoke(actual: Any): MatchResult = MatchResult.Match
    override fun description(): String = "anything"
    override fun negatedDescription() : String = "nothing"
}

val nothing = !anything
