package com.natpryce.hamkrest.assertion

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.matches
import org.junit.Test
import kotlin.text.RegexOption.DOT_MATCHES_ALL
import kotlin.text.RegexOption.MULTILINE

class AssertOutput {
    @Test
    fun produces_ide_friendly_error_message() {
        try {
            assertThat("foo", equalTo("bar"))
        } catch (e: AssertionError) {
            assertThat(e.message!!, matches(Regex("expected: .*but was: .*", setOf(MULTILINE, DOT_MATCHES_ALL))))
        }
    }
}
