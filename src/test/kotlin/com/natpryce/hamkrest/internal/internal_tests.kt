package com.natpryce.hamkrest.internal

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.describe
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class Utilities {
    @Test
    public fun splits_identifier_into_words() {
        val cases = listOf(
                "identifier" to listOf("identifier"),
                "an_identifier" to listOf("an", "identifier"),
                "anIdentifier" to listOf("an", "identifier"),
                "farenheit451" to listOf("farenheit", "451"),
                "i_got_99_problems" to listOf("i", "got", "99", "problems")
        )

        for ((identifier, words) in cases) {
            assertThat("${describe(identifier)} to words", identifierToWords(identifier), equalTo(words))
        }
    }
}