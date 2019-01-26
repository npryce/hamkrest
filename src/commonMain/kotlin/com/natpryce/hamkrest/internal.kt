package com.natpryce.hamkrest

import kotlin.text.*

internal fun match(comparisonResult: Boolean, describeMismatch: () -> String): MatchResult =
        if (comparisonResult) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch(describeMismatch())
        }

internal fun identifierToDescription(id: String) = identifierToWords(id).joinToString(" ")

internal fun identifierToNegatedDescription(id: String): String {
    val words = identifierToWords(id)
    val first = words[0]
    val rest = words.drop(1).joinToString(" ")

    return when (first) {
        "is" -> "is not ${rest}"
        "has" -> "does not have ${rest}"
        else -> "not ${first} ${rest}"
    }
}

fun identifierToWords(s: String): List<String> {
    val words = mutableListOf<String>()
    var buf = StringBuilder()

    for ((prev, c) in (s[0] + s).zip(s)) {
        if (isWordStart(prev, c)) {
            if (buf.isNotEmpty()) {
                words.add(buf.toString())
                buf = StringBuilder()
            }
        }
        
        if (isWordPart(c)) {
            buf.append(c.toLowerCase())
        }
    }

    words.add(buf.toString())

    return words
}

expect internal fun isWordPart(c: Char): Boolean

expect internal fun isWordStart(prev: Char, c: Char): Boolean
