# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed
- renamed `prop` to `having` as part of effort to unify API naming, old name is deprecated.
- renamed `suspendCall` to `having` as part of effort to unify API naming, old name is deprecated.
- added `doesNotExist` assertions to `Path`.
- the receiver types for `isTrue()`, `isFalse()`, and `isInstanceOf()` have been widened to operate on nullable values.
- signature of `isIn` and `isNotIn` has been changed to ensure at least two items are supplied.
- added assertions `isIn(Iterable<T>)` and `isNotIn(Iterable<T>)`
- added transformers `Map<K,V>.havingKeys()` and `Map<K,V>.havingValues()`

## [0.28.1] 2024-04-17

### Added
- Added core wasmWasi support, wasmJs to coroutines

## [0.28.0] 2023-12-05

### Changed
- Minimum supported kotlin version is 1.9.21
- Rename `isSameAs/isNotSameAs` to `isSameInstanceAs/isNotSameInstanceAs` to make it more clear they
  use are about instance identity, old name is deprecated
- Rename `containsAll` to `containsAtLeast` to make it more clear that the collection can contain additional elements,
  old name is deprecated
- Bytes are shown as hex on all supported platforms (previously it was only on the jvm)
- If `isEqualTo` fails and the expected and actual display the same string, print a disambiguation message, ex:
  ```
  expected:<4> with type:<class kotlin.Int> but was type:<class kotlin.Short> with the same string representation
  ```

### Breaking Changes
- Binary-breaking change as previous error-level deprecations were removed

### Added
- Added `doesNotContainKey` assertion for `Map`

### Fixed
- Fixed incorrect usage of contains in some kdoc examples
- Exceptions being swallowed if thrown in a soft assertion block
- More correctly re-throw fatal error in the jvm

## [0.27.0] 2023-09-13

### Changed
- Minimum supported kotlin version is 1.9.0
- Added support for WASM targets (note: this only applies to the core library
  and not assertk-coroutines as the coroutines library doesn't have a stable
  release for it yet)

### Breaking Changes
- Previous `assertThat {}` deprecation warning is now an error

## [0.26.1] 2023-05-18

### Fixed
- Made `assertFailure {}` inline like `assertThat {}` was to allow testing suspend function failures.

## [0.26] 2023-05-12

### Changed
- Minimum supported kotlin version is 1.8.10
- Minimum supported coroutines version is 1.7.0
- When asserting on a `Throwable` or failed `Result`, their exception is added as the cause to any
  `AssertionError`s which are thrown as the result of an assertion failure.
- Removed second type parameter on `Any.isInstanceOf` extension. In practice this would generally widen
  to `Any` which is what it has been replaced with.

### Breaking Changes
- Previous deprecations are now errors

### Added
- Added `assertFailure { }` entrypoint which is a shorthand for `assertThat(runCatching { .. }).isFailure()`
- Added `first` and `single` assertion for `Iterable`
- Added `containsMatch` assertion for `CharSequence`.
  Unlike 'contains' this is a regex rather than a literal.
  Unlike 'matches' this looks for a subset of the `CharSequence` to match rather than the entire contents.
- Added reified overloads of `hasClass`, `doesNotHaveClass`, `isInstanceOf`, and `isNotInstanceOf`.
  For example: `isInstanceOf<String>()`.
- Added sequence assertions to mirror iterable
- Added array assertions for `UByteArray`, `UShortArray`, `UIntArray`, and `ULongArray`.

### Deprecated
- Lambda-accepting `assertThat { }` entrypoint is now deprecated.
  Use `assertThat(T)` for normal values or `assertFailure { }` for exception-throwing code.

### Fixed
- Fixed iterable assertions that take a block that could have multiple assertions.
  Includes: `none`,`atLeast`,`atMost`,`exactly`, and `any`

## [0.25] 2021-09-12

### Changed
- Minimum supported kotlin version is 1.5.30
- Minimum supported coroutines version is 1.5.2
- Replaced custom result type returned from `assertThat {}` with `kotlin.Result`. This is a binary-incompatible change,
  but it should be source-compatible in most cases as referencing the custom result type directly was discouraged.

