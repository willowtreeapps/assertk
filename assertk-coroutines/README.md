# assertk-coroutines

This lib adds some assertions for coroutines/flow. You can see all built-in assertions in the
[docs](https://willowtreeapps.github.io/assertk/javadoc/assertk-coroutines/assertk-coroutines/assertk.coroutines.assertions/index.html).

## Setup

```groovy
repositories {
  mavenCentral()
}

dependencies {
  testCompile 'com.willowtreeapps.assertk:assertk-coroutines:0.23'
}
```

## Usage

### Flow

Currently, the main thing this lib provides is many of the collection assertions on `Flow`. For example you can do

```kotlin
runBlocking {
    val flow = flowOf("one", "two")
    assertThat(flow).contains("one", "two")
}
```

For more complicated flow assertions, you may want to check out [turbine](https://github.com/cashapp/turbine). It can
play nicely with `assertk`.

```kotlin
flowOf("one", "two").test {
  assertThat(expectItem()).isEqualTo("one")
  assertThat(expectItem()).isEqualTo("two")
  expectComplete()
}
```

### Extracting data

Additionally, there's a `suspendCall()` method that works much like `prop()` but for suspendable functions.

```kotlin
runBlocking {
    assertThat(person).suspendCall("resume") { it.fetchResume() }.contains("kotlin")
}
```