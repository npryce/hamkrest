package com.natpryce.hamkrest

import org.junit.Test

class ShouldExtension {

    @Test
    fun can_wrap_an_object() {
        "Banana".shouldMatch(startsWith("Ban"))
    }

    @Test
    fun can_wrap_a_primitive() {
        42.shouldMatch(isWithin(1..99))
    }

    @Test
    fun matches_with_function() {
        "Banana".shouldMatch(::isAYellowFruitName)
    }

    @Test
    fun matches_primitive_with_function() {
        42.shouldMatch(::isTheAnswer)
    }

    @Test
    fun matches_subtypes() {
        listOf(1, 2, 3, 4).shouldMatch(containsAny(equalTo(3)))
    }

    @Test
    fun is_type_safe() {
        // 42.shouldMatch(startsWith("Ban")) Doesn't compile
        // Integer(42).shouldMatch(startsWith("Ban")) Doesn't compile
    }
}

private fun isAYellowFruitName(name: String) = name.toLowerCase() in listOf("banana", "lemon", "hippophae")
private fun isTheAnswer(a: Int) = a == 42



