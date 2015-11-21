@file:JvmName("StringMatchers")

package com.natpryce.hamkrest

import kotlin.reflect.KFunction3
import kotlin.text.Regex

sealed class CaseSensitivity(val ignoreCase: Boolean, internal val description: String) {
    object CaseSensitive : CaseSensitivity(false, "")
    object CaseInsensitive : CaseSensitivity(true, " (case insensitive)")
}

abstract class StringMatcher<T : CaseSensitivity>(protected  val caseSensitivity: T) : Matcher.Primitive<CharSequence>() {
    abstract fun caseSensitive(): StringMatcher<CaseSensitivity.CaseSensitive>
    abstract fun caseInsensitive(): StringMatcher<CaseSensitivity.CaseInsensitive>

    companion object {
        operator fun <T, S : CaseSensitivity> invoke(fn: KFunction3<CharSequence, T, Boolean, Boolean>, expected: T, sensitivity: S): StringMatcher<S> {
            return object : StringMatcher<S>(sensitivity) {
                override fun caseSensitive() = StringMatcher(fn, expected, CaseSensitivity.CaseSensitive)
                override fun caseInsensitive() = StringMatcher(fn, expected, CaseSensitivity.CaseInsensitive)

                override fun description() =
                        "${identifierToDescription(fn.name)} ${describe(expected)}${caseSensitivity.description}"

                override fun negatedDescription() =
                        "${identifierToNegatedDescription(fn.name)} ${describe(expected)}${caseSensitivity.description}"

                override fun invoke(actual: CharSequence): MatchResult {
                    return match(fn(actual, expected, caseSensitivity.ignoreCase)) { "was ${describe(actual)}" }
                }
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
fun matches(r: Regex): Matcher<CharSequence> = Matcher(CharSequence::matches, r)

private fun CharSequence._startsWith(prefix: CharSequence, ignoreCase: Boolean) =
        this.startsWith(prefix, ignoreCase)

private fun CharSequence._endsWith(prefix: CharSequence, ignoreCase: Boolean) =
        this.endsWith(prefix, ignoreCase)

private fun CharSequence._containsSubstring(substring: CharSequence, ignoreCase: Boolean) =
        this.contains(substring, ignoreCase)

fun startsWith(prefix: CharSequence) = StringMatcher(CharSequence::_startsWith, prefix)
fun endsWith(suffix: CharSequence) = StringMatcher(CharSequence::_endsWith, suffix)
fun containsSubstring(substring: CharSequence) = StringMatcher(CharSequence::_containsSubstring, substring)

