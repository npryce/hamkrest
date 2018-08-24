package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assert
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AnythingAndNothing {
    @Test
    fun any_value_is_anything() {
        assertMatch(anything("a string"))
        assertMatch(anything(99))
        assertMatch(anything(99.0))
        assertMatch(anything(true))
        assertMatch(anything(false))
        assertMatch(anything(object {}))
    }
    
    @Test
    fun null_is_anything() {
        assertMatch(anything(null))
    }
    
    @Test
    fun any_value_is_not_nothing() {
        assertMismatch(nothing("a string"))
        assertMismatch(nothing(99))
        assertMismatch(nothing(99.0))
        assertMismatch(nothing(true))
        assertMismatch(nothing(false))
        assertMismatch(nothing(object {}))
    }
    
    @Test
    fun null_is_not_nothing() {
        assertMismatch(nothing(null))
    }
}

class Equality {
    @Test
    fun equal() {
        assertMatch((equalTo(10))(10))
        assertMatch(equalTo("hello")("hello"))
    }

    @Test
    fun not_equal() {
        assertMismatchWithDescription("was: 20", equalTo(10)(20))
        assertMismatchWithDescription("was: 1", equalTo(0)(1))
    }
    
    @Test
    fun null_comparison() {
        assertMatch((equalTo(null)(null)))
        
        val actual: String? = null
        assertMatch(equalTo<String?>(null)(actual))
        
        val expected: String? = null
        assertMatch(equalTo(expected)(null))
        
        assertMismatchWithDescription("was: null", equalTo("foo")(actual))
    }

    @Test
    fun description() {
        assertEquals("is equal to 20", equalTo(20).description)
        assertEquals("is equal to \"foo\"", equalTo("foo").description)
        assertEquals("is equal to \"hello \\\"nat\\\"\"", equalTo("hello \"nat\"").description)
        assertEquals("is equal to null", equalTo(null).description)
    }
}

class SameInstance {
    @Test
    fun same() {
        val s = "hello"
        val t = (s + "x").substring(0,s.length)

        assertTrue { s !== t }
        assertMatch(sameInstance(s)(s))
        assertMismatchWithDescription("was: \"hello\"", sameInstance(s)(t))
    }

    @Test
    fun description() {
        assertEquals("is same instance as \"x\"", sameInstance("x").description)
        assertEquals("is not same instance as \"y\"", (!sameInstance("y")).description)
    }
}

class Nullability {
    @Test
    fun absence() {
        val m : Matcher<Int?> = absent()

        assertMatch(m(null))
        assertMismatchWithDescription("was: 100", m(100))
    }

    @Test
    fun presence() {
        val m : Matcher<String?> = present()

        assertMatch(m("xxx"))
        assertMismatchWithDescription("was: null", m(null))
    }

    @Test
    fun presence_and_constraint() {
        val m : Matcher<String?> = present(equalTo("xxx"))

        assertMatch(m("xxx"))
        assertMismatchWithDescription("was: null", m(null))
        assertMismatchWithDescription("was: \"yyy\"", m("yyy"))
    }
}

class Downcasting {
    val m : Matcher<Any> = isA<String>(equalTo("bob"))

    @Test
    fun wrong_type() {
        assertMismatchWithDescription("was: a kotlin.Double", m(10.0))
    }

    @Test
    fun correct_type_and_downcast_mismatch() {
        assertMismatchWithDescription("was: \"alice\"", m("alice"))
    }

    @Test
    fun correct_type_and_downcast_match() {
        assertMatch(m("bob"))
    }

    @Test
    fun type_match() {
        assertMatch(isA<String>()("bob"))
        assertMismatchWithDescription("was: a kotlin.Int", isA<String>()(1))
    }
}

class Comparables {
    @Test
    fun order_comparisons() {
        assert.that(10, greaterThan(5))
        assert.that(10, greaterThanOrEqualTo(5))
        assert.that(10, greaterThanOrEqualTo(10))
        assert.that(10, !greaterThanOrEqualTo(50))

        assert.that(10, lessThan(20))
        assert.that(10, lessThanOrEqualTo(20))
        assert.that(10, lessThanOrEqualTo(10))
        assert.that(10, !lessThanOrEqualTo(5))
    }

    @Test
    fun within_range() {
        assert.that(1, isWithin(1..20))
        assert.that(10, isWithin(1..20))
        assert.that(20, isWithin(1..20))
        assert.that(0, !isWithin(1..20))
        assert.that(21, !isWithin(1..20))

        assert.that(isWithin(1..20).description, equalTo("is within 1..20"))
        assert.that((!isWithin(1..20)).description, equalTo("is not within 1..20"))
    }
}

class ExampleException(message: String) : Exception(message)
class DifferentException(message: String) : Exception(message)

class Throwing {
    @Test
    fun matches_block_that_throws_specific_exception() {
        assert.that({throw ExampleException("testing")}, throws<ExampleException>())
    }

    @Test
    fun matches_block_that_throws_specific_exception_and_state() {
        assert.that({throw ExampleException("testing")},
                throws<ExampleException>(has(Exception::message, present(equalTo("testing")))))
    }

    @Test
    fun does_not_match_block_that_does_not_throw() {
        assert.that({}, ! throws<ExampleException>())
    }

    @Test
    fun does_not_match_block_that_throws_different_exception() {
        assert.that({throw DifferentException("xxx")}, !throws<ExampleException>())
    }
}

class Converting {

    data class HasProperty(val hasAProperty: Boolean)

    @Test
    fun create_from_a_property() {
        assert.that(HasProperty(true), Matcher(HasProperty::hasAProperty))
    }
}