# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Split into multi-platform modules
- Add `hasSameSizeAs`, `containsNone`, `index` assertions to array.
- Add `isEmpty`, `isNotEmpty`, `isNullOrEmpty`, `hasSize` assertions to map.
- Rename map's `containsExactly` to `containsOnly` to make it more clear that order doesn't matter.
- Add `length` prop for CharSequences.
- Un-deprecated several has* methods: `hasLength`, `hasSameLengthAs`, `hasMessage`, `hasCause`, `hasNoCause`, `hasRootCause`.
- `hasCause` and `hasRootCause` check exception type and message instead of using `equals`. This makes them more useful
as exceptions don't typically implement `equals`

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
