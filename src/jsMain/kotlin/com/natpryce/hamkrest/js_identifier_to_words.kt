package com.natpryce.hamkrest

internal actual fun Char.isLowerCase() = this != this.toUpperCase()
internal actual fun Char.isUpperCase() = this != this.toLowerCase()
internal actual fun Char.isLetter() = isLowerCase() || isUpperCase()
private fun Char.isDigit() = toString().toIntOrNull() != null
private fun Char.isLetterOrDigit() = isLetter() || isDigit()

internal actual fun isWordPart(c: Char): Boolean = c.isLetterOrDigit()

