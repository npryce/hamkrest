# Extending how Hamkrest Describes Values

Hamkrest provides a function, called [describe][], that generates readable representations of values for inclusion in
assertion failure messages and the descriptions of matchers.

For example:

```
describe(10)
>>> 10

describe(listOf("a","b","c"))
>>> ["a", "b", "c"]

describe(null)
>>> null
```

The `describe` function generates reasonable representations of primitive types, null, Strings, Pairs, 
Triples and Collections, and falls back to calling `toString` when it has no specialised representation for
a value.

If the built-in representation is not suitable for your types, you can plug new representations into the `describe` function by registering a [JVM service][] of type [com.natpryce.hamkrest.ValueDescription][].

The ValueDescription service interface implements a single method, also called `describe`, which maps a value either to 
a String representation, or to `null` if the service cannot generate a representation for the value.  When a 
ValueDescription service returns `null`, Hamkrest will pass the value to other registered ValueDescription services and,
if no services can describe the value, generate a description using the default rules described above.

## Example

Suppose we have a type like this:

```kotlin
package com.bigcorp.crm

class Customer(val name: String /* ... and  other features */ )
```
By default, Hamkrest will represent Customers by calling their toString method.  But this does not give any useful
information.  We can therefore register a ValueDescription service to represent Customers in Hamkrest's test diagnostics.

1. Define a class of type [com.natpryce.hamkrest.ValueDescription][].

```kotlin
package com.bigcorp.crm

import com.natpryce.hamkrest.ValueDescription

class CustomerDescription : ValueDescription {
}
```

2. Implement the `ValueDescription::describe` method to either describe Customers or return `null`:

```kotlin
package com.bigcorp.crm

import com.natpryce.hamkrest.ValueDescription

class CustomerDescription : ValueDescription {
    override fun describe(v: Any?) =
        when (v) {
            is Customer -> "Customer[${v.name}]"
            else -> null
        }
}
```

3. Register the service by creating a resource file named `META-INF/services/com.natpryce.hamkrest.ValueDescription` that contains the fully qualified name name of the `CustomerDescription` class:

```
com.bigcorp.crm.CustomerDescription
```

## Tips

You can have more than one ValueDescription service, and each service can provide representations for multiple types of value.  The most convenient organisation I've found is to define a single ValueDescription services in each module of the project. This also allows them to use internal features to generate representations, if required.

[describe]: https://github.com/npryce/hamkrest/blob/master/src/main/kotlin/com/natpryce/hamkrest/describe.kt
[JVM service]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html
[com.natpryce.hamkrest.ValueDescription]: https://github.com/npryce/hamkrest/blob/master/src/main/kotlin/com/natpryce/hamkrest/ValueDescription.kt
