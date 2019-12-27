# assertk

[![CircleCI](https://circleci.com/gh/willowtreeapps/assertk.svg?style=svg)](https://circleci.com/gh/willowtreeapps/assertk)[![Maven Central](https://img.shields.io/maven-central/v/com.willowtreeapps.assertk/assertk.svg)](https://search.maven.org/search?q=g:com.willowtreeapps.assertk)
[![Sonatype Snapshot](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.willowtreeapps.assertk/assertk.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/willowtreeapps/assertk)

assertions for kotlin inspired by assertj

## Setup

### Gradle/JVM

```groovy
repositories {
  mavenCentral()
}

dependencies {
  testCompile 'com.willowtreeapps.assertk:assertk-jvm:0.20'
}
```

### Javascript/Common

Replace dependency on `assertk-jvm` with `assertk-js` or `assertk` to use it in JavaScript and common projects,
respectively.

## Usage

Simple usage is to wrap the value or property you are testing in `assertThat()` and call assertion methods on the result.

```kotlin
import assertk.assertThat
import assertk.assertions.*

class PersonTest {
    val person = Person(name = "Bob", age = 18)

    @Test
    fun testName() {
        assertThat(person.name).isEqualTo("Alice")
        // -> expected:<["Alice"]> but was:<["Bob"]>
    }

    @Test
    fun testAge() {
        assertThat(person.age, "age").isGreaterThan(20)
        // -> expected [age] to be greater than:<20> but was:<18>
    }

    @Test
    fun testNameProperty() {
        assertThat(person::name).isEqualTo("Alice")
        // -> expected [name]:<["Alice"]> but was:<["Bob"]>
    }
}
```

You can see all built-in assertions in the [docs](https://willowtreeapps.github.io/assertk/javadoc/assertk/assertk.assertions/index.html).

### Nullability
Since null is a first-class concept in kotlin's type system, you need to be explicit in your assertions.

```kotlin
val nullString: String? = null
assertThat(nullString).hasLength(4)
```
will not compile, since `hasLength()` only makes sense on non-null values. You can chain `isNotNull()` to handle this.

```kotlin
val nullString: String? = null
assertThat(nullString).isNotNull().hasLength(4)
// -> expected to not be null
```
This will first ensure the string is not null before running any other checks.

### Multiple assertions

You can assert multiple things on a single value by providing a lambda as the second argument. All assertions will be
run even if the first one fails.

```kotlin
val string = "Test"
assertThat(string).all {
    startsWith("L")
    hasLength(3)
}
// -> The following 2 assertions failed:
//    - expected to start with:<"L"> but was:<"Test">
//    - expected to have length:<3> but was:<"Test"> (4)
```

You can wrap multiple assertions in an `assertAll` to ensure all of them get run, not just the first one.

```kotlin
assertAll {
    assertThat(false).isTrue()
    assertThat(true).isFalse()
}
// -> The following 2 assertions failed:
//    - expected to be true
//    - expected to be false
```

### Exceptions

If you expect an exception to be thrown, you can use the version of `assertThat` that takes a lambda.

```kotlin
assertThat {
    throw Exception("error")
}.isFailure().hasMessage("wrong")
// -> expected [message] to be:<["wrong"]> but was:<["error"]>
```

This method also allows you to assert on successfully returned values.
```kotlin
assertThat { 1 + 1 }.isSuccess().isNegative()
// -> expected to be negative but was:<2>
```

### Table Assertions

If you have multiple sets of values you want to test with, you can create a table assertion.

```kotlin
tableOf("a", "b", "result")
    .row(0, 0, 1)
    .row(1, 2, 4)
    .forAll { a, b, result ->
        assertThat(a + b).isEqualTo(result)
    }
// -> the following 2 assertions failed:
//    on row:(a=<0>,b=<0>,result=<1>)
//    - expected:<[1]> but was:<[0]>
//    on row:(a=<1>,b=<2>,result=<4>)
//    - expected:<[4]> but was:<[3]>
```

Up to 4 columns are supported.

## Custom Assertions

One of the goals of this library is to make custom assertions easy to make. All assertions are just extension methods.

```kotlin
fun Assert<Person>.hasAge(expected: Int) {
    prop("age", Person::age).isEqualTo(expected)
}

assertThat(person).hasAge(10)
// -> expected [age]:<1[0]> but was:<1[8]> (Person(age=18))
```

For completely custom assertions, you can access the actual value with `given` and fail with `expected()` and `show()`.

```kotlin
fun Assert<Person>.hasAge(expected: Int) = given { actual ->
    if (actual.age == expected) return
    expected("age:${show(expected)} but was age:${show(actual.age)}")
}

assertThat(person).hasAge(10)
// -> expected age:<10> but was age:<18>
```

## Contributing to assertk
Contributions are more than welcome! Please see the [Contributing Guidelines](https://github.com/willowtreeapps/assertk/blob/master/Contributing.md) and be mindful of our [Code of Conduct](https://github.com/willowtreeapps/assertk/blob/master/code-of-conduct.md).

## Known Issues

1. You get `java.lang.AssertionError: java.lang.NoClassDefFoundError: org/opentest4j/AssertionFailedError` when running a failing test from intellij.

    I've filed a [bug](https://youtrack.jetbrains.com/issue/IDEA-214533) about this, it works correctly when running on the cmdline with gradle. To workaround, you can explicilty add `opentest4j` as a dependency.

   ```groovy
   testComple 'org.opentest4j:opentest4j:1.1.1'
   ```

2. Gradle fails to find the correct variant if the `kapt` plugin is applied:
    ```
       > Could not resolve com.willowtreeapps.assertk:assertk-jvm:0.19.
         Required by:
             project :core-test
          > Cannot choose between the following variants of com.willowtreeapps.assertk:assertk-jvm:0.19:
              - jvm-api
              - jvm-runtime
              - metadata-api
    ```
    This is a [known issue](https://youtrack.jetbrains.com/issue/KT-31641) with the kapt plugin, you can add the below to your gradle file to work around it
    ```groovy
    configurations.all { configuration ->
        // Workaround for kapt bug with MPP dependencies
        // https://youtrack.jetbrains.com/issue/KT-31641
        if (name.contains('kapt')) {
            attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.class, Usage.JAVA_RUNTIME))
        }
    }
    ```
