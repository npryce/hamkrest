@file:JvmName("StringMatchers")

package com.natpryce.hamkrest

import kotlin.reflect.KFunction3
import kotlin.text.Regex

/**
 * The case sensitivity of a [StringMatcher].
 *
 * A case sensitive [StringMatcher] can be converted into a case insensitive equivalent by calling
 * [StringMatcher.caseSensitive], and a case insensitive [StringMatcher] can be converted into a
 * case sensitive equivalent by calling [StringMatcher.caseInsensitive].
 *
 * By convention, [StringMatcher]s are case sensitive by default.
 *
 * The [StringMatcher] type is parameterised by case sensitivity, so it is possible to enforce
 * at compile time whether a match should be case sensitive or not.
 */
sealed class CaseSensitivity {
    /**
     * Indicates that the match is case sensitive.
     */
    object CaseSensitive : CaseSensitivity()

    /**
     * Indicates that the match is case insensitive.
     */
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
 *
 * @property caseSensitivity The case sensitivity of the match, either [CaseSensitivity.CaseSensitive] or [CaseSensitivity.CaseInsensitive].
 */
abstract class StringMatcher<S : CaseSensitivity>(protected val caseSensitivity: S) : Matcher.Primitive<CharSequence>() {
    /**
     * Returns this matcher transformed to have the given case sensitivity.
     */
    abstract internal fun <S2 : CaseSensitivity> withCaseSensitivity(newSensitivity: S2): StringMatcher<S2>

    companion object {
        /**
         * Convert a String predicate to a [StringMatcher], specifying the desired case sensitivity.
         *
         * The predicate must have the signature `(CharSequence, T, Boolean) -> Boolean`, where the final Boolean
         * argument indicates case sensitivity.
         */
        operator fun <T, S : CaseSensitivity> invoke(fn: KFunction3<CharSequence, T, Boolean, Boolean>, expected: T, sensitivity: S): StringMatcher<S> {
            return object : StringMatcher<S>(sensitivity) {
                override fun <S2 : CaseSensitivity> withCaseSensitivity(newSensitivity: S2) =
                        StringMatcher(fn, expected, newSensitivity)
                override val description: String get() =
                        "${identifierToDescription(fn.name)} ${describe(expected)}${suffix(caseSensitivity)}"
                override val negatedDescription: String get() =
                        "${identifierToNegatedDescription(fn.name)} ${describe(expected)}${suffix(caseSensitivity)}"
                override fun invoke(actual: CharSequence) =
                        match(fn(actual, expected, ignoreCase)) { "was ${describe(actual)}" }
            }
        }

        /**
         * Convert a String predicate to a case sensitive [StringMatcher].
         *
         * The predicate must have the signature `<T> (CharSequence, T, Boolean) -> Boolean`, where the final Boolean
         * argument indicates case sensitivity.
         */
        operator fun <T> invoke(fn: KFunction3<CharSequence, T, Boolean, Boolean>, expected: T) =
                invoke(fn, expected, CaseSensitivity.CaseSensitive)
    }

    /**
     * Should the match ignore case (be case insensitive)?
     */
    protected val ignoreCase: Boolean get() = caseSensitivity == CaseSensitivity.CaseInsensitive

    /**
     * A suffix to add to the description of a string matcher to indicate case sensitivity.
     */
    protected fun suffix(s: CaseSensitivity) = when (s) {
        CaseSensitivity.CaseSensitive -> ""
        CaseSensitivity.CaseInsensitive -> " (case insensitive)"
    }
}

/**
 * Returns a case sensitive version of a case insensitive [StringMatcher].
 */
fun StringMatcher<CaseSensitivity.CaseInsensitive>.caseSensitive() = withCaseSensitivity(CaseSensitivity.CaseSensitive)

/**
 * Returns a case insensitive version of a case sensitive [StringMatcher].
 */
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
@JvmField
val isBlank = Matcher(CharSequence::isBlank)

/**
 * Matches a nullable char sequence if it is either `null` or empty or consists solely of whitespace characters.
 */
@JvmField
val isNullOrBlank = Matcher(CharSequence::isNullOrBlank)

/**
 * Matches a char sequence if it is empty (contains no characters).
 */
@JvmField
val isEmptyString = Matcher(CharSequence::isEmpty)

/**
 * Matches a char sequence if it is either `null` or empty (contains no characters).
 */
@JvmField
val isNullOrEmptyString = Matcher(CharSequence::isNullOrEmpty)
