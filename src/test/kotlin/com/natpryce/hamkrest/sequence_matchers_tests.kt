package com.natpryce.hamkrest

import org.junit.Test


class Contains {
    @Test
    public fun contains_any() {
        assertThat(listOf(1,2,3,4), containsAny(equalTo(3)))
        assertThat(listOf(1,2,3,4), containsAny(greaterThanOrEqualTo(4)))
        assertThat(listOf(), !containsAny(anything))
    }

    @Test
    public fun empty_sequence_never_contains_any() {
        assertThat(emptyList(), !containsAny(anything))
    }

    @Test
    public fun contains_any_can_be_passed_a_function_reference() {
        assertThat(listOf("1", "2", " "), containsAny(String::isBlank))
    }

    @Test
    public fun contains_all() {
        assertThat(listOf(1), containsAll(equalTo(1)))
        assertThat(listOf(1,2,3,4), containsAll(greaterThan(0)))
        assertThat(listOf(1,2,3,4), !containsAll(equalTo(1)))
    }

    @Test
    public fun empty_sequence_always_contains_all() {
        assertThat(emptyList(), containsAll(anything))
    }

    @Test
    public fun contains_all_can_be_passed_a_function_reference() {
        assertThat(listOf("1", "2", " "), !containsAll(String::isBlank))
    }
}
