package com.natpryce.hamkrest

/**
 * A service interface for extensions to the [describe] function.
 */
interface ValueDescription {
    /**
     * Describes the value [v] or returns `null` to indicate that this service cannot describe the value, in which
     * case, other registered services are tried.  If no services can describe the value, the [defaultDescription]
     * function is called.
     */
    fun describe(v: Any?): String?
}