### Added
- Added `startsWith()` and `endsWith()` for `List`.
- Added `Optional<>.isPresent()`, `Optional<>.isEmpty()`, `Optional<>.hasValue()`
- Added expanded set up apple targets for kotlin native.

### Fixed
- Fixed behavior of `isEqualToIgnoringGivenProperties` to only check accessible properties.
- Added `prop` which can take a member function. This restores much of the functionality deprecated
  in 0.23, while preserving the useful type-safety of the new overloads.

## [0.24] 2021-05-05

### Fixed
- Fixed `any` breaking when the list had several items.

### Added
- Added `containsSubList` for `List`.
- Added `exists` for `Path`.

## [0.23.1] 2021-02-03

### Fixed
- Fixed `containsExactly` failing on custom list types.
- Fixed NullPointerException in `isDataClassEqualTo` when a property is null.
- Fixed multiple failures breaking `any`
- Fixed map's `containsOnly` to ensure keys match up with values.
- Fixed map's contain methods null handling.

## [0.23] 2020-09-01

### Changed
- Minimum supported kotlin version is 1.4.0
- Multiplatform artifacts are published as a single artifact. You can now just write
  ```groovy
  sourceSets {
    commonTest {
      dependencies {
         implementation "com.willowtreeapps.assertk:assertk:..."
      }
    }
  }
  ```
