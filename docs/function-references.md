# Shortcuts: using function references as Matchers

There are many functions and methods in the Kotlin & Java stdlibs that can be used to build matchers. It’s not practical to define Matcher implementations for all of them. Instead Hamkrest provides a few mechanisms to let you either refer to those functions directly or easily define one-liner factory functions that are useful for your project, which turn create Matchers from function references.


You can convert named unary predicates into matchers.

``` kotlin
val isBlank = Matcher(String::isBlank)

assertThat(input, isBlank)
```

As a shortcut, you can pass named functions to the `assertThat`, `and`, `or` and many other functions that take a matcher.

``` kotlin
assertThat(input, String::isBlank)
```

You can also convert a named binary predicate and the second argument to a matcher for first argument, which works well for extension methods.

``` kotlin
fun String.hasLength(n: Int): Boolean = this.length == n

val isTheRightLength = Matcher(String::hasLength, 8)

assertThat(secretCode, isTheRightLength)
```

You can use function and property references to match features of a value:

``` kotlin
val isLongEnough = has(String::length, greaterThan(8))

assertThat(password, isLongEnough)
```

All of these shortcuts produce good, human-readable diagnostics.
