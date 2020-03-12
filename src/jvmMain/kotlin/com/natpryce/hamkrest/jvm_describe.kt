package com.natpryce.hamkrest

import java.util.ServiceLoader
import kotlin.reflect.KClass

actual fun describe(v: Any?): String =
    ServiceLoader.load(ValueDescription::class.java).asSequence()
        .mapNotNull { it.describe(v) }
        .firstOrNull() ?: defaultDescription(v)

actual val KClass<*>.reportedName : String get() = qualifiedName ?: "<unknown type>"
