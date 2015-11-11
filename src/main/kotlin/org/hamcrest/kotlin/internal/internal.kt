package org.hamcrest.kotlin.internal

import org.hamcrest.kotlin.MatchResult
import java.util.*

public fun match(comparisonResult: Boolean, describeMismatch: () -> String): MatchResult =
        if (comparisonResult) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch(describeMismatch())
        }

public fun identifierToDescription(s: String) : String = identifierToWords(s).joinToString(" ")

public fun identifierToWords(s: String): List<String> {
    val words: MutableList<String> = ArrayList()
    val buf = StringBuilder()

    for ((prev, c) in (s[0] + s).zip(s)) {
        if (isWordStart(prev, c)) {
            if (buf.length > 0) {
                words.add(buf.toString())
                buf.setLength(0)
            }
        }

        if (isWordPart(c)) {
            buf.append(c.toLowerCase())
        }
    }

    words.add(buf.toString())

    return words
}

fun isWordPart(c: Char): Boolean = c.isLetterOrDigit()

fun isWordStart(prev: Char, c: Char): Boolean = when {
    c.isLetter() != prev.isLetter() -> true
    prev.isLowerCase() && c.isUpperCase() -> true
    else -> false
}
