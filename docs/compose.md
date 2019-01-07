# Composing Matchers

Matchers can be composed to form new matchers.  The following are a few of the combinators that Hamkrest provides to compose sophisticated matchers from more primitive parts.


## Logical combinators

Syntactic sugar. You can negate a matcher with the `!` operator and compose matchers with infix `and` and `or` functions:

```kotlin
assertThat("xyzzy", startsWith("x") and endsWith("y") and !(containsSubstring("a") or containsSubstring("b")))
```

The `allOf` function is the same as infix `and` but lets you combine any number of matchers:

```kotlin
assertThat("xyzzy", allOf(startsWith("x"), endsWith("y"), containsSubstring("zz")))
```


The `anyOf` function is the same as infix `or` but lets you combine any number of matchers:

```kotlin
assertThat("xyzzy", anyOf(startsWith("x"), startsWith("y"), startsWith("z")))
```


## Matching properties of the subject

The `has` combinator creates a matcher that applies another matcher to a property of the subject:

```kotlin
assertThat("xyzzy", has(String::length, equalTo(5)))
```


## Presence (e.g. non-nullableness)

The `present` combinator creates a matcher for a nullable value that asserts that the value is present (e.g. not null)
and applies another matcher to that non-null value.

```kotlin
val s: String? = "xyzzy"

assertThat(s, present(startsWith("x")))
```

When not passed a matcher, `present` matches any value that is not null.

The opposite is `absent`, which only matches null.


