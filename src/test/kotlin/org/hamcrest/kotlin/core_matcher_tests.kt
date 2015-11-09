package org.hamcrest.kotlin;

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

fun assertIsMismatchWithDescription(expectedDescription: String, m: MatchResult) {
    when (m) {
        is MatchResult.Mismatch -> {
            assertEquals(expectedDescription, m.description())
        }
        MatchResult.Match -> fail("unexpected match")
    }
}


class Equality {
    @Test
    public fun equal() {
        assertEquals(MatchResult.Match, equalTo(10)(10))
        assertEquals(MatchResult.Match, equalTo("hello")("hello"))
    }

    @Test
    public fun notEqual() {
        assertIsMismatchWithDescription("20", equalTo(10)(20))
        assertIsMismatchWithDescription("1", equalTo(0)(1))
    }

    @Test
    public fun description() {
        assertEquals("equal to 20", equalTo(20).description())
        assertEquals("equal to \"foo\"", equalTo("foo").description())
        assertEquals("equal to \"hello \\\"nat\\\"\"", equalTo("hello \"nat\"").description())
    }

    @Test
    public fun negation() {
        val m : Matcher<Int> = !equalTo(20)
        assertEquals("not equal to 20", m.description())
        assertEquals("equal to 20", m.negatedDescription())
        assertIsMismatchWithDescription("was equal to 20", m(20));
    }
}


class Nullability {
    @Test
    public fun absence() {
        val m : Matcher<Int?> = absent();

        assertEquals(MatchResult.Match, m(null))
        assertIsMismatchWithDescription("100", m(100))
    }

    @Test
    public fun presence() {
        val m : Matcher<String?> = present(equalTo("xxx"));

        assertEquals(MatchResult.Match, m("xxx"))
        assertIsMismatchWithDescription("null", m(null))
        assertIsMismatchWithDescription("\"yyy\"", m("yyy"))
    }
}