package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assertThat
import org.junit.Test


class Contains {
    @Test
    fun contains_any() {
        assertThat(listOf(1, 2, 3, 4), anyElement(equalTo(3)))
        assertThat(listOf(1, 2, 3, 4), anyElement(greaterThanOrEqualTo(4)))
        assertThat(listOf(), !anyElement(anything))
    }

    @Test
    fun empty_sequence_never_contains_any() {
        assertThat(emptyList(), !anyElement(anything))
    }

    @Test
    fun contains_any_can_be_passed_a_function_reference() {
        assertThat(listOf("1", "2", " "), anyElement(String::isBlank))
    }

    @Test
    fun contains_all() {
        assertThat(listOf(1), allElements(equalTo(1)))
        assertThat(listOf(1, 2, 3, 4), allElements(greaterThan(0)))
        assertThat(listOf(1, 2, 3, 4), !allElements(equalTo(1)))
    }

    @Test
    fun empty_sequence_always_contains_all() {
        assertThat(emptyList(), allElements(anything))
    }

    @Test
    fun contains_all_can_be_passed_a_function_reference() {
        assertThat(listOf("1", "2", " "), !allElements(String::isBlank))
    }
}


class CollectionSize {
    @Test
    fun size() {
        val l = listOf(1,2,3)

        assertThat(l, hasSize(equalTo(3)))
        assertThat(l, hasSize(greaterThan(2)))
        assertThat(l, !hasSize(lessThan(3)))
    }

    @Test
    fun size_description() {
        assertThat(hasSize(greaterThan(3)).description(), equalTo("has size that is greater than 3"))
        assertThat((!hasSize(greaterThan(3))).description(), equalTo("does not have size that is greater than 3"))
    }

    @Test
    fun empty() {
        assertThat(emptyList<Int>(), isEmpty)
        assertThat(listOf(1,2,3), !isEmpty)
    }

    @Test
    fun empty_description() {
        assertThat(isEmpty.description(), equalTo("is empty"))
        assertThat((!isEmpty).description(), equalTo("is not empty"))
    }
}
