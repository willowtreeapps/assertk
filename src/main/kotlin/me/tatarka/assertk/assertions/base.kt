package me.tatarka.assertk.assertions

import me.tatarka.assertk.Assert
import me.tatarka.assertk.assert
import me.tatarka.assertk.assertions.support.expected
import me.tatarka.assertk.assertions.support.fail
import me.tatarka.assertk.assertions.support.show
import kotlin.reflect.KClass

// Object
fun <T> Assert<T>.isEqualTo(expected: Any?) {
    if (actual == expected) return
    fail(expected, actual)
}

fun <T> Assert<T>.isNotEqualTo(expected: Any?) {
    if (actual != expected) return
    expected(":${show(expected)} not to be equal to:${show(actual)}")
}

fun <T> Assert<T>.isSameAs(expected: T) {
    if (actual === expected) return
    expected(":${show(expected)} and:${show(actual)} to refer to the same object")
}

fun <T> Assert<T>.isNotSameAs(expected: Any?) {
    if (actual !== expected) return
    expected(":${show(expected)} to not refer to the same object")
}

fun <T : Any> Assert<T>.hasClass(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (jclass == actual.javaClass) return
    expected("to have class:${show(jclass)} but was:${show(actual.javaClass)}")
}

fun <T : Any> Assert<T>.hasClass(jclass: Class<out T>) {
    if (jclass == actual.javaClass) return
    expected("to have class:${show(jclass)} but was:${show(actual.javaClass)}")
}

fun <T : Any> Assert<T>.doesNotHaveClass(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (jclass != actual.javaClass) return
    expected("to not have class:${show(jclass)}")
}

fun <T : Any> Assert<T>.doesNotHaveClass(jclass: Class<out T>) {
    if (jclass != actual.javaClass) return
    expected("to not have class:${show(jclass)}")
}

fun <T : Any> Assert<T>.isInstanceOf(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (jclass.isInstance(actual)) return
    expected("to be instance of:${show(jclass)} but had class:${show(actual.javaClass)}")
}

fun <T : Any> Assert<T>.isInstanceOf(jclass: Class<out T>) {
    if (jclass.isInstance(actual)) return
    expected("to be instance of:${show(jclass)} but had class:${show(actual.javaClass)}")
}

fun <T : Any> Assert<T>.isNotInstanceOf(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (!jclass.isInstance(actual)) return
    expected("to not be instance of:${show(jclass)}")
}

fun <T : Any> Assert<T>.isNotInstanceOf(jclass: Class<out T>) {
    if (!jclass.isInstance(actual)) return
    expected("to not be instance of:${show(jclass)}")
}

fun <T> Assert<out T>.isIn(vararg values: T) {
    if (actual in values) return
    expected(":${show(values)} to contain:${show(actual)}")
}

fun <T> Assert<out T>.isNotIn(vararg values: T) {
    if (actual !in values) return
    expected(":${show(values)} to not contain:${show(actual)}")
}

fun <T> Assert<T>.hasToString(string: String) {
    val result = actual.toString()
    if (result == string) return
    expected("toString() to be:${show(string)} but was:${show(result)}")
}

fun <T : Any> Assert<T>.hasHashCode(hashCode: Int) {
    val result = actual.hashCode()
    if (result == hashCode) return
    expected("hashCode() to be:${show(hashCode)} but was:${show(result)}")
}

// Object nullable
fun <T : Any> Assert<T?>.isNull() {
    if (actual == null) return
    expected("null but was:${show(actual)}")
}

fun <T : Any> Assert<T?>.isNotNull(f: (Assert<T>) -> Unit = {}) {
    if (actual != null) {
        assert(name, actual, f)
    } else {
        expected("to not be null")
    }
}

// Throwable
fun <T : Throwable> Assert<T>.hasMessage(message: String?) {
    assert("message", actual.message).isNotNull {
        it.isEqualTo(message)
    }
}

fun <T : Throwable> Assert<T>.hasCause(cause: Throwable) {
    assert("cause", actual.cause).isNotNull {
        it.isEqualTo(cause)
    }
}

fun <T : Throwable> Assert<T>.hasNoCause() {
    if (actual.cause == null) return
    expected("[cause] to not exist but was:${show(actual.cause)}")
}

fun <T : Throwable> Assert<T>.hasMessageStartingWith(prefix: String) {
    assert("message", actual.message).isNotNull {
        it.startsWith(prefix)
    }
}

fun <T : Throwable> Assert<T>.hasMessageContaining(string: String) {
    assert("message", actual.message).isNotNull {
        it.contains(string)
    }
}

fun <T : Throwable> Assert<T>.hasMessageMatching(regex: Regex) {
    assert("message", actual.message).isNotNull {
        it.matches(regex)
    }
}

