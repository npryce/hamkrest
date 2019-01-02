# Extending how Hamkrest Describes Values

Hamkrest defines the `describe` function to generate readable representations of values in assertion failure messages 
and the descriptions of matchers.

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
Triples and Collections, and falls back to the `toString` method when it has no specialised representation for
a value.

If the `toString` method is not suitable for your types, you extend the `describe` function with your own representations
by defining a [JVM service][] of type `com.natpryce.hamkrest.ValueDescription`.
 
The ValueDescription service implements a single method, also called `describe`, which maps a value to a String 
representation, or `null` when it cannot generate a representation for a value.  When a ValueDescription
service returns `null`, other registered services are tried.  If no services can describe the value, the default 
description is used.


[JVM service]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html
