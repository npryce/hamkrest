package com.natpryce.hamkrest.should

import com.natpryce.hamkrest.anyElement
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isWithin
import com.natpryce.hamkrest.present
import com.natpryce.hamkrest.startsWith
import org.junit.Test

class ShouldExtension {

    @Test
    fun can_extend_an_object() {
        "Banana" shouldMatch startsWith("Ban")
        42 shouldMatch isWithin(1..99)
    }

    @Test
    fun matches_with_function() {
        "Banana" shouldMatch ::isAYellowFruitName
        42 shouldMatch ::isTheAnswer
    }

    @Test
    fun matches_subtypes() {
        listOf(1, 2, 3, 4) shouldMatch anyElement(equalTo(3))
    }

    @Test
    fun can_name_asserted_value() {
        try {
            42 describedAs "bob" shouldMatch equalTo(63)
        } catch (e: AssertionError) {
            assertThat(e.message, present(startsWith("bob: ")))
        }
    }

    @Test
    fun is_type_safe() {
        // 42 shouldMatch startsWith("Ban") // Doesn't compile
        // Integer(42) shouldMatch startsWith("Ban") // Doesn't compile
    }


    @Test
    fun not_extend_an_object() {
        "Banana" shouldNotMatch startsWith("App")
        42 shouldNotMatch isWithin(1..10)
    }

    @Test
    fun not_matches_with_function() {
        "Apple" shouldNotMatch ::isAYellowFruitName
        666 shouldNotMatch ::isTheAnswer
    }

    @Test
    fun not_matches_subtypes() {
        listOf(1, 2, 4) shouldNotMatch anyElement(equalTo(3))
    }
}

private fun isAYellowFruitName(name: String) = name.toLowerCase() in listOf("banana", "lemon", "hippophae")
private fun isTheAnswer(a: Int) = a == 42



