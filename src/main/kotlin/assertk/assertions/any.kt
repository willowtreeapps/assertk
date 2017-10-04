package assertk.assertions

import assertk.Assert
import assertk.assert
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show
import kotlin.reflect.KClass

/**
 * Asserts the value is equal to the expected one, using `==`.
 * @see [isNotEqualTo]
 * @see [isSameAs]
 */
fun <T> Assert<T>.isEqualTo(expected: Any?) {
    if (actual == expected) return
    fail(expected, actual)
}

/**
 * Asserts the value is not equal to the expected one, using `!=`.
 * @see [isEqualTo]
 * @see [isNotSameAs]
 */
fun <T> Assert<T>.isNotEqualTo(expected: Any?) {
    if (actual != expected) return
    expected(":${show(expected)} not to be equal to:${show(actual)}")
}

/**
 * Asserts the value is the same as the expected one, using `===`.
 * @see [isNotSameAs]
 * @see [isEqualTo]
 */
fun <T> Assert<T>.isSameAs(expected: T) {
    if (actual === expected) return
    expected(":${show(expected)} and:${show(actual)} to refer to the same object")
}

/**
 * Asserts the value is not the same as the expected one, using `!==`.
 * @see [isSameAs]
 * @see [isNotEqualTo]
 */
fun <T> Assert<T>.isNotSameAs(expected: Any?) {
    if (actual !== expected) return
    expected(":${show(expected)} to not refer to the same object")
}

/**
 * Asserts the value has the expected kotlin class. This is an exact match, so `assert("test").hasClass(String::class)`
 * is succesful but `assert("test").hasClass(Any::class)` fails.
 * @see [doesNotHaveClass]
 * @see [isInstanceOf]
 */
fun <T : Any> Assert<T>.hasClass(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (jclass == actual.javaClass) return
    expected("to have class:${show(jclass)} but was:${show(actual.javaClass)}")
}

/**
 * Asserts the value has the expected java class. This is an exact match, so
 * `assert("test").hasClass(String::class.java)` is successful but `assert("test").hasClass(Any::class.java)` fails.
 * @see [doesNotHaveClass]
 * @see [isInstanceOf]
 */
fun <T : Any> Assert<T>.hasClass(jclass: Class<out T>) {
    if (jclass == actual.javaClass) return
    expected("to have class:${show(jclass)} but was:${show(actual.javaClass)}")
}

/**
 * Asserts the value does not have the expected kotlin class. This is an exact match, so
 * `assert("test").doesNotHaveClass(String::class)` is fails but `assert("test").doesNotHaveClass(Any::class)` is
 * succesful.
 * @see [hasClass]
 * @see [isNotInstanceOf]
 */
fun <T : Any> Assert<T>.doesNotHaveClass(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (jclass != actual.javaClass) return
    expected("to not have class:${show(jclass)}")
}

/**
 * Asserts the value does not have the expected java class. This is an exact match, so
 * `assert("test").doesNotHaveClass(String::class.java)` is fails but `assert("test").doesNotHaveClass(Any::class.java)`
 * is succesful.
 * @see [hasClass]
 * @see [isNotInstanceOf]
 */
fun <T : Any> Assert<T>.doesNotHaveClass(jclass: Class<out T>) {
    if (jclass != actual.javaClass) return
    expected("to not have class:${show(jclass)}")
}

/**
 * Asserts the value is an instance of the expected kotlin class. Both `assert("test").isInstanceOf(String::class)` and
 * `assert("test").isInstanceOf(Any::class)` is successful.
 * @see [isNotInstanceOf]
 * @see [hasClass]
 */
fun <T : Any> Assert<T>.isInstanceOf(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (jclass.isInstance(actual)) return
    expected("to be instance of:${show(jclass)} but had class:${show(actual.javaClass)}")
}

/**
 * Asserts the value is an instance of the expected java class. Both `assert("test").isInstanceOf(String::class.java)`
 * and `assert("test").isInstanceOf(Any::class.java)` is successful.
 * @see [isNotInstanceOf]
 * @see [hasClass]
 */
fun <T : Any> Assert<T>.isInstanceOf(jclass: Class<out T>) {
    if (jclass.isInstance(actual)) return
    expected("to be instance of:${show(jclass)} but had class:${show(actual.javaClass)}")
}

/**
 * Asserts the value is not an instance of the expected kotlin class. Both
 * `assert("test").isNotInstanceOf(String::class)` and `assert("test").isNotInstanceOf(Any::class)` fails.
 * @see [isInstanceOf]
 * @see [doesNotHaveClass]
 */
fun <T : Any> Assert<T>.isNotInstanceOf(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (!jclass.isInstance(actual)) return
    expected("to not be instance of:${show(jclass)}")
}

/**
 * Asserts the value is not an instance of the expected java class. Both `assert("test").isNotInstanceOf(String::class)`
 * and `assert("test").isNotInstanceOf(Any::class)` fails.
 * @see [isInstanceOf]
 * @see [doesNotHaveClass]
 */
fun <T : Any> Assert<T>.isNotInstanceOf(jclass: Class<out T>) {
    if (!jclass.isInstance(actual)) return
    expected("to not be instance of:${show(jclass)}")
}

/**
 * Asserts the value is in the expected values, using `in`.
 * @see [isNotIn]
 */
fun <T> Assert<T>.isIn(vararg values: T) {
    if (actual in values) return
    expected(":${show(values)} to contain:${show(actual)}")
}

/**
 * Asserts the value is not in the expected values, using `!in`.
 * @see [isIn]
 */
fun <T> Assert<T>.isNotIn(vararg values: T) {
    if (actual !in values) return
    expected(":${show(values)} to not contain:${show(actual)}")
}

/**
 * Asserts the value has the expected string from it's [toString].
 */
fun <T> Assert<T>.hasToString(string: String) {
    val result = actual.toString()
    if (result == string) return
    expected("toString() to be:${show(string)} but was:${show(result)}")
}

/**
 * Asserts the value has the expected hash code from it's [hashCode].
 */
fun <T : Any> Assert<T>.hasHashCode(hashCode: Int) {
    val result = actual.hashCode()
    if (result == hashCode) return
    expected("hashCode() to be:${show(hashCode)} but was:${show(result)}")
}

// nullable
/**
 * Asserts the value is null.
 */
fun <T : Any> Assert<T?>.isNull() {
    if (actual == null) return
    expected("null but was:${show(actual)}")
}

/**
 * Asserts the value is not null. You can pass in an optional lambda to run additonal assertions on the non-null value.
 *
 * ```
 * val name: String? = ...
 * assert(name).isNotNull() {
 *   hasLength(4)
 * }
 * ```
 */
fun <T : Any> Assert<T?>.isNotNull(f: (Assert<T>) -> Unit = {}) {
    if (actual != null) {
        assert(actual, name, f)
    } else {
        expected("to not be null")
    }
}
