package com.natpryce.hamkrest

import java.util.ServiceLoader

private val descriptionServices = ServiceLoader.load(ValueDescription::class.java)

actual fun describe(v: Any?): String =
    descriptionServices.map { it.describe(v) }.filterNotNull().firstOrNull() ?: defaultDescription(v)
