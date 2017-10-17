package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.Test
import java.nio.file.Paths

class DelimitingValuesInStrings {
    @Test
    fun quotes_around_string_values_and_escaped_within() {
        assert.that(describe("hello, \"bob\""), equalTo("\"hello, \\\"bob\\\"\""))
    }

    @Test
    fun pairs() {
        assert.that(describe(Pair("x", "y")), equalTo("(\"x\", \"y\")"))
    }

    @Test
    fun triples() {
        assert.that(describe(Triple("x", "y", "z")), equalTo("(\"x\", \"y\", \"z\")"))
    }

    @Test
    fun iterables() {
        assert.that(describe(listOf(1, 2)), equalTo("[1, 2]"))
        assert.that(describe(listOf("1", "2")), equalTo("""["1", "2"]"""))
    }

    @Test
    fun ranges() {
        assert.that(describe(1..8), equalTo("1..8"))
    }

    @Test
    fun maps() {
        assert.that(describe(mapOf("a" to 1, "b" to 2)), equalTo("""{"a":1, "b":2}"""))
    }

    @Test
    fun describable_objects() {
        val d = object : SelfDescribing {
            override val description = "d"
        }

        assert.that(describe(d), equalTo("d"))
    }
    
    @Test
    fun file_paths() {
        val path = Paths.get("/foo/bar/baz")
        assertThat(describe(path), equalTo("/foo/bar/baz"))
    }
}
