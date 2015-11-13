package com.natpryce.hamkrest

import org.junit.Test

class DelimitingValuesInStrings {
    @Test
    fun escapes_quotes_in_strings() {
        assertThat(delimit("hello, \"bob\""), equalTo("\"hello, \\\"bob\\\"\""))
    }

    @Test
    fun delimits_elements_of_pairs() {
        assertThat(delimit(Pair("x", "y")), equalTo("(\"x\", \"y\")"))
    }
}