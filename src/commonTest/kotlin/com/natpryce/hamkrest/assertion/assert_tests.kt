package com.natpryce.hamkrest.assertion

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.matches
import com.natpryce.hamkrest.present
import kotlin.text.RegexOption.MULTILINE
import kotlin.test.Test

class AssertOutput {
    @Test
    fun produces_ide_friendly_error_message() {
        try {
            assertThat("foo", equalTo("bar"))
        } catch (e: AssertionError) {
            assertThat(e.message, present(matches(Regex("expected: (.|\\n)*but was: (.|\\n)*", setOf(MULTILINE)))))
        }
    }
}
