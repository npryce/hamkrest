package com.natpryce.hamkrest

import java.util.ServiceLoader
import kotlin.reflect.KClass

private val descriptionServices = ServiceLoader.load(ValueDescription::class.java)

actual fun describe(v: Any?): String =
    descriptionServices.map { it.describe(v) }.filterNotNull().firstOrNull() ?: defaultDescription(v)


actual val KClass<*>.reportedName : String get() = qualifiedName ?: "<unknown type>"
