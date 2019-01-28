package com.natpryce.hamkrest

import kotlin.test.assertEquals
import kotlin.test.fail

fun assertMismatchWithDescription(expectedDescription: String, m: MatchResult) {
    when (m) {
        Match -> fail("unexpected match")
        is Mismatch -> {
            assertEquals(expectedDescription, m.description)
        }
    }
}

fun assertMismatch(m: MatchResult) {
     if (m == Match) fail("unexpected match")
}

fun assertMatch(result: MatchResult) {
    assertEquals(Match, result)
}