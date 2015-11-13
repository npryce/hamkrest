package com.natpryce.hamkrest

fun delimit(v: Any?): String = when (v) {
    null -> "null"
    is String -> "\"" + v.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    is Pair<*,*> -> Pair(delimit(v.first), delimit(v.second)).toString()
    else -> v.toString()
}

