package org.hamcrest.kotlin

import org.junit.Test


class ContainsElement {
    @Test
    public fun contains_element() {
        assertThat(listOf(1,2,3,4), contains(equalTo(3)))
        assertThat(listOf(1,2,3,4), contains(greaterThanOrEqualTo(4)))
        assertThat(listOf("1", "2", "three"), !contains(Matcher(String::isBlank)))
    }
}