package com.natpryce.hamkrest

private fun Char.isLowerCase() = this == this.toLowerCase()
private fun Char.isUpperCase() = this == this.toUpperCase()
private fun Char.isLetter() = isLowerCase() || isUpperCase()
private fun Char.isDigit() = toString().toIntOrNull() != null
private fun Char.isLetterOrDigit() = isLetter() || isDigit()

internal actual fun isWordPart(c: Char): Boolean = c.isLetterOrDigit()

internal actual fun isWordStart(prev: Char, c: Char): Boolean = when {
    c.isLetter() != prev.isLetter() -> true
    prev.isLowerCase() && c.isUpperCase() -> true
    else -> false
}
