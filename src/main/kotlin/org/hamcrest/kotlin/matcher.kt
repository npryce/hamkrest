package org.hamcrest.kotlin

import kotlin.reflect.KFunction1


public fun <T> delimit(v: T): String = when (v) {
    is String -> "\"" + v.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    else -> v.toString()
}

public fun match(comparisonResult: Boolean, describeMismatch: () -> String): MatchResult =
        if (comparisonResult) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch(describeMismatch())
        }


public sealed class MatchResult {
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
    public abstract fun description(): String;
    public open fun negatedDescription(): String = "not " + description();

    public open operator fun not(): Matcher<T> {
        return Negation(this);
    }

    public class Negation<in T>(private val negated: Matcher<T>) : Matcher<T>() {
        override fun invoke(actual: T): MatchResult =
                when (negated(actual)) {
                    MatchResult.Match -> {
                        MatchResult.Mismatch("was " + negated.description())
                    }
                    is MatchResult.Mismatch -> {
                        MatchResult.Match
                    }
                }

        override fun description(): String = negated.negatedDescription()
        override fun negatedDescription(): String = negated.description()
    }

    public class Disjunction<in T>(private val left: Matcher<T>, private val right: Matcher<T>) : Matcher<T>() {
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

    public class Conjunction<in T>(private val left: Matcher<T>, private val right: Matcher<T>) : Matcher<T>() {
        override fun invoke(actual: T): MatchResult =
            left(actual).let { l ->
                when (l) {
                    MatchResult.Match -> right(actual)
                    is MatchResult.Mismatch -> l
                }
            }

        override fun description(): String = "${left.description()} and ${right.description()}"
    }

    public abstract class Primitive<in T> : Matcher<T>()
}

public infix fun <T> Matcher<T>.or(that: Matcher<T>): Matcher<T> = Matcher.Disjunction<T>(this, that)

public infix fun <T> Matcher<T>.and(that: Matcher<T>): Matcher<T> = Matcher.Conjunction<T>(this, that)


public fun <T> (KFunction1<T, Boolean>).asMatcher(): Matcher<T> = object : Matcher.Primitive<T>() {
    override fun invoke(actual: T): MatchResult =
            match(this@asMatcher(actual)) { "was ${delimit(actual)}" }

    override fun description(): String = this@asMatcher.name
}
