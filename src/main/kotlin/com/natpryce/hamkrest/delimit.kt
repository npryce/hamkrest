package com.natpryce.hamkrest

fun <T> delimit(v: T): String = when (v) {
    is String -> "\"" + v.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    is Pair<*,*> -> Pair(delimit(v.first), delimit(v.second)).toString()
    else -> v.toString()
}

