# Matching numbers

## Ordering matchers

Match values by their relative order to another value: 

* greaterThan
* lessThan
* greaterThanOrEqualTo
* lessThanOrEqualTo

```kotlin
assertThat(5, lessThan(10))
assertThat(10, greaterThan(5))
assertThat(10, greaterThanOrEqualTo(5))
assertThat(10, greaterThanOrEqualTo(10))
```

Because these work on Comparable values, they apply to all number types, and also strings, dates and times, and your own types that implement Comparable.

```kotlin
assertThat("large", lessThan("small"))
```

## "Close to" matcher

Matches a floating point value that is close enough to a given value. 

```kotlin
assertThat(5.17, closeTo(5.0, error=0.2))
```

Defined for both Double and Float.


## Using function references

[There are many functions in the Kotlin standard library you can use as matchers](function-references.md). For example:

```kotlin
assertThat(0.0/0.0, Double::isNaN)
```
