package com.natpryce.hamkrest

import kotlin.reflect.KClass

actual fun describe(v: Any?): String = defaultDescription(v)

actual val KClass<*>.reportedName : String get() = qualifiedName ?: "<unknown type>"
