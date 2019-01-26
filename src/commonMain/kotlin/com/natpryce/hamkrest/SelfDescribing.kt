package com.natpryce.hamkrest

/**
 * An object that can describe itself.
 */
interface SelfDescribing {
    /**
     * The description of this object
     */
    val description: String
}