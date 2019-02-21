package com.natpryce.hamkrest

import kotlin.reflect.KClass

/**
 * A [Matcher] that matches anything, always returning [Match].
 */
val anything = object : PrimitiveMatcher<Any?>() {
    override fun invoke(actual: Any?): MatchResult = Match
    override val description: String get() = "anything"
    override val negatedDescription: String get() = "nothing"
}

/**
 * A [Matcher] that matches nothing, always returning a [Mismatch].
 */
val nothing = !anything


/**
 * Returns a matcher that reports if a value is equal to an [expected] value.  Handles null comparisons, just as
 * the `==` operator does.
 */
fun <T> equalTo(expected: T?): Matcher<T?> =
    object : PrimitiveMatcher<T?>() {
        override fun invoke(actual: T?): MatchResult = match(actual == expected) { "was: ${describe(actual)}" }
        override val description: String get() = "is equal to ${describe(expected)}"
        override val negatedDescription: String get() = "is not equal to ${describe(expected)}"
    }

/**
 * Returns a matcher that reports if a value is the same instance as [expected] value.
 */
fun <T> sameInstance(expected: T): Matcher<T> =
    object : PrimitiveMatcher<T>() {
        override fun invoke(actual: T): MatchResult = match(actual === expected) { "was: ${describe(actual)}" }
        override val description: String get() = "is same instance as ${describe(expected)}"
        override val negatedDescription: String get() = "is not same instance as ${describe(expected)}"
    }


/**
 * Returns a matcher that reports if a value is null.
 */
fun <T> absent(): Matcher<T?> = object : PrimitiveMatcher<T?>() {
    override fun invoke(actual: T?): MatchResult = match(actual == null) { "was: ${describe(actual)}" }
    override val description: String get() = "null"
}

/**
 * Returns a matcher that reports if a value is not null and meets the criteria of the [valueMatcher]
 */
fun <T> present(valueMatcher: Matcher<T>? = null) = object : PrimitiveMatcher<T?>() {
    override fun invoke(actual: T?) =
        when {
            actual == null -> Mismatch("was: null")
            valueMatcher == null -> Match
            else -> valueMatcher(actual)
        }
    
    override val description: String
        get() = "is not null" + (if (valueMatcher == null) "" else " & ${valueMatcher.description}")
}

/**
 * Returns a matcher that reports if a value of [Any] type is of a type compatible with [downcastMatcher] and, if so,
 * if the value meets its criteria.
 */
inline fun <reified T : Any> isA(downcastMatcher: Matcher<T>? = null): Matcher<Any> {
    return isInstanceOf(T::class, downcastMatcher)
}

@PublishedApi
internal fun <T : Any> isInstanceOf(tClass: KClass<T>, downcastMatcher: Matcher<T>? = null): Matcher<Any> =
    object : PrimitiveMatcher<Any>() {
        override fun invoke(actual: Any) =
            if (!tClass.isInstance(actual)) {
                Mismatch("was: a ${actual::class.reportedName}")
            }
            else if (downcastMatcher == null) {
                Match
            }
            else {
                // Reified types not fully supported on native platform yet.
                @Suppress("UNCHECKED_CAST")
                downcastMatcher(actual as T)
            }
        
        override val description: String
            get() = "is a ${tClass.reportedName}" + if (downcastMatcher == null) "" else " ${downcastMatcher.description}"
    }


/**
 * Returns a matcher that reports if a value of [Any] type is of a type compatible with [downcastMatcher] and, if so,
 * if the value meets its criteria.
 */
inline fun <reified T : Any> cast(downcastMatcher: Matcher<T>): Matcher<Any> = isA(downcastMatcher)

/**
 * Returns a matcher that reports if a [Comparable] value is greater than [n]
 */
fun <N : Comparable<N>> greaterThan(n: N) = _comparesAs("greater than", n) { it > 0 }

/**
 * Returns a matcher that reports if a [Comparable] value is greater than or equal to [n]
 */
fun <N : Comparable<N>> greaterThanOrEqualTo(n: N) = _comparesAs("greater than or equal to", n) { it >= 0 }

/**
 * Returns a matcher that reports if a [Comparable] value is less than [n]
 */
fun <N : Comparable<N>> lessThan(n: N) = _comparesAs("less than", n) { it < 0 }

/**
 * Returns a matcher that reports if a [Comparable] value is less than or equal to [n]
 */
fun <N : Comparable<N>> lessThanOrEqualTo(n: N) = _comparesAs("less than or equal to", n) { it <= 0 }

private fun <N : Comparable<N>> _comparesAs(description: String, n: N, expectedSignum: (Int) -> Boolean): Matcher<N> {
    return object : PrimitiveMatcher<N>() {
        override fun invoke(actual: N): MatchResult =
            match(expectedSignum(actual.compareTo(n))) { "was: ${describe(actual)}" }
        
        override val description: String
            get() {
                return "is ${description} ${describe(n)}"
            }
    }
}

/**
 * Returns a matcher that reports if a [kotlin.Comparable] value falls within the given [range].
 *
 * @param range The range that contains matching values.
 */
fun <T : Comparable<T>> isWithin(range: ClosedRange<T>): Matcher<T> {
    fun _isWithin(actual: T, range: ClosedRange<T>): Boolean {
        return range.contains(actual)
    }
    
    return Matcher(::_isWithin, range)
}


/**
 * Returns a matcher that reports if a block throws an exception of type [E] and, if [exceptionCriteria] is given,
 * the exception matches the [exceptionCriteria].
 */
inline fun <reified E : Throwable> throws(exceptionCriteria: Matcher<E>? = null): Matcher<() -> Unit> {
    return throwsExceptionOfClass(E::class, exceptionCriteria)
}

@PublishedApi
internal fun <E : Throwable> throwsExceptionOfClass(exceptionClass: KClass<E>, exceptionCriteria: Matcher<E>?): Matcher<()->Unit> =
    object : PrimitiveMatcher<() -> Unit>() {
        private val exceptionName = exceptionClass.reportedName
        
        override fun invoke(actual: () -> Unit): MatchResult =
            try {
                actual()
                Mismatch("did not throw")
            }
            catch (e: Throwable) {
                if (exceptionClass.isInstance(e)) {
                    // Reified types not fully supported on native platform yet.
                    @Suppress("UNCHECKED_CAST")
                    exceptionCriteria?.invoke(e as E) ?: Match
                }
                else {
                    Mismatch("threw ${e::class.reportedName}")
                }
            }
        
        override val description: String
            get() = "throws $exceptionName${exceptionCriteria?.let { " that ${describe(it)}" } ?: ""}"
        override val negatedDescription: String
            get() = "does not throw $exceptionName${exceptionCriteria?.let { " that ${describe(it)}" } ?: ""}"
    }

