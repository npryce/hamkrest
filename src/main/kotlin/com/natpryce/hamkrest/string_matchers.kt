@file:JvmName("StringMatchers")

package com.natpryce.hamkrest

import kotlin.reflect.KFunction3
import kotlin.text.Regex

sealed class CaseSensitivity {
    object CaseSensitive : CaseSensitivity()

    object CaseInsensitive : CaseSensitivity()
}

/**
 * A Matcher of strings with a specified case sensitivity.
 *
 * A case insensitive version of a case sensitive matcher can be obtained by calling its [caseInsensitive] method,
 * and a case sensitive version of a case insensitive matcher can be obtained by calling its [caseSensitive] method.
 *
 * If desired, case sensitivity can be enforced at compile time, by requiring a
 * `StringMatcher<CaseSensitivity.CaseInsensitive>` or `StringMatcher<CaseSensitivity.CaseSensitive>`.
 * If case sensitivity does not need to be enforced, require a `Matcher<String>`.
 */
abstract class StringMatcher<S : CaseSensitivity>(protected val caseSensitivity: S) : Matcher.Primitive<CharSequence>() {
    abstract internal fun <S2 : CaseSensitivity> withCaseSensitivity(s2: S2): StringMatcher<S2>

    companion object {
        operator fun <T, S : CaseSensitivity> invoke(fn: KFunction3<CharSequence, T, Boolean, Boolean>, expected: T, sensitivity: S): StringMatcher<S> {
            return object : StringMatcher<S>(sensitivity) {
                internal override fun <S2 : CaseSensitivity> withCaseSensitivity(s2: S2) = StringMatcher(fn, expected, s2)

                override fun description() =
                        "${identifierToDescription(fn.name)} ${describe(expected)}${suffix(caseSensitivity)}"

                override fun negatedDescription() =
                        "${identifierToNegatedDescription(fn.name)} ${describe(expected)}${suffix(caseSensitivity)}"

                override fun invoke(actual: CharSequence): MatchResult {
                    return match(fn(actual, expected, ignoreCase)) { "was ${describe(actual)}" }
                }
            }
        }

        operator fun <T> invoke(fn: KFunction3<CharSequence, T, Boolean, Boolean>, expected: T) =
                invoke(fn, expected, CaseSensitivity.CaseSensitive)
    }

    protected val ignoreCase: Boolean get() = caseSensitivity == CaseSensitivity.CaseInsensitive

    protected fun suffix(s: CaseSensitivity) = when (s) {
        CaseSensitivity.CaseSensitive -> ""
        CaseSensitivity.CaseInsensitive -> " (case insensitive)"
    }
}

fun StringMatcher<CaseSensitivity.CaseInsensitive>.caseSensitive() = withCaseSensitivity(CaseSensitivity.CaseSensitive)
fun StringMatcher<CaseSensitivity.CaseSensitive>.caseInsensitive() = withCaseSensitivity(CaseSensitivity.CaseInsensitive)

/**
 * Matches a char sequence if it contain the given [Regex].
 *
 * @see [String.contains]
 */
fun contains(r: Regex): Matcher<String> = Matcher(::_contains, r)

private fun _contains(s: CharSequence, regex: Regex): Boolean = regex.containsMatchIn(s)

/**
 * Matches a char sequence if it all characters matches the given [Regex].
 *
 * @see [String.matches]
 */
fun matches(r: Regex): Matcher<CharSequence> = Matcher(CharSequence::matches, r)


/**
 * Matches a char sequence if it starts with [prefix].
 *
 * A case insensitive version can be obtained by calling [StringMatcher.caseInsensitive].
 */
fun startsWith(prefix: CharSequence) = StringMatcher(CharSequence::_startsWith, prefix)

private fun CharSequence._startsWith(prefix: CharSequence, ignoreCase: Boolean) =
        this.startsWith(prefix, ignoreCase)

/**
 * Matches a char sequence if it ends with [suffix].
 *
 * A case insensitive version can be obtained by calling [StringMatcher.caseInsensitive].
 */
fun endsWith(suffix: CharSequence) = StringMatcher(CharSequence::_endsWith, suffix)

private fun CharSequence._endsWith(prefix: CharSequence, ignoreCase: Boolean) =
        this.endsWith(prefix, ignoreCase)

/**
 * Matches a char sequence if it contains [substring].
 *
 * A case insensitive version can be obtained by calling [StringMatcher.caseInsensitive].
 */
fun containsSubstring(substring: CharSequence) = StringMatcher(CharSequence::_containsSubstring, substring)

private fun CharSequence._containsSubstring(substring: CharSequence, ignoreCase: Boolean) =
        this.contains(substring, ignoreCase)

/**
 * Matches a char sequence if it is empty or consists solely of whitespace characters.
 */
val isBlank = Matcher(CharSequence::isBlank)

/**
 * Matches a nullable char sequence if it is either `null` or empty or consists solely of whitespace characters.
 */
val isNullOrBlank = Matcher(CharSequence::isNullOrBlank)

/**
 * Matches a char sequence if it is empty (contains no characters).
 */
val isEmptyString = Matcher(CharSequence::isEmpty)

/**
 * Matches a char sequence if it is empty (contains no characters).
 */
val isNullOrEmptyString = Matcher(CharSequence::isNullOrEmpty)
