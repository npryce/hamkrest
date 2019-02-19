package com.natpryce.hamkrest
import kotlin.text.isLowerCase
import kotlin.text.isUpperCase
import kotlin.text.isLetter

internal actual fun isWordPart(c: Char): Boolean = c.isLetterOrDigit()
internal actual fun Char.isLetter(): Boolean = isLetter()
internal actual fun Char.isLowerCase(): Boolean = isLowerCase()
internal actual fun Char.isUpperCase(): Boolean = isUpperCase()
