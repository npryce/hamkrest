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

**Tip**: You can define ValueDescription services in each module of your project, meaning they can use internal features
of your modules to generate representations.

[describe]: https://github.com/npryce/hamkrest/blob/master/src/main/kotlin/com/natpryce/hamkrest/describe.kt
[JVM service]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html
[com.natpryce.hamkrest.ValueDescription]: https://github.com/npryce/hamkrest/blob/master/src/main/kotlin/com/natpryce/hamkrest/ValueDescription.kt
