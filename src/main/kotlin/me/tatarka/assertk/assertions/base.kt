package me.tatarka.assertk.assertions

import me.tatarka.assertk.Assert
import kotlin.reflect.KClass
import kotlin.text.Regex

// Object
fun <T> Assert<T>.isEqualTo(expected: Any?) {
    if (actual == expected) {
    } else {
        fail(expected, actual)
    }
}

fun <T> Assert<T>.isNotEqualTo(expected: Any?) {
    if (actual != expected) {
    } else {
        expected(":${show(expected)} not to be equal to:${show(actual)}")
    }
}

fun <T> Assert<T>.isSameAs(expected: T) {
    if (actual === expected) {
    } else {
        expected(":${show(expected)} and:${show(actual)} to refer to the same object")
    }
}

fun <T> Assert<T>.isNotSameAs(expected: Any?) {
    if (actual !== expected) {
    } else {
        expected(":${show(expected)} to not refer to the same object")
    }
}

fun <T : Any> Assert<T>.hasClass(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (jclass.equals(actual.javaClass)) {
    } else {
        expected("to have class:${show(jclass)} but was:${show(actual.javaClass)}")
    }
}

fun <T : Any> Assert<T>.hasClass(jclass: Class<out T>) {
    if (jclass.equals(actual.javaClass)) {
    } else {
        expected("to have class:${show(jclass)} but was:${show(actual.javaClass)}")
    }
}

fun <T : Any> Assert<T>.doesNotHaveClass(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (!jclass.equals(actual.javaClass)) {
    } else {
        expected("to not have class:${show(jclass)}")
    }
}

fun <T : Any> Assert<T>.doesNotHaveClass(jclass: Class<out T>) {
    if (!jclass.equals(actual.javaClass)) {
    } else {
        expected("to not have class:${show(jclass)}")
    }
}

fun <T : Any> Assert<T>.isInstanceOf(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (jclass.isInstance(actual)) {
    } else {
        expected("to be instance of:${show(jclass)} but had class:${show(actual.javaClass)}")
    }
}

fun <T : Any> Assert<T>.isInstanceOf(jclass: Class<out T>) {
    if (jclass.isInstance(actual)) {
    } else {
        expected("to be instance of:${show(jclass)} but had class:${show(actual.javaClass)}")
    }
}

fun <T : Any> Assert<T>.isNotInstanceOf(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (!jclass.isInstance(actual)) {
    } else {
        expected("to not be instance of:${show(jclass)}")
    }
}

fun <T : Any> Assert<T>.isNotInstanceOf(jclass: Class<out T>) {
    if (!jclass.isInstance(actual)) {
    } else {
        expected("to not be instance of:${show(jclass)}")
    }
}

fun <T> Assert<out T>.isIn(vararg values: T) {
    if (actual in values) {
    } else {
        expected(":${show(values)} to contain:${show(actual)}")
    }
}

fun <T> Assert<out T>.isNotIn(vararg values: T) {
    if (actual !in values) {
    } else {
        expected(":${show(values)} to not contain:${show(actual)}")
    }
}

fun <T> Assert<T>.hasToString(string: String) {
    val result = actual.toString()
    if (result == string) {
    } else {
        expected("toString() to be:${show(string)} but was:${show(result)}")
    }
}

fun <T : Any> Assert<T>.hasHashCode(hashCode: Int) {
    val result = actual.hashCode()
    if (result == hashCode) {
    } else {
        expected("hashCode() to be:${show(hashCode)} but was:${show(result)}")
    }
}

// Object nullable
fun <T : Any> Assert<T?>.isNull() {
    if (actual == null) {
    } else {
        expected("null but was:${show(actual)}")
    }
}

fun <T : Any> Assert<T?>.isNotNull(f: (Assert<T>) -> Unit = {}) {
    if (actual != null) {
        assert(actual, f)
    } else {
        expected("to not be null")
    }
}

// Throwable
fun <T : Throwable> Assert<T>.hasMessage(message: String?) {
    assert(actual.message).named("message").isNotNull {
        it.isEqualTo(message)
    }
}

