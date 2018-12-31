package com.natpryce.hamkrest

import java.util.ServiceLoader

interface ValueDescription {
    fun describe(v: Any?): String?
}

private val descriptionServices = ServiceLoader.load(ValueDescription::class.java)

fun describe(v: Any?): String =
    descriptionServices.map { it.describe(v) }.filterNotNull().firstOrNull() ?: defaultDescription(v)


/**
 * Formats [v] to be included in a description.  Strings are delimited with quotes and elements of tuples, ranges,
 * iterable collections and maps are (recursively) described.  A null reference is described as `null`.
 * For anything else, the result of [Any.toString] is used.
 *
 * @param v the value to be described.
 */
fun defaultDescription(v: Any?): String = when (v) {
    null -> "null"
    is SelfDescribing -> v.description
    is String -> "\"" + v.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    is Pair<*, *> -> Pair(describe(v.first), describe(v.second)).toString()
    is Triple<*, *, *> -> Triple(describe(v.first), describe(v.second), describe(v.third)).toString()
    is ClosedRange<*> -> "${describe(v.start)}..${describe(v.endInclusive)}"
    is Collection<*> -> v.map(::describe).joinToString(prefix = "[", separator = ", ", postfix = "]")
    is Map<*, *> -> v.entries.map { "${describe(it.key)}:${describe(it.value)}" }.joinToString(prefix = "{", separator = ", ", postfix = "}")
    else -> v.toString()
}

/**
 * An object that can describe itself.
 */
interface SelfDescribing {
    /**
     * The description of this object
     */
    val description: String
}

/**
 * Combines a [value] and its [description].
 *
 * @property description the description of the value
 * @property value the value described by the description
 */
class Described<T>(override val description: String, val value: T) : SelfDescribing
