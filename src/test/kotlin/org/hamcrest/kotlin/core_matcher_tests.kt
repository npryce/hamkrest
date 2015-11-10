package org.hamcrest.kotlin;

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

fun assertMismatchWithDescription(expectedDescription: String, m: MatchResult) {
    when (m) {
        is MatchResult.Mismatch -> {
            assertEquals(expectedDescription, m.description())
        }
        MatchResult.Match -> fail("unexpected match")
    }
}

private fun assertMatch(result: MatchResult) {
    assertEquals(MatchResult.Match, result)
}


class Equality {
    @Test
    public fun equal() {
        assertMatch(equalTo(10)(10))
        assertMatch(equalTo("hello")("hello"))
    }

    @Test
    public fun notEqual() {
        assertMismatchWithDescription("was 20", equalTo(10)(20))
        assertMismatchWithDescription("was 1", equalTo(0)(1))
    }

    @Test
    public fun description() {
        assertEquals("equal to 20", equalTo(20).description())
        assertEquals("equal to \"foo\"", equalTo("foo").description())
        assertEquals("equal to \"hello \\\"nat\\\"\"", equalTo("hello \"nat\"").description())
    }
}

class LogicalConnectives {
    @Test
    public fun negation() {
        val m : Matcher<Int> = !equalTo(20)
        assertEquals("not equal to 20", m.description())
        assertEquals("equal to 20", m.negatedDescription())
        assertMismatchWithDescription("was equal to 20", m(20));
    }

    @Test
    public fun disjunction() {
        val m = equalTo(10) or equalTo(20)

        assertMatch(m(10))
        assertMatch(m(20))
        assertMismatchWithDescription("was 11", m(11))

        assertThat(m.description(), equalTo("equal to 10 or equal to 20"))
    }

    @Test
    public fun conjunction() {
        fun greaterThan10(i : Int): Boolean = i > 10
        fun lessThan20(i : Int): Boolean = i < 20

        val m = ::greaterThan10.asMatcher() and ::lessThan20.asMatcher()

        assertMatch(m(11))
        assertMatch(m(19))
        assertMismatchWithDescription("was 10", m(10))
        assertMismatchWithDescription("was 20", m(20))

        assertThat(m.description(), equalTo("greaterThan10 and lessThan20"))
    }
}

class Nullability {
    @Test
    public fun absence() {
        val m : Matcher<Int?> = absent();

        assertMatch(m(null))
        assertMismatchWithDescription("was 100", m(100))
    }

    @Test
    public fun presence() {
        val m : Matcher<String?> = present(equalTo("xxx"));

        assertMatch(m("xxx"))
        assertMismatchWithDescription("was null", m(null))
        assertMismatchWithDescription("was \"yyy\"", m("yyy"))
    }
}

class Downcasting {
    val m : Matcher<Any> = cast<String>(equalTo("bob"))

    @Test
    public fun wrongType() {
        assertMismatchWithDescription("was a kotlin.Double", m(10.0))
    }

    @Test
    public fun correctTypeAndDowncastMatch() {
        assertMatch(m("bob"))
    }

    @Test
    public fun correctTypeAndDowncastMismatch() {
        assertMismatchWithDescription("was \"alice\"", m("alice"))
    }
}

fun isGreat(actual : String) : Boolean = actual == "great"

class FromFunction {
    @Test
    public fun createMatcherFromNamedFunctionReferenceByExtensionMethod() {
        val m = ::isGreat.asMatcher()

        assertEquals("isGreat", m.description())

        assertMatch(m("great"))
        assertMismatchWithDescription("was \"grand\"", m("grand"))
    }

}
