package com.natpryce.hamkrest

internal actual fun isWordPart(c: Char): Boolean = c.isLetterOrDigit()

internal actual fun isWordStart(prev: Char, c: Char): Boolean = when {
    c.isLetter() != prev.isLetter() -> true
    prev.isLowerCase() && c.isUpperCase() -> true
    else -> false
}
