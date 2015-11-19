@file:JvmName("StringMatchers")

package com.natpryce.hamkrest

import kotlin.text.Regex

/**
 * Matches strings that contain the given [Regex].
 *
 * @see [String.contains]
 */
private fun _contains(s : CharSequence, regex: Regex): Boolean = regex.containsMatchIn(s)

fun contains(r: Regex) : Matcher<String> = Matcher(::_contains, r)

/**
 * Matches strings that match the given [Regex].
 *
 * @see [String.matches]
 */
fun matches(r: Regex) : Matcher<String> = Matcher(String::matches, r)

private fun _startsWith(s: String, prefix: String) = s.startsWith(prefix, false)
private fun _endsWith(s: String, suffix: String) = s.endsWith(suffix, false)

/**
 *  Matches strings that start with the given prefix.
 */
val startsWith = Matcher(::_startsWith)

/**
 *  Matches strings that end with the given prefix
 */
val endsWith = Matcher(::_endsWith)
