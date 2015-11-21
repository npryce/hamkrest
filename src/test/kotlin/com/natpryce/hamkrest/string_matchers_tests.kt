package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assertThat
import org.junit.Test
import kotlin.text.Regex

class RegexMatching {
    val pattern = Regex("a(b*)a")

    @Test
    fun entire_string_matches_regex() {
        assertThat("abba", matches(pattern))
        assertThat("aba", matches(pattern))
        assertThat("abbbbbba", matches(pattern))
        assertThat("aha!", !matches(pattern))
        assertThat("yabba dabba doo", !matches(pattern))
    }

    @Test
    fun string_contains_regex() {
        assertThat("abba", contains(pattern))
        assertThat("aba", contains(pattern))
        assertThat("abbbbbba", contains(pattern))
        assertThat("aha!", !matches(pattern))
        assertThat("yabba dabba doo", contains(pattern))
    }

}

class ContainsSubstring {
    @Test
    fun contains_substring() {
        assertThat("qwerty", containsSubstring("qwe"))
        assertThat("qwerty", containsSubstring("wert"))
        assertThat("qwerty", containsSubstring("erty"))
    }

    @Test
    fun contains_substring_can_specify_case_sensitivity() {
        assertThat("qwerty", containsSubstring("WERT").caseInsensitive())
        assertThat("qwerty", !containsSubstring("WERT").caseInsensitive().caseSensitive())
    }

    @Test
    fun description() {
        assertThat(containsSubstring("foo").description(), equalTo("contains substring \"foo\""))
        assertThat(containsSubstring("foo").caseInsensitive().description(), equalTo("contains substring \"foo\" (case insensitive)"))
    }
}

class PrefixAndSuffix {
    @Test
    fun prefix() {
        assertThat("qwerty", startsWith("q"))
        assertThat("qwerty", startsWith("qwe"))
        assertThat("qwerty", !startsWith("Q"))
        assertThat("qwerty", startsWith(""))
    }

    @Test
    fun prefix_can_specify_case_sensitivity() {
        assertThat("qwerty", startsWith("Q").caseInsensitive())
        assertThat("qwerty", !startsWith("Q").caseInsensitive().caseSensitive())
    }

    @Test
    fun suffix() {
        assertThat("qwerty", endsWith("y"))
        assertThat("qwerty", endsWith("rty"))
        assertThat("qwerty", !endsWith("Y"))
        assertThat("qwerty", endsWith(""))
    }

    @Test
    fun suffix_can_specify_case_sensitivity() {
        assertThat("qwerty", endsWith("Y").caseInsensitive())
        assertThat("qwerty", !endsWith("Y").caseInsensitive().caseSensitive())
    }

    @Test
    fun type_checker_can_enforce_case_sensitivity() {
        // the following lines will not compile
        // val caseInsensitive : StringMatcher<CaseSensitivity.CaseInsensitive> = endsWith("Y")
        // val caseSensitive: StringMatcher<CaseSensitivity.CaseSensitive> = endsWith("Y").caseInsensitive()

        val caseSensitiveOk: StringMatcher<CaseSensitivity.CaseSensitive> = endsWith("Y")
        val doNotCare: Matcher<String> = endsWith("Y")
        val doNotCareEither: Matcher<String> = endsWith("Y").caseInsensitive()
    }
}