package com.natpryce.hamkrest.assertion

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.matches
import org.junit.Test
import kotlin.text.RegexOption.*

class AssertOutput {
    @Test
    fun produces_ide_friendly() {
        try {
            assert.that("foo", equalTo("bar"))
        } catch (e: AssertionError) {
            assert.that(e.message!!, matches(Regex("expected: .*but was: .*", setOf(MULTILINE, DOT_MATCHES_ALL))))
        }
    }
}