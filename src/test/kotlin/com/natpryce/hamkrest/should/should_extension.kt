package com.natpryce.hamkrest.should

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.Test

class ShouldExtension {

    @org.junit.Test
    fun can_extend_an_object() {
        "Banana" shouldMatch startsWith("Ban")
        42 shouldMatch isWithin(1..99)
    }

    @org.junit.Test
    fun matches_with_function() {
        "Banana" shouldMatch ::isAYellowFruitName
        42 shouldMatch ::isTheAnswer
    }

    @Test
    fun matches_subtypes() {
        listOf(1, 2, 3, 4) shouldMatch containsAny(equalTo(3))
    }

    @Test
    fun can_name_asserted_value() {
        try {
            42 describedAs "bob" shouldMatch equalTo(63)
        }
        catch (e: AssertionError) {
            assertThat(e.message, present(startsWith("bob: ")))
        }
    }

    @Test
    fun is_type_safe() {
         // 42 shouldMatch startsWith("Ban") // Doesn't compile
         // Integer(42) shouldMatch startsWith("Ban") // Doesn't compile
    }
}

private fun isAYellowFruitName(name: String) = name.toLowerCase() in listOf("banana", "lemon", "hippophae")
private fun isTheAnswer(a: Int) = a == 42



