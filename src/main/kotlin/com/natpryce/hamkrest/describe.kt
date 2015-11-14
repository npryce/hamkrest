package com.natpryce.hamkrest

fun describe(v: Any?): String = when (v) {
    null -> "null"
    is SelfDescribing -> v.description()
    is String -> "\"" + v.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    is Pair<*, *> -> Pair(describe(v.first), describe(v.second)).toString()
    is Triple<*, *, *> -> Triple(describe(v.first), describe(v.second), describe(v.third)).toString()
    is Range<*> -> "${describe(v.start)}..${describe(v.end)}"
    is Iterable<*> -> v.map(::describe).joinToString(prefix = "[", separator = ", ", postfix = "]")
    is Map<*, *> -> v.entries.map { "${describe(it.key)}:${describe(it.value)}" }.joinToString(prefix = "{", separator = ", ", postfix = "}")
    else -> v.toString()
}

interface SelfDescribing {
    fun description(): String
}

class Described<T>(val description: String, val value: T) : SelfDescribing {
    override fun description() = description
}
