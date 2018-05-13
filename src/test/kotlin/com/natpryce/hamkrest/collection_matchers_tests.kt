package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assert
import org.junit.Test


class Contains {
    @Test
    fun contains_any() {
        assert.that(listOf(1, 2, 3, 4), anyElement(equalTo(3)))
        assert.that(listOf(1, 2, 3, 4), anyElement(greaterThanOrEqualTo(4)))
        assert.that(listOf(), !anyElement(anything))
    }
    
    @Test
    fun contains_element() {
        assert.that(listOf(1, 2, 3, 4), hasElement(1))
        assert.that(listOf(1, 2, 3, 4), hasElement(2))
        assert.that(listOf(1, 2, 3, 4), !hasElement(0))
    }

    @Test
    fun empty_sequence_never_contains_any() {
        assert.that(emptyList(), !anyElement(anything))
    }

    @Test
    fun contains_any_can_be_passed_a_function_reference() {
        assert.that(listOf("1", "2", " "), anyElement(String::isBlank))
    }

    @Test
    fun contains_all() {
        assert.that(listOf(1), allElements(equalTo(1)))
        assert.that(listOf(1, 2, 3, 4), allElements(greaterThan(0)))
        assert.that(listOf(1, 2, 3, 4), !allElements(equalTo(1)))
    }

    @Test
    fun empty_sequence_always_contains_all() {
        assert.that(emptyList(), allElements(anything))
    }

    @Test
    fun contains_all_can_be_passed_a_function_reference() {
        assert.that(listOf("1", "2", " "), !allElements(String::isBlank))
    }
}

class IsIn {
    @Test
    fun is_in_collection() {
        assert.that(1, isIn(listOf(1, 2, 3, 4)))
        assert.that(2, isIn(listOf(1, 2, 3, 4)))
        assert.that(3, isIn(listOf(1, 2, 3, 4)))
        assert.that(4, isIn(listOf(1, 2, 3, 4)))
        
        assert.that(5, !isIn(listOf(1, 2, 3, 4)))
    
        assert.that(3, !isIn(emptyList<Int>()))
        
        assert.that(1, isIn(setOf(1, 2, 3, 4)))
        assert.that(3, isIn(setOf(1, 2, 3, 4)))
    }
    
    @Test
    fun is_in_varargs_treated_as_list() {
        assert.that(1, isIn(1, 2, 3, 4))
        assert.that(2, isIn(1, 2, 3, 4))
    }
}


class CollectionSize {
    @Test
    fun size() {
        val l = listOf(1,2,3)

        assert.that(l, hasSize(equalTo(3)))
        assert.that(l, hasSize(greaterThan(2)))
        assert.that(l, !hasSize(lessThan(3)))
    }

    @Test
    fun integer_size() {
        val l = listOf(1,2,3)

        assert.that(l, hasSize(3))
    }

    @Test
    fun size_description() {
        assert.that(hasSize(greaterThan(3)).description, equalTo("has size that is greater than 3"))
        assert.that((!hasSize(greaterThan(3))).description, equalTo("does not have size that is greater than 3"))
    }

    @Test
    fun integer_size_description() {
        assert.that(hasSize(3).description, equalTo("has size that is equal to 3"))
        assert.that((!hasSize(3)).description, equalTo("does not have size that is equal to 3"))
    }

    @Test
    fun empty() {
        assert.that(emptyList<Int>(), isEmpty)
        assert.that(listOf(1,2,3), !isEmpty)
    }

    @Test
    fun empty_description() {
        assert.that(isEmpty.description, equalTo("is empty"))
        assert.that((!isEmpty).description, equalTo("is not empty"))
    }
}
