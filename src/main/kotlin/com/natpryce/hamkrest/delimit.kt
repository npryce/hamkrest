package com.natpryce.hamkrest

fun delimit(v: Any?): String = when (v) {
    null -> "null"
    is String -> "\"" + v.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    is Pair<*, *> -> Pair(delimit(v.first), delimit(v.second)).toString()
    is Triple<*, *, *> -> Triple(delimit(v.first), delimit(v.second), delimit(v.third)).toString()
    is Range<*> -> "${delimit(v.start)}..${delimit(v.end)}"
    is Iterable<*> -> v.map(::delimit).joinToString(prefix = "[", separator= ", ", postfix = "]")
    is Map<*,*> -> v.entries.map{"${delimit(it.key)}:${delimit(it.value)}"}.joinToString(prefix="{", separator=", ", postfix="}")
    else -> v.toString()
}

