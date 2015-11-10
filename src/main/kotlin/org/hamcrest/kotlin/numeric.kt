
package org.hamcrest.kotlin

import org.hamcrest.kotlin.internal.match

public fun <N :Comparable<N>> greaterThan(n:N) = _comparesAs(n, "greater than") {it > 0}
public fun <N :Comparable<N>> greaterThanOrEqualTo(n:N) = _comparesAs(n, "greater than") {it >= 0}
public fun <N :Comparable<N>> lessThan(n:N) = _comparesAs(n, "greater than") {it < 0}
public fun <N :Comparable<N>> lessThanOrEqualTo(n:N) = _comparesAs(n, "greater than") {it <= 0}

private fun <N :Comparable<N>> _comparesAs(n: N, description: String, expectedSignum: (Int)->Boolean) : Matcher<N> {
    return object : Matcher.Primitive<N>() {
        override fun invoke(actual: N): MatchResult =
                match(expectedSignum(actual.compareTo(n))) {"was ${delimit(actual)}"}

        override fun description(): String {
            return "${description} ${delimit(n)}"
        }
    }
}