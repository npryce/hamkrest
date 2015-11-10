package org.hamcrest.kotlin;

import org.junit.Test

class Comparables {
    @Test
    public fun numericalComparison() {
        assertThat(10, greaterThan(5))
        assertThat(10, greaterThanOrEqualTo(5))
        assertThat(10, greaterThanOrEqualTo(10))
        assertThat(10, !greaterThanOrEqualTo(50))

        assertThat(10, lessThan(20))
        assertThat(10, lessThanOrEqualTo(20))
        assertThat(10, lessThanOrEqualTo(10))
        assertThat(10, !lessThanOrEqualTo(5))
    }

}