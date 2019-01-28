package com.natpryce.hamkrest

import kotlin.reflect.KClass

actual val KClass<*>.reportedName : String get() = simpleName ?: "<unknown>"

actual fun describe(v: Any?): String = defaultDescription(v)
