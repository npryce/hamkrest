package com.natpryce.hamkrest.should

import com.natpryce.hamkrest.Described
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import kotlin.reflect.KFunction1

infix fun <T> T.shouldMatch(matcher: Matcher<T>) {
    assertThat(this, matcher)
}

infix fun <T> T.shouldMatch(f: KFunction1<T, Boolean>) {
    this shouldMatch Matcher(f)
}

infix fun <T> T.describedAs(name: String) = Described(name, this)

infix fun <T> Described<T>.shouldMatch(criteria: Matcher<T>) = assertThat(this.description, this.value, criteria)
infix fun <T> Described<T>.shouldMatch(p: KFunction1<T,Boolean>) = assertThat(this.description, this.value, Matcher(p))
