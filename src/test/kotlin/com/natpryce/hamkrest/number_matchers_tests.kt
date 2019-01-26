package com.natpryce.hamkrest

import kotlin.test.Test
import kotlin.test.assertEquals

class CloseTo {
    @Test
    fun close() {
        assertMatch(closeTo(3.0, 0.1)(3.01))
        assertMatch(closeTo(3.0)(3.0))
        assertMatch(closeTo(3.0f, 0.1f)(3.01f))
        assertMatch(closeTo(3.0f)(3.0f))
    }

    @Test
    fun not_close() {
        assertMismatchWithDescription("a numeric value 4.0 differed by 0.9 more than error 0.1", closeTo(3.0, 0.1)(4.0))
    }

    @Test
    fun description() {
        assertEquals("is equal to 3.0 within 0.1", closeTo(3.0, 0.1).description)
        assertEquals("is not equal to 3.0 within 0.01", closeTo(3.0, 0.01).negatedDescription)
        assertEquals("is equal to 3.0 within 0.1", closeTo(3.0f, 0.1f).description)
        assertEquals("is not equal to 3.0 within 0.01", closeTo(3.0f, 0.01f).negatedDescription)
    }
}