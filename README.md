# assertk

[![CircleCI](https://circleci.com/gh/willowtreeapps/assertk.svg?style=svg)](https://circleci.com/gh/willowtreeapps/assertk)[![Maven Central](https://img.shields.io/maven-central/v/com.willowtreeapps.assertk/assertk.svg)](https://search.maven.org/search?q=g:com.willowtreeapps.assertk)
[![Sonatype Snapshot](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.willowtreeapps.assertk/assertk.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/willowtreeapps/assertk/)

assertions for kotlin inspired by assertj

## Setup

### Gradle/JVM

```groovy
repositories {
  mavenCentral()
}

dependencies {
  testImplementation 'com.willowtreeapps.assertk:assertk-jvm:0.25'
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

You can see all built-in assertions in the [docs](https://willowtreeapps.github.io/assertk/javadoc/assertk/assertk/assertk.assertions/index.html).

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

### Iterable/List Assertions
You can assert on the contents of an `Iterable/List` with the various `contains*` functions. They have different
semantics as follows:

|Assertion|Description|
|---|---|
|containsAll|Asserts the iterable contains all the expected elements, in **any order**. The collection may also contain **additional elements**.|
|containsSubList|Asserts that a collection contains a **subset** of items the **same order**, but may have **additional elements** in the list.|
|containsOnly|Asserts the iterable contains **only the expected elements**, in **any order**. **Duplicate values** in the expected and actual are ignored.|
|containsExactlyInAnyOrder|Asserts the iterable contains **exactly the expected elements**, in **any order**. Each value in expected must correspond to a matching value in actual, and visa-versa.|
|containsExactly|Asserts the list contains **exactly the expected elements**. They must be in the **same order** and there must not be any extra elements.|
|containsNone|Asserts the iterable **does not contain any** of the expected elements|

### Extracting data

There's a few ways you extract the data you want to assert on. While you can do this yourself before calling the 
assertion, these methods will add the extra context to the failure message which can be helpful.

The simplest way is with `prop()`. It will take a property (or function, or a name and a lambda) and return an
assertion on that property.

```kotlin
val person = Person(age = 22)
assertThat(person).prop(Person::age).isEqualTo(20)

// -> expected [age]:<2[0]> but was:<2[2]> (Person(age=22))
```

For collections, you can use `index()` to pull a specific index from a list, and `key()` to pull a specific value from
a map.

```kotlin
assertThat(listOf(1, 2, 3)).index(1).isEqualTo(1)

// -> expected: [[1]]:<1> but was:<2> ([1, 2, 3])

assertThat(mapOf("one" to 1, "two" to 2, "three" to 3)).key("two").isEqualTo(1)

// -> expected: [["two"]]:<1> but was:<2> ({"one"=1, "two"=2, "three"=3})
```

You can also extract a property from a collection using `extracting()`.

```kotlin
val people = listOf(Person(name = "Sue"), Person(name = "Bob"))
assertThat(people)
    .extracting(Person::name)
    .containsExactly("Sue", "Bob")
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
    prop(Person::age).isEqualTo(expected)
}

assertThat(person).hasAge(20)
// -> expected [age]:<2[0]> but was:<2[2]> (Person(age=22))
```

For completely custom assertions, you have a few building blocks. `given` will give you the actual value to assert on,
and `expected()` and `show()` will help you format your failure message.

```kotlin
fun Assert<Person>.hasAge(expected: Int) = given { actual ->
    if (actual.age == expected) return
    expected("age:${show(expected)} but was age:${show(actual.age)}")
}

assertThat(person).hasAge(20)
// -> expected age:<20> but was age:<22>
```

You can also build assertions that chain by using `transform`. This allows you to both assert on the actual value, and
return something more specific that additional assertions can be chained on.

```kotlin
fun Assert<Person>.hasMiddleName(): Assert<String> = transform(appendName("middleName", seperator = ".")) { actual ->
   if (actual.middleName != null) {
       actual.middleName
   } else {
       expected("to not be null")
   }
}

assertThat(person).hasMiddleName().isEqualTo("Lorie")

// -> expected [middleName]:to not be null
```

Note: this is a bit of a contrived example as you'd probably want to build this out of existing assertions instead.

```kotlin
fun Assert<Person>.hasMiddleName(): Assert<String> = prop(Person::middleName).isNotNull()
```

The general rule of thumb is to prefer building out of the existing assertions unless you can give a more meaningful
error message.

## Contributing to assertk
Contributions are more than welcome! Please see the [Contributing Guidelines](https://github.com/willowtreeapps/assertk/blob/master/Contributing.md) and be mindful of our [Code of Conduct](https://github.com/willowtreeapps/assertk/blob/master/code-of-conduct.md).
