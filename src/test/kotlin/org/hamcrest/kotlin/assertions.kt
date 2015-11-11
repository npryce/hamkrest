package org.hamcrest.kotlin

import kotlin.test.assertEquals
import kotlin.test.fail

fun assertMismatchWithDescription(expectedDescription: String, m: MatchResult) {
    when (m) {
        is MatchResult.Mismatch -> {
            assertEquals(expectedDescription, m.description())
        }
        MatchResult.Match -> fail("unexpected match")
    }
}

fun assertMatch(result: MatchResult) {
    assertEquals(MatchResult.Match, result)
}