fun <T : Throwable> Assert<T>.hasMessageEndingWith(suffix: String) {
    assert("message", actual.message).isNotNull {
        it.endsWith(suffix)
    }
}

fun <T : Throwable> Assert<T>.hasCauseInstanceOf(kclass: KClass<out T>) {
    assert("cause", actual.cause).isNotNull {
        it.isInstanceOf(kclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseInstanceOf(jclass: Class<out T>) {
    assert("cause", actual.cause).isNotNull {
        it.isInstanceOf(jclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseWithClass(kclass: KClass<out T>) {
    assert("cause", actual.cause).isNotNull {
        it.hasClass(kclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseWithClass(jclass: Class<out T>) {
    assert("cause", actual.cause).isNotNull {
        it.hasClass(jclass)
    }
}

fun <T : Throwable> Assert<T>.hasRootCause(cause: Throwable) {
    assert("root cause", actual.rootCause()).isEqualTo(cause)

}

fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(kclass: KClass<out T>) {
    assert("root cause", actual.rootCause()).isInstanceOf(kclass)
}

fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(jclass: Class<out T>) {
    assert("root cause", actual.rootCause()).isInstanceOf(jclass)
}

fun <T : Throwable> Assert<T>.hasRootCauseWithClass(kclass: KClass<out T>) {
    assert("root cause", actual.rootCause()).hasClass(kclass)
}

fun <T : Throwable> Assert<T>.hasRootCauseWithClass(jclass: Class<out T>) {
    assert("root cause", actual.rootCause()).hasClass(jclass)
}

private fun Throwable.rootCause(): Throwable =
        this.cause?.rootCause() ?: this

fun <T : Throwable> Assert<T>.hasStackTraceContaining(description: String) {
    assert("stack trace", actual.stackTrace.map { it.toString() }).contains(description)
}

// Boolean
fun Assert<Boolean>.isTrue() {
    if (actual) return
    expected("to be true")
}

fun Assert<Boolean>.isFalse() {
    if (!actual) return
    expected("to be false")
}

// Number
fun <T : Number> Assert<T>.isZero() {
    if (actual == 0) return
    expected("to be 0 but was:${show(actual)}")
}

fun <T : Number> Assert<T>.isNotZero() {
    if (actual != 0) return
    expected("to not be 0")
}

fun <T> Assert<T>.isPositive() where T : Number, T : Comparable<T> {
    if (actual > 0 as T) return
    expected("to be positive but was:${show(actual)}")
}

fun <T> Assert<T>.isNegative() where T : Number, T : Comparable<T> {
    if (actual < 0 as T) return
    expected("to be negative but was:${show(actual)}")
}

// CharSequence
@JvmName("isStringEmpty")
fun <T : CharSequence> Assert<T>.isEmpty() {
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

@JvmName("isStringNotEmpty")
fun <T : CharSequence> Assert<T>.isNotEmpty() {
    if (actual.isNotEmpty()) return
    expected("to to not be empty")
}

@JvmName("isStringNullOrEmpty")
fun <T : CharSequence?> Assert<T>.isNullOrEmpty() {
    if (actual.isNullOrEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

@JvmName("stringHasSize")
fun <T : CharSequence> Assert<T>.hasLength(length: Int) {
    assert("length", actual.length).isEqualTo(length)
}

fun <T : CharSequence> Assert<T>.hasSameLengthAs(other: CharSequence) {
    val actualLength = actual.length
    val otherLength = other.length
    if (actualLength == otherLength) return
    expected("to have same length as:${show(other)} ($otherLength) but was:${show(actual)} ($actualLength)")
}

// String
fun Assert<String>.hasLineCount(lineCount: Int) {
    val actualLineCount = actual.lines().size
    if (actualLineCount == lineCount) return
    expected("To have line count:${show(lineCount)} but was:${show(actualLineCount)}")
}

fun Assert<String>.isEqualTo(other: String, ignoreCase: Boolean = false) {
    if (actual.equals(other, ignoreCase)) return
    fail(other, actual)
}

fun Assert<String>.isNotEqualTo(other: String, ignoreCase: Boolean = false) {
    if (!actual.equals(other, ignoreCase)) return
    expected(":${show(other)} not to be equal to:${show(actual)}")
}

fun Assert<String>.contains(other: CharSequence, ignoreCase: Boolean = false) {
    if (actual.contains(other, ignoreCase)) return
    expected("to contain:${show(other)} but was:${show(actual)}")
}

fun Assert<String>.startsWith(other: String, ignoreCase: Boolean = false) {
    if (actual.startsWith(other, ignoreCase)) return
    expected("to start with:${show(other)} but was:${show(actual)}")
}

fun Assert<String>.endsWith(other: String, ignoreCase: Boolean = false) {
    if (actual.endsWith(other, ignoreCase)) return
    expected("to end with:${show(other)} but was:${show(actual)}")
}

fun Assert<String>.matches(regex: Regex) {
    if (actual.matches(regex)) return
    expected("to match:${show(regex)} but was:${show(actual)}")
}

// Comparable
fun <A, B : Comparable<A>> Assert<B>.isGreaterThan(other: A) {
    if (actual > other) return
    expected("to be greater than:${show(other)} but was:${show(actual)}")
}

fun <A, B : Comparable<A>> Assert<B>.isLessThan(other: A) {
    if (actual < other) return
    expected("to be less than:${show(other)} but was:${show(actual)}")
}

fun <A, B : Comparable<A>> Assert<B>.isGreaterThanOrEqualTo(other: A) {
    if (actual >= other) return
    expected("to be greater than or equal to:${show(other)} but was:${show(actual)}")
}

fun <A, B : Comparable<A>> Assert<B>.isLessThanOrEqualTo(other: A) {
    if (actual <= other) return
    expected("to be less than or equal to:${show(other)} but was:${show(actual)}")
}

fun <A, B : Comparable<A>> Assert<B>.isBetween(start: A, end: A) {
    if (actual >= start && actual <= end) return
    expected("to be between:${show(start)} and ${show(end)} but was:${show(actual)}")
}

fun <A, B : Comparable<A>> Assert<B>.isStrictlyBetween(start: A, end: A) {
    if (actual > start && actual < end) return
    expected("to be strictly between:${show(start)} and ${show(end)} but was:${show(actual)}")
}

// Collection
fun <T : Collection<*>> Assert<T>.isEmpty() {
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

fun <T : Collection<*>> Assert<T>.isNotEmpty() {
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

fun <T : Collection<*>?> Assert<T>.isNullOrEmpty() {
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

fun <T : Collection<*>> Assert<T>.hasSize(size: Int) {
    assert("size", actual.size).isEqualTo(size)
}

fun <T : Collection<*>> Assert<T>.hasSameSizeAs(other: Collection<*>) {
    val actualSize = actual.size
    val otherSize = other.size
    if (actualSize == otherSize) return
    expected("to have same size as:${show(other)} ($otherSize) but was size:($actualSize)")
}

fun <T : Collection<*>> Assert<T>.contains(element: Any?) {
    if (element in actual) return
    expected("to contain:${show(element)} but was:${show(actual)}")
}

fun <T : Collection<*>> Assert<T>.doesNotContain(element: Any?) {
    if (element !in actual) return
    expected("to not contain:${show(element)} but was:${show(actual)}")
}

fun <T : Collection<*>> Assert<T>.containsAll(vararg elements: Any?) {
    if (actual.containsAll(elements.toList())) return
    expected("to contain all:${show(elements)} but was${show(actual)}")
}

fun <T : Collection<*>> Assert<T>.containsExactly(vararg elements: Any?) {
    val itr = actual.iterator()
    var i = 0
    while (itr.hasNext()) {
        if (itr.next() != elements[i]) {
            expected("to contain exactly:${show(elements)} but was:${show(actual)}")
        }
        i += 1
    }
}

fun <E, T : Collection<E>> Assert<T>.all(f: (Assert<E>) -> Unit) {
    for (item in actual) {
        f(assert(name, item))
    }
}

// Array
@JvmName("arrayIsEmpty")
fun <T> Assert<Array<T>>.isEmpty() {
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

@JvmName("arrayIsNotEmpty")
fun <T> Assert<Array<T>>.isNotEmpty() {
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

@JvmName("arrayIsNullOrEmpty")
fun <T> Assert<Array<T>?>.isNullOrEmpty() {
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

@JvmName("arrayHasSize")
fun <T> Assert<Array<T>>.hasSize(size: Int) {
    assert("size", actual.size).isEqualTo(size)
}

@JvmName("arrayContains")
fun <T> Assert<Array<T>>.contains(element: Any?) {
    if (element in actual) return
    expected("to contain ${show(element)} but was:${show(actual)}")
}

@JvmName("arrayDoesNotContain")
fun <T> Assert<Array<T>>.doesNotContain(element: Any?) {
    if (element !in actual) return
    expected("to not contain ${show(element)} but was:${show(actual)}")
}

@JvmName("arrayContainsAll")
fun <T> Assert<Array<T>>.containsAll(vararg elements: Any?) {
    if (elements.all { actual.contains(it) }) return
    expected("to contain all ${show(elements)} but was${show(actual)}")
}

@JvmName("arrayContainsExactly")
fun <T> Assert<Array<T>>.containsExactly(vararg elements: Any?) {
    if ((0..elements.size - 1).all { i -> actual[i] == elements[i] }) return
    expected("to contain exactly:${show(elements)} but was:${show(actual)}")
}

@JvmName("arrayAll")
fun <T> Assert<Array<T>>.all(f: (Assert<T>) -> Unit) {
    for (item in actual) {
        f(assert(name, item))
    }
}
