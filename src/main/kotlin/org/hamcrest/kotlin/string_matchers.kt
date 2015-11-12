package org.hamcrest.kotlin

import kotlin.text.Regex

fun contains(r: Regex) : Matcher<String> = Matcher(CharSequence::contains, r)
fun matches(r: Regex) : Matcher<String> = Matcher(String::matches, r)

private fun startsWith(s: String, prefix: String) = s.startsWith(prefix, false)
private fun endsWith(s: String, suffix: String) = s.endsWith(suffix, false)

val startsWith = Matcher(::startsWith)
val endsWith = Matcher(::endsWith)
