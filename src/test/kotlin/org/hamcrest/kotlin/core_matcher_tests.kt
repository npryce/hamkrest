package org.hamcrest.kotlin;

import org.junit.Test
import kotlin.reflect.KFunction1
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
        assertIsMismatchWithDescription("was 20", equalTo(10)(20))
        assertIsMismatchWithDescription("was 1", equalTo(0)(1))
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
        assertIsMismatchWithDescription("was 100", m(100))
    }

    @Test
    public fun presence() {
        val m : Matcher<String?> = present(equalTo("xxx"));

        assertEquals(MatchResult.Match, m("xxx"))
        assertIsMismatchWithDescription("was null", m(null))
        assertIsMismatchWithDescription("was \"yyy\"", m("yyy"))
    }
}

class Downcasting {
    val m : Matcher<Any> = isA<String>(equalTo("bob"))

    @Test
    public fun wrongType() {
        assertIsMismatchWithDescription("was a kotlin.Double", m(10.0))
    }

    @Test
    public fun correctTypeAndDowncastMatch() {
        assertEquals(MatchResult.Match, m("bob"))
    }

    @Test
    public fun correctTypeAndDowncastMismatch() {
        assertIsMismatchWithDescription("was \"alice\"", m("alice"))
    }
}

fun isGreat(actual : String) : Boolean = actual == "great"

class FromFunctions {
    @Test
    public fun createMatcherFromNamedFunctionReferenceByExtensionMethod() {
        val m = ::isGreat.asMatcher()

        assertEquals("isGreat", m.description())

        assertEquals(MatchResult.Match, m("great"))
        assertIsMismatchWithDescription("was \"grand\"", m("grand"))
    }
}