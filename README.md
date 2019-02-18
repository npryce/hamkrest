# Hamkrest - Hamcrest for Kotlin

An implementation of Hamcrest to take advantage of [Kotlin](https://kotlinlang.org/) language features.

[![Kotlin](https://img.shields.io/badge/kotlin-1.3.11-blue.svg)](http://kotlinlang.org)
[![Build Status](https://travis-ci.org/npryce/hamkrest.svg?branch=master)](https://travis-ci.org/npryce/hamkrest)
[![Maven Central](https://img.shields.io/maven-central/v/com.natpryce/hamkrest.svg)](https://search.maven.org/artifact/com.natpryce/hamkrest)

Note: as of version 1.4.0.0, you must add kotlin-reflect to the classpath to use Hamkrest's reflective features.

## Getting Started

Add Hamkrest as a dependency of your project. [Hamkrest is distributed via Maven Central](https://search.maven.org/artifact/com.natpryce/hamkrest). 

Import matchers from `com.natpryce.hamkrest`.  E.g. a quick way to get started is to import `com.natpryce.hamkrest.*` and then use IntelliJ to optimise imports when you're done.

If you're using Hamkrest for assertions, import `com.natpryce.hamkrest.assertion.assertThat`

Now you can make assertions about values using matchers:

```kotlin
assertThat(thisName, equalTo("Alice"))
assertThat(thatName, equalTo("Bob"))
```

... and so forth


## More Information

* [Matching numbers](docs/numbers.md)
* [Composing matchers](docs/compose.md)
* [Using function and method references as Matchers](docs/function-references.md)
* [How Hamkrest describes values, and how you can plug in your own descriptions](docs/describe.md)
* [Hamkrest's version numbers explained](docs/version-numbering.md)
