package org.hamcrest.kotlin

import kotlin.text.Regex

fun contains(r: Regex) : Matcher<String> = Matcher(CharSequence::contains, r)
fun matches(r: Regex) : Matcher<String> = Matcher(String::matches, r)

private fun _startsWith(s: String, prefix: String) = s.startsWith(prefix, false)
private fun _endsWith(s: String, suffix: String) = s.endsWith(suffix, false)

val startsWith = Matcher(::_startsWith)
val endsWith = Matcher(::_endsWith)
