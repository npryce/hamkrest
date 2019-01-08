# Matching numbers

## Ordering matchers

Match values by their relative order to another value: 

* greaterThan
* lessThan
* greaterThanOrEqualTo
* lessThanOrEqualTo

Because these work on Comparable values, they apply to all number types, and also strings, dates and times, and your own types that implement Comparable.


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
