package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assert
import org.junit.Test

class RegexMatching {
    val pattern = Regex("a(b*)a")

    @Test
    fun entire_string_matches_regex() {
        assert.that("abba", matches(pattern))
        assert.that("aba", matches(pattern))
        assert.that("abbbbbba", matches(pattern))
        assert.that("aha!", !matches(pattern))
        assert.that("yabba dabba doo", !matches(pattern))
    }

    @Test
    fun string_contains_regex() {
        assert.that("abba", contains(pattern))
        assert.that("aba", contains(pattern))
        assert.that("abbbbbba", contains(pattern))
        assert.that("aha!", !matches(pattern))
        assert.that("yabba dabba doo", contains(pattern))
    }

}

class ContainsSubstring {
    @Test
    fun contains_substring() {
        assert.that("qwerty", containsSubstring("qwe"))
        assert.that("qwerty", containsSubstring("wert"))
        assert.that("qwerty", containsSubstring("erty"))
    }

    @Test
    fun contains_substring_can_specify_case_sensitivity() {
        assert.that("qwerty", containsSubstring("WERT").caseInsensitive())
        assert.that("qwerty", !containsSubstring("WERT").caseInsensitive().caseSensitive())
    }

    @Test
    fun description() {
        assert.that(containsSubstring("foo").description, equalTo("contains substring \"foo\""))
        assert.that(containsSubstring("foo").caseInsensitive().description, equalTo("contains substring \"foo\" (case insensitive)"))
    }
}

class StringPrefixAndSuffix {
    @Test
    fun prefix() {
        assert.that("qwerty", startsWith("q"))
        assert.that("qwerty", startsWith("qwe"))
        assert.that("qwerty", !startsWith("Q"))
        assert.that("qwerty", startsWith(""))
    }

    @Test
    fun prefix_can_specify_case_sensitivity() {
        assert.that("qwerty", startsWith("Q").caseInsensitive())
        assert.that("qwerty", !startsWith("Q").caseInsensitive().caseSensitive())
    }

    @Test
    fun suffix() {
        assert.that("qwerty", endsWith("y"))
        assert.that("qwerty", endsWith("rty"))
        assert.that("qwerty", !endsWith("Y"))
        assert.that("qwerty", endsWith(""))
    }

    @Test
    fun suffix_can_specify_case_sensitivity() {
        assert.that("qwerty", endsWith("Y").caseInsensitive())
        assert.that("qwerty", !endsWith("Y").caseInsensitive().caseSensitive())
    }
}

class NullableString {
    @Test
    fun null_or_empty_works_on_null_or_empty_string() {
        assert.that(null, isNullOrEmptyString)
        assert.that("", isNullOrEmptyString)
    }

    @Test
    fun null_or_blank_works_on_null_or_blank_string() {
        assert.that(null, isNullOrBlank)
        assert.that("", isNullOrBlank)
        assert.that(" ", isNullOrBlank)
    }
}

class CaseInsensitiveEquals {
    @Test
    fun equalToIgnoringCase() {
        assert.that("qwerty", equalToIgnoringCase("qwerty"))
        assert.that("qwerty", equalToIgnoringCase("QWERTY"))
        assert.that("QWERTY", equalToIgnoringCase("qwerty"))
        assert.that("qwerty", !equalToIgnoringCase(" qwerty"))
        assert.that("qwerty", !equalToIgnoringCase(null))
        assert.that(null, equalToIgnoringCase(null))
        assert.that(null, !equalToIgnoringCase("qwerty"))
    }

    @Test
    fun description() {
        assert.that(equalToIgnoringCase("foo").description, equalToIgnoringCase("""IS EQUAL (IGNORING CASE) TO "foo""""))
        assert.that(equalToIgnoringCase(null).description, equalToIgnoringCase("""IS EQUAL (IGNORING CASE) TO NULL"""))
    }
}