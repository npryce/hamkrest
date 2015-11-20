@file:JvmName("StringMatchers")

package com.natpryce.hamkrest

import kotlin.reflect.KFunction3
import kotlin.text.Regex

sealed class CaseSensitivity(val ignoreCase: Boolean, private val description: String) : SelfDescribing {
    override fun description(): String = description

    object CaseSensitive : CaseSensitivity(false, "case sensitive")

    object CaseInsensitive : CaseSensitivity(true, "case insensitive")
}

abstract class StringMatcher<T : CaseSensitivity>(private val caseSensitivity: T) : Matcher.Primitive<CharSequence>() {
    override fun invoke(actual: CharSequence): MatchResult {
        return match(stringMatches(actual, caseSensitivity.ignoreCase)) { "was ${describe(actual)}" }
    }

    protected abstract fun stringMatches(actual: CharSequence, ignoreCase: Boolean): Boolean

    abstract fun caseSensitive(): StringMatcher<CaseSensitivity.CaseSensitive>
    abstract fun caseInsensitive(): StringMatcher<CaseSensitivity.CaseInsensitive>

    companion object {
        operator fun <T, S : CaseSensitivity> invoke(fn: KFunction3<CharSequence, T, Boolean, Boolean>, expected: T, sensitivity: S): StringMatcher<S> {
            return object : StringMatcher<S>(sensitivity) {
                override fun stringMatches(actual: CharSequence, ignoreCase: Boolean) = fn(actual, expected, ignoreCase)
                override fun caseSensitive() = StringMatcher(fn, expected, CaseSensitivity.CaseSensitive)
                override fun caseInsensitive() = StringMatcher(fn, expected, CaseSensitivity.CaseInsensitive)

                override fun description() =
                        "${identifierToDescription(fn.name)} ${describe(expected)} (case sensitive)"

                override fun negatedDescription() =
                        "${identifierToNegatedDescription(fn.name)} ${describe(expected)} (case insensitive)"
            }
        }

        operator fun <T> invoke(fn: KFunction3<CharSequence, T, Boolean, Boolean>, expected: T) =
                invoke(fn, expected, CaseSensitivity.CaseSensitive)
    }
}


/**
 * Matches strings that contain the given [Regex].
 *
 * @see [String.contains]
 */
private fun _contains(s: CharSequence, regex: Regex): Boolean = regex.containsMatchIn(s)

fun contains(r: Regex): Matcher<String> = Matcher(::_contains, r)

/**
 * Matches strings that match the given [Regex].
 *
 * @see [String.matches]
 */
fun matches(r: Regex): Matcher<String> = Matcher(String::matches, r)

private fun CharSequence._startsWith(prefix: CharSequence, ignoreCase: Boolean) =
        this.startsWith(prefix, ignoreCase)

private fun CharSequence._endsWith(prefix: CharSequence, ignoreCase: Boolean) =
        this.endsWith(prefix, ignoreCase)

fun startsWith(prefix: CharSequence) = StringMatcher(CharSequence::_startsWith, prefix)
fun endsWith(suffix: CharSequence) = StringMatcher(CharSequence::_endsWith, suffix)

