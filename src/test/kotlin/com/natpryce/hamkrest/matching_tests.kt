package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.assertion.that
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class LogicalConnectives {
    @Test
    fun negation() {
        val m: Matcher<Int> = !equalTo(20)

        assertEquals("is not equal to 20", m.description)
        assertEquals("is equal to 20", (!m).description)

        assertMismatchWithDescription("was: 20", m(20))
    }

    @Test
    fun negation_of_negation_is_identity() {
        val m: Matcher<Int> = equalTo(20)

        assertTrue {!(!m) === m}
    }

    @Test
    fun disjunction() {
        val m = equalTo(10) or equalTo(20)

        assertMatch(m(10))
        assertMatch(m(20))
        assertMismatchWithDescription("was: 11", m(11))

        assert.that(m.description, equalTo("is equal to 10 or is equal to 20"))
    }

    @Test
    fun conjunction() {
        val m = greaterThan(10) and lessThan(20)

        assertMatch(m(11))
        assertMatch(m(19))
        assertMismatchWithDescription("was: 10", m(10))
        assertMismatchWithDescription("was: 20", m(20))

        assert.that(m.description, equalTo("is greater than 10 and is less than 20"))
    }
}


class FunctionToMatcher {
    @Test
    fun create_matcher_from_named_function_reference() {
        val isBlank = Matcher(String::isBlank)

        assertEquals("is blank", isBlank.description)

        assertMatch(isBlank(""))
        assertMatch(isBlank(" "))
        assertMismatchWithDescription("was: \"wrong\"", isBlank("wrong"))
    }

    @Test
    fun create_matcher_from_binary_function_reference_and_second_parameter() {
        fun String.hasLength(n: Int): Boolean = this.length == n

        val hasLength4 = Matcher(String::hasLength, 4)

        assertEquals("has length 4", hasLength4.description)

        assertMatch(hasLength4("yeah"))
        assertMatch(hasLength4("nope"))
        assertMismatchWithDescription("was: \"wrong\"", hasLength4("wrong"))
    }

    @Test
    fun create_matcher_factory_from_binary_function_reference() {
        fun String.hasLength(n: Int): Boolean = this.length == n
        val hasLength = Matcher(String::hasLength)

        assertEquals("has length 4", hasLength(4).description)
        assertEquals("has length 6", hasLength(6).description)

        assertMatch(hasLength(3)("yes"))
        assertMatch(hasLength(4)("yeah"))
    }

    @Test
    fun can_pass_function_references_to_assert_that() {
        assert.that("  ", String::isBlank)
    }
}

open class Fruit(val ripeness: Double)
class Apple(ripeness: Double, val forCooking: Boolean) : Fruit(ripeness)
class Orange(ripeness: Double, val segmentCount: Int) : Fruit(ripeness)


fun isRipe(f: Fruit): Boolean = f.ripeness >= 0.5
fun canBeShared(o: Orange): Boolean = o.segmentCount % 2 == 0
fun isCookingApple(a: Apple): Boolean = a.forCooking

class Subtyping {
    @Test
    fun the_kotlin_type_system_makes_an_old_java_programmer_very_happy() {
        val mA: Matcher<Apple> = ::isRipe and ::isCookingApple
        val mO: Matcher<Orange> = ::isRipe and ::canBeShared

        assertMatch(mA(Apple(ripeness = 1.0, forCooking = true)))
        assertMatch(mO(Orange(ripeness = 1.0, segmentCount = 4)))
    }
}


class Projections {
    @Test
    fun can_match_projection_by_property() {
        val isLongEnough = has(String::length, greaterThan(4))

        assert.that("12345", isLongEnough)
        assert.that("1234", !isLongEnough)

        assert.that(isLongEnough.description, equalTo("has length that is greater than 4"))
    }

    @Test
    fun can_match_projection_by_function() {
        assert.that("12345", has(::toInt, equalTo(12345)))
        assert.that("1234", !has(::toInt, equalTo(12345)))
    }

    @Test
    fun description_of_projection_is_human_readableified() {
        assert.that(has(::toInt, equalTo(12345)).description, equalTo(
                "has to int that is equal to 12345"))
    }
}

fun toInt(s: String) : Int = s.toInt()