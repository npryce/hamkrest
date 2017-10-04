@file:JvmName("MapMatchers")

package com.natpryce.hamkrest

/**
 * Matches an [Map] if any pair is matched by [pairMatcher].
 */
fun <K, V, KI : K, VI : V> anyPair(pairMatcher: Matcher<Pair<KI, VI>>) = object : Matcher.Primitive<Map<K, V>>() {
    override fun invoke(actual: Map<K, V>): MatchResult =
        match(actual.entries.map { it.toPair() }.any(pairMatcher.asPredicate() as (Pair<K, V>) -> Boolean)) { "was ${describe(actual)}" }

    override val description: String get() = "in which any pair ${describe(pairMatcher)}"
    override val negatedDescription: String get() = "in which no pair ${describe(pairMatcher)}"
}

internal fun <K, V> Map.Entry<K, V>.toPair() = this.key to this.value
