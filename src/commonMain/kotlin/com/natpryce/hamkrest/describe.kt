package com.natpryce.hamkrest

/**
 * Formats [v] to be included in a description.  Strings are delimited with quotes and elements of tuples, ranges,
 * iterable collections and maps are (recursively) described.  A null reference is described as `null`.
 * For anything else, the result of [Any.toString] is used.
 *
 * @param v the value to be described.
 */
expect fun describe(v: Any?): String

/**
 * The default description of a value, used when a value is not described by any registered [ValueDescription] services.
 */
internal fun defaultDescription(v: Any?): String = when (v) {
    null -> "null"
    is SelfDescribing -> v.description
    is String -> "\"" + v.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    is Pair<*, *> -> Pair(describe(v.first), describe(v.second)).toString()
    is Triple<*, *, *> -> Triple(describe(v.first), describe(v.second), describe(v.third)).toString()
    is ClosedRange<*> -> "${describe(v.start)}..${describe(v.endInclusive)}"
    is Set<*> -> v.map(::describe).joinToString(prefix = "{", separator = ", ", postfix = "}")
    is Collection<*> -> v.map(::describe).joinToString(prefix = "[", separator = ", ", postfix = "]")
    is Map<*, *> -> v.entries.map { "${describe(it.key)}:${describe(it.value)}" }.joinToString(prefix = "{", separator = ", ", postfix = "}")
    else -> v.toString()
}
