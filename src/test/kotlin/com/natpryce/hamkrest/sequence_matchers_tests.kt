package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assertThat
import org.junit.Test


class Contains {
    @Test
    public fun contains_any() {
        assertThat(listOf(1, 2, 3, 4), anyElement(equalTo(3)))
        assertThat(listOf(1, 2, 3, 4), anyElement(greaterThanOrEqualTo(4)))
        assertThat(listOf(), !anyElement(anything))
    }

    @Test
    public fun empty_sequence_never_contains_any() {
        assertThat(emptyList(), !anyElement(anything))
    }

    @Test
    public fun contains_any_can_be_passed_a_function_reference() {
        assertThat(listOf("1", "2", " "), anyElement(String::isBlank))
    }

    @Test
    public fun contains_all() {
        assertThat(listOf(1), allElements(equalTo(1)))
        assertThat(listOf(1, 2, 3, 4), allElements(greaterThan(0)))
        assertThat(listOf(1, 2, 3, 4), !allElements(equalTo(1)))
    }

    @Test
    public fun empty_sequence_always_contains_all() {
        assertThat(emptyList(), allElements(anything))
    }

    @Test
    public fun contains_all_can_be_passed_a_function_reference() {
        assertThat(listOf("1", "2", " "), !allElements(String::isBlank))
    }
}
