package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assert
import org.junit.Test


class AnyPair {
    @Test
    fun contains_any_pair() {
        assert.that(mapOf(1 to 'a', 2 to 'b', 3 to 'c'), anyPair(equalTo(2 to 'b')))
        assert.that(mapOf(1 to 'a', 2 to 'b', 3 to 'c'), anyPair(Matcher("anotherMatcher", { it.second == 'a' })))
        assert.that(mapOf('a' to 'b'), !anyPair(equalTo('a' to 'c')))
    }

    @Test
    fun contains_any_pair_works_with_subclasses() {
        assert.that(mapOf<Char, Number>('a' to 1, 'b' to 2.0), anyPair(equalTo('b' to 2.0)))
        assert.that(mapOf<Number, Char>(2 to 'a', 2.0 to 'b'), anyPair(equalTo(2.0 to 'b')))
    }
}
