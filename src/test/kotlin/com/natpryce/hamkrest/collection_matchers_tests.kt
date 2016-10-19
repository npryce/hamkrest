package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test


class Contains {
    @Test
    fun contains_any() {
        assertThat(listOf(1, 2, 3, 4), anyElement(equalTo(3)))
        assertThat(listOf(1, 2, 3, 4), anyElement(greaterThanOrEqualTo(4)))
        assertThat(listOf(), !anyElement(anything))
    }
    
    @Test
    fun contains_element() {
        assertThat(listOf(1, 2, 3, 4), hasElement(1))
        assertThat(listOf(1, 2, 3, 4), hasElement(2))
        assertThat(listOf(1, 2, 3, 4), !hasElement(0))
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

class ContainsAllTest {
    @Test
    fun empty_iterable() {
        assertThat(emptyList<Int>(), containsAll(emptyList()))
        assertThat(emptyList<Int>(), !containsAll(anything))
        assertThat(listOf(anything), !containsAll(emptyList<Any>()))
    }

    @Test
    fun matching_iterables() {
        assertThat(listOf(1), containsAll(1))
        assertThat(listOf(1), containsAll(equalTo(1)))

        assertThat(listOf(1, 1), containsAll(1, 1))
        assertThat(listOf(1, 2), containsAll(1, 2))
        assertThat(listOf(2, 1), containsAll(1, 2))

        assertThat(listOf(1, 2, 2), containsAll(2, 2, 1))
        assertThat(listOf(1, 2, 3), containsAll(2, 3, 1))
    }

    @Test
    fun mismatching_iterables() {
        assertThat("one element is different", listOf(1, 2, 3), !containsAll(4, 2, 1))
        assertThat("disjoint sets", listOf(1, 2), !containsAll(3, 4))
        assertThat("expected is subset", listOf(1, 2, 3), !containsAll(1, 2))
        assertThat("actual is subset", listOf(1, 2), !containsAll(1, 2, 3))
    }

    @Test
    fun different_amount_of_elements() {
        assertThat(listOf(1, 1), !containsAll(1))
        assertThat(listOf(1), !containsAll(1, 1))
        assertThat(listOf(1, 2, 2), !containsAll(1, 1, 2))
    }

    @Test
    fun nested_iterables() {
        assertThat(listOf(emptyList()), containsAllNested(listOf(emptyList<Int>())))
        assertThat(listOf(listOf(1)), containsAllNested(listOf(listOf(1))))

        assertThat(listOf(listOf(1, 2)), containsAllNested(listOf(listOf(2, 1))))
        assertThat(listOf(listOf(1, 1, 2)), containsAllNested(listOf(listOf(2, 1, 1))))

        assertThat(listOf(listOf(1, 2, 3), listOf(1)), containsAllNested(listOf(listOf(1), listOf(3, 2, 1))))
        assertThat(listOf(listOf(1, 2, 4), listOf(1)), containsAllNested(listOf(listOf(1), listOf(4, 2, 1))))
    }

    @Test
    fun description() {
        assertThat(containsAll(1, 2).description, equalTo("iterable with elements [is equal to 1, is equal to 2] in any order"))
    }

    @Test
    fun mismatch_description() {
        assertThat(mismatchDescriptionOf(emptyList(), containsAll(1, 2)), equalTo("no element matches: [is equal to 1, is equal to 2] in []"))
        assertThat(mismatchDescriptionOf(listOf(1, 2, 4), containsAll(1, 2, 3)), equalTo("not matched: 4"))
        assertThat(mismatchDescriptionOf(listOf(1, 2, 2), containsAll(2, 1, 1)), equalTo("not matched: 2"))
        assertThat(mismatchDescriptionOf(listOf(1, 2, 3), containsAll(1, 3)), equalTo("not matched: 2"))
        assertThat(mismatchDescriptionOf(listOf(1, 2), containsAll(1, 2, 3)), equalTo("no element matches: [is equal to 3] in [1, 2]"))
    }

    @Test
    fun clash_with_builtin_containsAll_function() {
        listOf(1, 2).apply {
            assertThat(this, com.natpryce.hamkrest.containsAll(listOf(1, 2)))
            //               ^^^^^^^^^^^^ have to use qualified name to avoid conflict with kotlin.collections.List.containsAll
        }
        listOf(listOf(1, 2)).apply {
            assertThat(this, anyElement(com.natpryce.hamkrest.containsAll(listOf(1, 2))))
            //                          ^^^^^^^^^^^^ have to use qualified name to avoid conflict with kotlin.collections.containsAll
        }
    }

    private fun <T> mismatchDescriptionOf(arg: T, matcher: Matcher<T>): String {
        val matchResult = matcher.invoke(arg)
        assertTrue("Precondition: Matcher should not match item.", matchResult is MatchResult.Mismatch)
        return (matchResult as MatchResult.Mismatch).description
    }
}
