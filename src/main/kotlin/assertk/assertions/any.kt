package assertk.assertions

import assertk.Assert
import assertk.assert
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show
import kotlin.reflect.KClass

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

fun <T> Assert<T>.isIn(vararg values: T) {
    if (actual in values) return
    expected(":${show(values)} to contain:${show(actual)}")
}

fun <T> Assert<T>.isNotIn(vararg values: T) {
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

// nullable
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
