package com.natpryce.hamkrest

import kotlin.test.assertEquals
import kotlin.test.fail

fun assertMismatchWithDescription(expectedDescription: String, m: MatchResult) {
    when (m) {
        MatchResult.Match -> fail("unexpected match")
        is MatchResult.Mismatch -> {
            assertEquals(expectedDescription, m.description)
        }
    }
}

fun assertMismatch(m: MatchResult) {
     if (m == MatchResult.Match) fail("unexpected match")
}

fun assertMatch(result: MatchResult) {
    assertEquals(MatchResult.Match, result)
}