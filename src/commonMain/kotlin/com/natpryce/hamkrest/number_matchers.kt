package com.natpryce.hamkrest

import kotlin.math.abs

/**
 * Returns a matcher that reports if a value is equal to an [expected] value, withing a range of +/- [error].
 */
fun closeTo(expected: Float, error: Float = 0.00001f): Matcher<Float> = _closeTo(expected, error)

/**
 * Returns a matcher that reports if a value is equal to an [expected] value, withing a range of +/- [error].
 */
fun closeTo(expected: Double, error: Double = 0.00001): Matcher<Double> = _closeTo(expected, error)

private fun <T : Number> _closeTo(expected: T, error: T): Matcher<T> =
        object : Matcher<T> {
            override fun invoke(actual: T): MatchResult = match(delta(actual, expected, error) <= 0.0) {
                "a numeric value ${describe(actual)} differed by ${describe(delta(actual, expected, error))} more than error ${describe(error)}"
            }
            override val description: String get() = "is equal to ${describe(expected)} within ${describe(error)}"
            override val negatedDescription: String get() = "is not equal to ${describe(expected)} within ${describe(error)}"

            private fun delta(actual: T, expected: T, error: T) = abs(actual.toDouble() - expected.toDouble()) - error.toDouble()
        }