instead of defining one for each platform.
- Added support for the Kotlin/JS [IR compiler](https://kotlinlang.org/docs/reference/js-ir-compiler.html)
- Added optional `displayActual` function to `assertThat` as an alternative to `.toString()` for external types
- Moved most `String` assertions to `CharSequence`

### Added
- Added `prop` function with `KProperty1` argument.
- Added `containsExactlyInAnyOrder` assertion for `Iterable` and primitive arrays
- Added initial Coroutines artifact (additional support planned for the future)

### Deprecated
- Deprecated `prop` function with `KCallable` argument. Use the new overload
with type-safe `KProperty1` argument or another overload with explicit name and lambda.

### Fixed
- Primitive array 'contains' methods now work with NaN. ex:
  ```kotlin
  assertThat(floatArrayOf(Float.Nan)).contains(Float.NaN)
  ```
  will pass when it failed before.
- Fixes a [bug](https://github.com/willowtreeapps/assertk/issues/314) causing failures to sometimes be dropped in a nested assertion context

## [0.22] 2020-03-11

### Added
- Add multi-value support for `Assert<String>.contains()` and `doesNotContain()`
- Add `isEqualByComparingTo` to compare objects by `compareTo` instead of `equals` this is useful for cases like
`BigDecimal` where `equals` would fail because of differing precision.
- Add `containsOnly` for arrays.

### Changed
- Minimum supported kotlin version is 1.3.70
- Updated opentest4j to 1.2.0. This changes the multiple assertion message to include each exception class name.
- Moved `containsAll`, `containsNone`, and `containsOnly` from `Collection` to `Iterable` to make
them a bit more flexible.
- `containsAll`, `containsNone`, and `containsOnly` error messages now include the expected and actual lists.
- Unwrap exceptions thrown by `prop(callable: KCallable<*>)` to make them more clear.
- Add all exception stacktraces to a `MultipleFailuresError` with `Throwable.addSurpressed` on the jvm (used when 
collecting multiple exceptions with `assertAll`). Unfortunately, if you are using gradle you won't see this due to a 
known [gradle issue](https://github.com/gradle/gradle/issues/9487).
- No longer wrap exceptions in `AssertionError`s when using `given` and `transform`. Warning: this is techinicaly a 
  breaking change as code like:
  ```kotlin
  try {
      assertThat(foo).given { throw MyException("error") }
  } catch (e: AssertionError) {
      // assume caught
  }
  ```
  will no longer be caught. But you shouldn't be writing code like that anyway ;)

### Fixed
- Don't let `assertAll` capture `OutOfMemory` errors.

### Breaking Changes
- Previously deprecated methods (as of 0.18) are now errors.

## [0.21] 2020-01-22

### Added
- Add `any` to iterable assertions. It will pass when any of the provided assertions pass.

### Changed
- Minimum supported kotlin version is 1.3.60
- Changed the signatures of `isEqualToWithGivenProperties` and `isEqualToIgnoringGivenProperties` to 
be able to take nullable properties.
- Improved the output of `containsExactly` to show the entire list.
- Render tabs and newlines in diffs to make clear what's different.

### Fixed
- Fixed issues with nested soft assertions. (https://github.com/willowtreeapps/assertk/issues/253)

## [0.20] 2019-09-13

### Added
-  Add `corresponds`/`doesNotCorrespond` to compare values with a correspondence function. This is
useful when the value doesn't implement `equals`.

### Changed
- Changed the signature of `isEqualTo` from `Any?` to `T`. This should not effect any existing code
due to `T` being covariant. The one improvement this brings is that literal numbers can be inferred 
to the correct type. Before this change `assertThat(1L).isEqualTo(1)` would fail, now it passes.

### Fixed
- Fixed `isSuccess` failing on null return values

## [0.19] 2019-07-20

### Fixed
- Fixed thread-safety issue with soft assertions

## [0.18] 2019-07-12

### Added
- Add `extracting` to allow extracting a value for each item in an array.
- Add `Assert<Result<T>>.isSuccess()` and `Assert<Result<T>>.isFailure()` to replace `AssertBlock` assertions.
- Add `messageContains` for throwable.

### Changed
- Minimum supported kotlin version is 1.3.40
- `assertThat {}` and `catch {}` are inlined for better coroutine support
- Improved display of `Pair` and `Triple`

### Breaking Changes
- `AssertBlock` is removed and it's methods have been turned into extension functions on `Assert<Result<T>>`.
You can migrate by:
  1. Alt-enter on `returnedValue`, `thrownError`, and `doesNotThrowAnyException` and select import.
  2. Alt-enter on the deprecated version of above and choose replace with...
  3. If your expression only has 1 value, you can replace ex: `isSucess().all { isEqualTo(1) }` with `isSuccess().isEqualTo(1)`

### Deprecated
- Deprecated `catch` in favor of `assertThat {}.isFailure()`

## [0.17] 2019-05-29

### Fixed
- Fixed transitive dep in js artifact

## [0.16] 2019-05-18

### Fixed
- Fixed js artifact

## [0.15] 2019-05-18

### Added
- Add `isEqualToIgnoringGivenProperties` for the JVM
- Add support for kotlin native
- Add `none` assertion for iterables

### Changed
- Minimum supported kotlin version is 1.3.30
- Common artifact changed from `assertk-common` to `assertk`
- All previous deprecations on now at level error

### Removed
- Deprecated `containsExactly` for maps

## [0.14] - 2019-04-27

### Added
- Add `assertThat` for Kotlin property based assertions.

### Fixed
- Fixed showing null expected/actual values in intellj when those values aren't provided. (#180)
- Fixed IndexOutOfBoundsException in containsExactly differ (#185)
- Fixed block asserts not respecting assertAll

## [0.13] - 2019-01-17

### Added
- Add `isDataClassEqualTo` for better messaging when comparing data classes.
- Add `matchesPredicate` to match against a predicate function/lambda.
- Add `atLeast` iterable assertion which passes if the assertion on each item passes at least n times.
- Add `isCloseTo` for floats and doubles to check that a value is within a delta of what's expected.
- Add `lines` and `bytes` to Path assertions for asserting on a file's contents.
- Add `containsOnly` for Collection assertions.

### Changed
- Sort `containsExactly` output by index to make it easier to compare.
- `containsAll` now includes the expected map.

### Fixed
- Number assertions now work correctly on BigInteger and BigDecimal
- Soft assertions breaking if exception is thrown.

### Deprecated
- Deprecated `assert` in favor of `assertThat`.
- Deprecated accessing the `actual` value on an assertion directly. Instead use `given` which will provide it in a lambda.
- Deprecated some methods that took a lambda, replacing them with versions that chain instead.
  * `isNotNull()`
  * `isInstanceOf()`
  * `index()`
  * `key()`

### Breaking Changes
- Previously deprecated methods (as of 0.10) are now errors.

## [0.12] - 2018-08-20

### Fixed
- Bumped the kotlin version to 1.2.50 to fix a packaging issue.
- Remove `<packaging>pom</packaging>` as it should be using the default jar packaging.

## [0.11] - 2018-08-04

### Added
- Split into multi-platform modules: `assertk-common`, `assertk-jvm`, and `assertk-js`
Important: this means the maven coordinate has changed for java projects, it is now `com.willowtreeapps:assertk:assertk-jvm:0.11`.
- Add `hasSameSizeAs`, `containsNone`, `index` assertions to array.
- Add `isEmpty`, `isNotEmpty`, `isNullOrEmpty`, `hasSize` assertions to map.
- Rename map's `containsExactly` to `containsOnly` to make it more clear that order doesn't matter.
- Add `length` prop for CharSequences.
- Un-deprecated several has* methods: `hasLength`, `hasSameLengthAs`, `hasMessage`, `hasCause`, `hasNoCause`, `hasRootCause`.
- `hasCause` and `hasRootCause` check exception type and message instead of using `equals`. This makes them more useful
as exceptions don't typically implement `equals`
- Uses [opentest4j](https://github.com/ota4j-team/opentest4j) assertions. This should allow better IDE integration etc.

## [0.10] - 2018-03-31

### Added
- Add docs to all assertions.
- Add Path assertions.
- Add InputStream assertions.
- `prop()`, `index()`, and `key()` to be able to easily assert on parts of a value.
- Replaced many 'has*' assertions with `prop()` getters. This allows you more flexibility when asserting and simplifies
the implementation. For example, instead of doing `assert(throwable).hasMessage(message)`, you do
`assert(throwable).message().isEqualTo(message)`. This gives you all the string assertions for free on the throwable
message.
- You now need to do `assert(value).all { ... }` instead of `assert(value) { ... }`. This makes the implementation more
consistent and better mirrors `assertAll { ... }` which has a similar behavior.
- New dependency on kotlin-reflect
- `contains`, `doesNotContain`, and `each` works on Iterable instead of Collection
- better error messages for collections
- more display types in `show()` (Char, Byte, Long, Float, primitive Arrays)
- Add `@DslMarker` to help catch misleading nested assertions.

### Breaking Changes
- Because of the above, `Assert<Collection>.all` and `Assert<Array>.all` have both been renamed to `each`.
- `Assert<Collection>.containsExactly` is now `Assert<List>.containsExactly` as ordering does not matter on all
collections (ex: `Set`).

### Deprecated
    src/main/kotlin/assertk/assert.kt
    76:@Deprecated(message = "Use assert(actual, name).all(f) instead.",
    77-        replaceWith = ReplaceWith("assert(actual, name).all(f)"))
    78-fun <T> assert(actual: T, name: String? = null, f: Assert<T>.() -> Unit) {

    src/main/kotlin/assertk/assertions/throwable.kt
    31:@Deprecated(message = "Use message().isEqualTo(message) instead.",
    32-        replaceWith = ReplaceWith("message().isEqualTo(message)"))
    33-fun <T : Throwable> Assert<T>.hasMessage(message: String?) {
    --
    43:@Deprecated(message = "Use cause().isEqualTo(cause) instead.",
    44-        replaceWith = ReplaceWith("cause().isEqualTo(cause)"))
    45-fun <T : Throwable> Assert<T>.hasCause(cause: Throwable) {
    --
    53:@Deprecated(message = "Use cause().isNull() instead.", replaceWith = ReplaceWith("cause().isNull()"))
    54-fun <T : Throwable> Assert<T>.hasNoCause() {
    55-    if (actual.cause == null) return
    --
    62:@Deprecated(message = "Use message().isNotNull { it.startsWith(prefix) } instead.",
    63-        replaceWith = ReplaceWith("message().isNotNull { it.startsWith(prefix) }"))
    64-fun <T : Throwable> Assert<T>.hasMessageStartingWith(prefix: String) {
    --
    73:@Deprecated(message = "Use message().isNotNull { it.contains(string) } instead.",
    74-        replaceWith = ReplaceWith("message().isNotNull { it.contains(string) }"))
    75-fun <T : Throwable> Assert<T>.hasMessageContaining(string: String) {
    --
    84:@Deprecated(message = "Use message().isNotNull { it.matches(regex) } instead.",
    85-        replaceWith = ReplaceWith("message().isNotNull { it.matches(regex) }"))
    86-fun <T : Throwable> Assert<T>.hasMessageMatching(regex: Regex) {
    --
    95:@Deprecated(message = "Use message().isNotNull { it.endsWith(suffix) } instead.",
    96-        replaceWith = ReplaceWith("message().isNotNull { it.endsWith(suffix) }"))
    97-fun <T : Throwable> Assert<T>.hasMessageEndingWith(suffix: String) {
    --
    108:@Deprecated(message = "Use cause().isNotNull { it.isInstanceOf(kclass) } instead.",
    109-        replaceWith = ReplaceWith("cause().isNotNull { it.isInstanceOf(kclass) }"))
    110-fun <T : Throwable> Assert<T>.hasCauseInstanceOf(kclass: KClass<out T>) {
    --
    121:@Deprecated(message = "Use cause().isNotNull { it.isInstanceOf(jclass) } instead.",
    122-        replaceWith = ReplaceWith("cause().isNotNull { it.isInstanceOf(jclass) }"))
    123-fun <T : Throwable> Assert<T>.hasCauseInstanceOf(jclass: Class<out T>) {
    --
    134:@Deprecated(message = "Use cause().isNotNull { it.kClass().isEqualTo(kclass) } instead.",
    135-        replaceWith = ReplaceWith("cause().isNotNull { it.kClass().isEqualTo(kclass) }"))
    136-fun <T : Throwable> Assert<T>.hasCauseWithClass(kclass: KClass<out T>) {
    --
    147:@Deprecated(message = "Use cause().isNotNull { it.jClass().isEqualTo(jclass) } instead.",
    148-        replaceWith = ReplaceWith("cause().isNotNull { it.jClass().isEqualTo(jclass) }"))
    149-fun <T : Throwable> Assert<T>.hasCauseWithClass(jclass: Class<out T>) {
    --
    158:@Deprecated(message = "Use rootCause().isEqualTo(cause) instead.",
    159-        replaceWith = ReplaceWith("rootCause().isEqualTo(cause)"))
    160-fun <T : Throwable> Assert<T>.hasRootCause(cause: Throwable) {
    --
    170:@Deprecated(message = "Use rootCause().isInstanceOf(kclass) instead.",
    171-        replaceWith = ReplaceWith("rootCause().isInstanceOf(kclass)"))
    172-fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(kclass: KClass<out T>) {
    --
    183:@Deprecated(message = "Use rootCause().isInstanceOf(jclass) instead.",
    184-        replaceWith = ReplaceWith("rootCause().isInstanceOf(jclass)"))
    185-fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(jclass: Class<out T>) {
    --
    196:@Deprecated(message = "Use rootCause().isNotNull { it.kClass().isEqualTo(kclass) } instead.",
    197-        replaceWith = ReplaceWith("rootCause().isNotNull { it.kClass().isEqualTo(kclass) }"))
    198-fun <T : Throwable> Assert<T>.hasRootCauseWithClass(kclass: KClass<out T>) {
    --
    209:@Deprecated(message = "Use rootCause().isNotNull { it.jClass().isEqualTo(jclass) } instead.",
    210-        replaceWith = ReplaceWith("rootCause().isNotNull { it.jClass().isEqualTo(jclass) }"))
    211-fun <T : Throwable> Assert<T>.hasRootCauseWithClass(jclass: Class<out T>) {
    --
    223:@Deprecated(message = "Use stackTrace().contains(description) instead.",
    224-        replaceWith = ReplaceWith("stackTrace().contains(description)"))
    225-fun <T : Throwable> Assert<T>.hasStackTraceContaining(description: String) {

    src/main/kotlin/assertk/assertions/charsequence.kt
    47:@Deprecated("Use length().isEqualTo(length) instead.",
    48-        replaceWith = ReplaceWith("length().isEqualTo(length)"))
    49-fun <T : CharSequence> Assert<T>.hasLength(length: Int) {
    --
    57:@Deprecated("Use length().isEqualTo(other.length) instead.",
    58-        replaceWith = ReplaceWith("length().isEqualTo(other.length)"))
    59-fun <T : CharSequence> Assert<T>.hasSameLengthAs(other: CharSequence) {

    src/main/kotlin/assertk/assertions/file.kt
    86:@Deprecated(message = "Use name().isEqualTo(expected) instead",
    87-        replaceWith = ReplaceWith("name().isEqualTo(expected)"))
    88-fun Assert<File>.hasName(expected: String) {
    --
    95:@Deprecated(message = "Use path().isEqualTo(expected) instead",
    96-        replaceWith = ReplaceWith("path().isEqualTo(expected)"))
    97-fun Assert<File>.hasPath(expected: String) {
    --
    104:@Deprecated(message = "Use parent().isEqualTo(expected) instead",
    105-        replaceWith = ReplaceWith("parent().isEqualTo(expected)"))
    106-fun Assert<File>.hasParent(expected: String) {
    --
    113:@Deprecated(message = "Use extension().isEqualTo(expected) instead",
    114-        replaceWith = ReplaceWith("extension().isEqualTo(expected)"))
    115-fun Assert<File>.hasExtension(expected: String) {
    --
    125:@Deprecated(message = "Use text(charset).isEqualTo(expected) instead",
    126-        replaceWith = ReplaceWith("text(charset).isEqualTo(expected)"))
    127-fun Assert<File>.hasText(expected: String, charset: Charset = Charsets.UTF_8) {
    --
    138:@Deprecated(message = "Use text(charset).contains(expected) instead",
    139-        replaceWith = ReplaceWith("text(charset).contains(expected)"))
    140-fun Assert<File>.containsText(expected: String, charset: Charset = Charsets.UTF_8) {
    --
    151:@Deprecated(message = "Use text(charset).matches(expected) instead",
    152-        replaceWith = ReplaceWith("text(charset).matches(expected)"))
    153-fun Assert<File>.matchesText(expected: Regex, charset: Charset = Charsets.UTF_8) {

    src/main/kotlin/assertk/assertions/any.kt
    85:@Deprecated(message = "Use kClass().isEqualTo(kclass) instead.",
    86-        replaceWith = ReplaceWith("kClass().isEqualTo(kclass)"))
    87-fun <T : Any> Assert<T>.hasClass(kclass: KClass<out T>) {
    --
    99:@Deprecated(message = "Use jClass().isEqualTo(jclass) instead.",
    100-        replaceWith = ReplaceWith("jClass().isEqualTo(jclass)"))
    101-fun <T : Any> Assert<T>.hasClass(jclass: Class<out T>) {
    --
    113:@Deprecated(message = "Use kClass().isNotEqualTo(kclass) instead.",
    114-        replaceWith = ReplaceWith("kClass().isNotEqualTo(kclass)"))
    115-fun <T : Any> Assert<T>.doesNotHaveClass(kclass: KClass<out T>) {
    --
    128:@Deprecated(message = "Use jClass().isNotEqualTo(jclass) instead.",
    129-        replaceWith = ReplaceWith("jClass().isNotEqualTo(jclass)"))
    130-fun <T : Any> Assert<T>.doesNotHaveClass(jclass: Class<out T>) {
    --
    202:@Deprecated(message = "Use toStringFun().isEqualTo(string) instead.",
    203-        replaceWith = ReplaceWith("toStringFun().isEqualTo(string)"))
    204-fun <T> Assert<T>.hasToString(string: String) {
    --
    213:@Deprecated(message = "Use hashCodeFun().isEqualTo(hashCode) instead.",
    214-        replaceWith = ReplaceWith("hashCodeFun().isEqualTo(hashCode)"))
    215-fun <T : Any> Assert<T>.hasHashCode(hashCode: Int) {

### Fixed
- Fix typos on a few assertions.
- Fix bug in array `containsExactly`.
- Fix issue with isEqualTo and nullable java strings

## [0.9] - 2017-09-25
- Initial Release