fun <T : Throwable> Assert<T>.hasCause(cause: Throwable) {
    assert(actual.cause).named("cause").isNotNull {
        it.isEqualTo(cause)
    }
}

fun <T : Throwable> Assert<T>.hasNoCause() {
    if (actual.cause == null) {
    } else {
        named("cause").expected("to not exist but was:${show(actual.cause)}")
    }
}

fun <T : Throwable> Assert<T>.hasMessageStartingWith(prefix: String) {
    assert(actual.message).named("message").isNotNull {
        it.startsWith(prefix)
    }
}

fun <T : Throwable> Assert<T>.hasMessageContaining(string: String) {
    assert(actual.message).named("message").isNotNull {
        it.contains(string)
    }
}

fun <T : Throwable> Assert<T>.hasMessageMatching(regex: Regex) {
    assert(actual.message).named("message").isNotNull {
        it.matches(regex)
    }
}

fun <T : Throwable> Assert<T>.hasMessageEndingWith(suffix: String) {
    assert(actual.message).named("message").isNotNull {
        it.endsWith(suffix)
    }
}

fun <T : Throwable> Assert<T>.hasCauseInstanceOf(kclass: KClass<out T>) {
    assert(actual.cause).named("cause").isNotNull {
        it.isInstanceOf(kclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseInstanceOf(jclass: Class<out T>) {
    assert(actual.cause).named("cause").isNotNull {
        it.isInstanceOf(jclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseWithClass(kclass: KClass<out T>) {
    assert(actual.cause).named("cause").isNotNull {
        it.hasClass(kclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseWithClass(jclass: Class<out T>) {
    assert(actual.cause).named("cause").isNotNull {
        it.hasClass(jclass)
    }
}

fun <T : Throwable> Assert<T>.hasRootCause(cause: Throwable) {
}

fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(kclass: KClass<out T>) {

}

fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(jclass: Class<out T>) {

}

fun <T : Throwable> Assert<T>.hasRootCauseWithClass(kclass: KClass<out T>) {

}

fun <T : Throwable> Assert<T>.hasRootCauseWithClass(jclass: Class<out T>) {

}

// Boolean
fun Assert<Boolean>.isTrue() {
    if (actual) {
    } else {
        expected("to be true")
    }
}

fun Assert<Boolean>.isFalse() {
    if (!actual) {
    } else {
        expected("to be false")
    }
}

// Number
fun <T : Number> Assert<T>.isZero() {
    if (actual == 0) {
    } else {
        expected("to be 0 but was:${show(actual)}")
    }
}

fun <T> Assert<T>.isPositive() where T : Number, T : Comparable<T> {
    if (actual > 0 as T) {
    } else {
        expected("to be positive but was:${show(actual)}")
    }
}

fun <T> Assert<T>.isNegative() where T : Number, T : Comparable<T> {
    if (actual < 0 as T) {
    } else {
        expected("to be negative but was:${show(actual)}")
    }
}

// CharSequence
@JvmName("isStringEmpty")
fun <T : CharSequence> Assert<T>.isEmpty() {
    if (actual.length == 0) {
    } else {
        expected("to be empty but was:${show(actual)}")
    }
}

@JvmName("isStringNotEmpty")
fun <T : CharSequence> Assert<T>.isNotEmpty() {
    if (actual.length > 0) {
    } else {
        expected("to to not be empty")
    }
}

@JvmName("isStringNullOrEmpty")
fun <T : CharSequence?> Assert<T>.isNullOrEmpty() {
    if (actual == null || actual.length == 0) {
    } else {
        expected("to be null or empty but was:${show(actual)}")
    }
}

@JvmName("stringHasSize")
fun <T : CharSequence> Assert<T>.hasLength(length: Int) {
    val actualLength = actual.length
    if (actualLength == length) {
    } else {
        expected("to have length:${show(length)} but was length:${show(actualLength)}")
    }
}

fun <T : CharSequence> Assert<T>.hasSameLengthAs(other: CharSequence) {
    val actualLength = actual.length
    val otherLength = other.length
    if (actualLength == otherLength) {
    } else {
        expected("to have same length as:${show(other)} ($otherLength) but was:${show(actual)} ($actualLength)")
    }
}

// String
fun Assert<String>.hasLineCount(lineCount: Int) {
    val actualLineCount = actual.lines().size
    if (actualLineCount == lineCount) {
    } else {
        expected("To have line count:${show(lineCount)} but was:${show(actualLineCount)}")
    }
}

fun Assert<String>.isEqualTo(other: String, ignoreCase: Boolean = false) {
    if (actual.equals(other, ignoreCase)) {
    } else {
        fail(other, actual)
    }
}

fun Assert<String>.isNotEqualTo(other: String, ignoreCase: Boolean = false) {
    if (!actual.equals(other, ignoreCase)) {
    } else {
        expected(":${show(other)} not to be equal to:${show(actual)}")
    }
}

fun Assert<String>.contains(other: CharSequence, ignoreCase: Boolean = false) {
    if (actual.contains(other, ignoreCase)) {
    } else {
        expected("to contain:${show(other)} but was:${show(actual)}")
    }
}

fun Assert<String>.startsWith(other: String, ignoreCase: Boolean = false) {
    if (actual.startsWith(other, ignoreCase)) {
    } else {
        expected("to start with:${show(other)} but was:${show(actual)}")
    }
}

fun Assert<String>.endsWith(other: String, ignoreCase: Boolean = false) {
    if (actual.endsWith(other, ignoreCase)) {
    } else {
        expected("to end with:${show(other)} but was:${show(actual)}")
    }
}

fun Assert<String>.matches(regex: Regex) {
    if (actual.matches(regex)) {
    } else {
        expected("to match:${show(regex)} but was:${show(actual)}")
    }
}

// Comparable
fun <A, B : Comparable<A>> Assert<B>.isGreaterThan(other: A) {
    if (actual > other) {
    } else {
        expected("to be greater than:${show(other)} but was:${show(actual)}")
    }
}

fun <A, B : Comparable<A>> Assert<B>.isLessThan(other: A) {
    if (actual < other) {
    } else {
        expected("to be less than:${show(other)} but was:${show(actual)}")
    }
}

fun <A, B : Comparable<A>> Assert<B>.isGreaterThanOrEqualTo(other: A) {
    if (actual >= other) {
    } else {
        expected("to be greater than or equal to:${show(other)} but was:${show(actual)}")
    }
}

fun <A, B : Comparable<A>> Assert<B>.isLessThanOrEqualTo(other: A) {
    if (actual <= other) {
    } else {
        expected("to be less than or equal to:${show(other)} but was:${show(actual)}")
    }
}

fun <A, B : Comparable<A>> Assert<B>.isBetween(start: A, end: A) {
    if (actual >= start && actual <= end) {
    } else {
        expected("to be between:${show(start)} and ${show(end)} but was:${show(actual)}")
    }
}

fun <A, B : Comparable<A>> Assert<B>.isStrictlyBetween(start: A, end: A) {
    if (actual > start && actual < end) {
    } else {
        expected("to be strictly between:${show(start)} and ${show(end)} but was:${show(actual)}")
    }
}

// Collection
fun <T : Collection<*>> Assert<T>.isEmpty() {
    if (actual.isEmpty()) {
    } else {
        expected("to be empty but was:${show(actual)}")
    }
}

fun <T : Collection<*>> Assert<T>.isNotEmpty() {
    if (!actual.isNotEmpty()) {
        expected("to not be empty")
    }
}

fun <T : Collection<*>?> Assert<T>.isNullOrEmpty() {
    if (actual == null || actual.isEmpty()) {
    } else {
        expected("to be null or empty but was:${show(actual)}")
    }
}

fun <T : Collection<*>> Assert<T>.hasLength(size: Int) {
    val actualSize = actual.size
    if (actualSize == size) {
    } else {
        expected("to have size:${show(size)} but was size:$actualSize")
    }
}

fun <T : Collection<*>> Assert<T>.hasSameLengthAs(other: Collection<*>) {
    val actualSize = actual.size
    val otherSize = other.size
    if (actualSize == otherSize) {
    } else {
        expected("to have same size as:${show(other)} ($otherSize) but was size:($actualSize)")
    }
}
