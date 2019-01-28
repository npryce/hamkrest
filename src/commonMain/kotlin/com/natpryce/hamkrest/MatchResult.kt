package com.natpryce.hamkrest


/**
 * The result of matching some actual value against criteria defined by a [Matcher].
 */
sealed class MatchResult

/**
 * Represents that the actual value matched.
 */
object Match : MatchResult() {
    override fun toString(): String = "Match"
}

/**
 * Represents that the actual value did not match, and includes a human-readable description of the reason.
 *
 * @param description human readable text that explains why the value did not match.
 */
class Mismatch(override val description: String) : MatchResult(), SelfDescribing {
    override fun toString() = "Mismatch[${describe(description)}]"
}

