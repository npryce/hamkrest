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

class PrefixAndSuffix {
    @Test
    fun prefix() {
        assertThat("qwerty", startsWith("q"))
        assertThat("qwerty", startsWith("qwe"))
        assertThat("qwerty", !startsWith("Q"))
        assertThat("qwerty", startsWith(""))
    }

    @Test
    fun suffix() {
        assertThat("qwerty", endsWith("y"))
        assertThat("qwerty", endsWith("rty"))
        assertThat("qwerty", !endsWith("Y"))
        assertThat("qwerty", endsWith(""))
    }
}