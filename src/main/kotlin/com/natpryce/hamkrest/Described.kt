package com.natpryce.hamkrest

/**
 * Combines a [value] and its [description].
 *
 * @property description the description of the value
 * @property value the value described by the description
 */
@Deprecated("This is only used by the `should` package, which is deprecated. Use assertThat instead")
class Described<T>(override val description: String, val value: T) : SelfDescribing
