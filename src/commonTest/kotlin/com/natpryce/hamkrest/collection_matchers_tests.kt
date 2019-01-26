package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assertThat
import kotlin.test.Test

class Contains {
    @Test
    fun contains_any() {
        assertThat(listOf(1, 2, 3, 4), anyElement(equalTo(3)))
        assertThat(listOf(1, 2, 3, 4), anyElement(greaterThanOrEqualTo(4)))
        assertThat(emptyList<Int>(), !anyElement(anything))
    }
    
    @Test
    fun contains_element() {
        assertThat(listOf(1, 2, 3, 4), hasElement(1))
        assertThat(listOf(1, 2, 3, 4), hasElement(2))
        assertThat(listOf(1, 2, 3, 4), !hasElement(0))
    }

    @Test
    fun empty_sequence_never_meets_any_elements() {
        assertThat(emptyList<Int>(), !anyElement(anything))
    }

    @Test
    fun any_elements_can_be_passed_a_function_reference() {
        assertThat(listOf("1", "2", " "), anyElement(String::isBlank))
    }

    @Test
    fun all_elements() {
        assertThat(listOf(1), allElements(equalTo(1)))
        assertThat(listOf(1, 2, 3, 4), allElements(greaterThan(0)))
        assertThat(listOf(1, 2, 3, 4), !allElements(equalTo(1)))
    }

    @Test
    fun empty_sequence_always_meets_all_elements() {
        assertThat(emptyList<Any>(), allElements(anything))
    }

    @Test
    fun all_elements_can_be_passed_a_function_reference() {
        assertThat(listOf("1", "2", " "), !allElements(String::isBlank))
    }
}

class IsIn {
    @Test
    fun is_in_collection() {
        assertThat(1, isIn(listOf(1, 2, 3, 4)))
        assertThat(2, isIn(listOf(1, 2, 3, 4)))
        assertThat(3, isIn(listOf(1, 2, 3, 4)))
        assertThat(4, isIn(listOf(1, 2, 3, 4)))
        
        assertThat(5, !isIn(listOf(1, 2, 3, 4)))
    
        assertThat(3, !isIn(emptyList<Int>()))
        
        assertThat(1, isIn(setOf(1, 2, 3, 4)))
        assertThat(3, isIn(setOf(1, 2, 3, 4)))
    }
    
    @Test
    fun is_in_varargs_treated_as_list() {
        assertThat(1, isIn(1, 2, 3, 4))
        assertThat(2, isIn(1, 2, 3, 4))
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
        assertThat(hasSize(greaterThan(3)).description, equalTo("has size that is greater than 3"))
        assertThat((!hasSize(greaterThan(3))).description, equalTo("does not have size that is greater than 3"))
    }

    @Test
    fun empty() {
        assertThat(emptyList<Int>(), isEmpty)
        assertThat(listOf(1,2,3), !isEmpty)
    }

    @Test
    fun empty_description() {
        assertThat(isEmpty.description, equalTo("is empty"))
        assertThat((!isEmpty).description, equalTo("is not empty"))
    }
}
