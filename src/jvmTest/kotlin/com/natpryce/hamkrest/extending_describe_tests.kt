package com.natpryce.hamkrest

import com.natpryce.hamkrest.assertion.assertThat
import kotlin.test.Test

class NeedsCustomDescription

class ExampleDescribeExtension : ValueDescription {
    override fun describe(v: Any?) =
        when (v) {
            is NeedsCustomDescription -> "the-custom-description"
            else -> null
        }
}

class ExtendingDescribeWithAService {
    @Test
    fun can_extend_describe_function_with_jvm_services() {
        assertThat(describe(NeedsCustomDescription()), equalTo("the-custom-description"))
    }
}
