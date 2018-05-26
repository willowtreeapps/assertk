package assertk.assertions

import assertk.Assert
import assertk.all
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * Returns an assert on the kotlin class of the value.
 */
fun <T : Any> Assert<T>.kClass() = prop("class", { it::class })

/**
 * Returns an assert on the toString method of the value.
 */
fun <T> Assert<T>.toStringFun() = prop("toString", Any?::toString)

/**
 * Returns an assert on the hasCode method of the value.
 */
fun <T : Any> Assert<T>.hashCodeFun() = prop("hashCode", Any::hashCode)


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
    val showExpected = show(expected)
    val showActual = show(actual)
    // if they display the same, only show one.
    if (showExpected == showActual) {
        expected("to not be equal to:$showActual")
    } else {
        expected(":$showExpected not to be equal to:$showActual")
    }
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
    toStringFun().isEqualTo(string)
}

/**
 * Asserts the value has the expected hash code from it's [hashCode].
 */
fun <T : Any> Assert<T>.hasHashCode(hashCode: Int) {
    hashCodeFun().isEqualTo(hashCode)
}

// nullable
/**
 * Asserts the value is null.
 */
fun <T : Any> Assert<T?>.isNull() {
    if (actual == null) return
    expected("to be null but was:${show(actual)}")
}

/**
 * Asserts the value is not null. You can pass in an optional lambda to run additional assertions on the non-null value.
 *
 * ```
 * val name: String? = ...
 * assert(name).isNotNull() {
 *   it.hasLength(4)
 * }
 * ```
 */
fun <T : Any> Assert<T?>.isNotNull(f: (Assert<T>) -> Unit = {}) {
    if (actual != null) {
        assert(actual, name = name).all(f)
    } else {
        expected("to not be null")
    }
}

/**
 * Returns an assert that asserts on the given property of the value.
 * @param name The name of the property to show in failure messages.
 * @param extract The function to extract the property value out of the value of the current assert.
 *
 * ```
 * assert(person).prop("name", { it.name }).isEqualTo("Sue")
 * ```
 */
fun <T, P> Assert<T>.prop(name: String, extract: (T) -> P)
        = assert(extract(actual), "${if (this.name != null) this.name + "." else ""}$name")


/**
 * Asserts the value has the expected kotlin class. This is an exact match, so `assert("test").hasClass(String::class)`
 * is successful but `assert("test").hasClass(Any::class)` fails.
 * @see [doesNotHaveClass]
 * @see [isInstanceOf]
 */
fun <T : Any> Assert<T>.hasClass(kclass: KClass<out T>) {
    if (kclass == actual::class) return
    expected("to have class:${show(kclass)} but was:${show(actual::class)}")
}

/**
 * Asserts the value does not have the expected kotlin class. This is an exact match, so
 * `assert("test").doesNotHaveClass(String::class)` is fails but `assert("test").doesNotHaveClass(Any::class)` is
 * successful.
 * @see [hasClass]
 * @see [isNotInstanceOf]
 */
fun <T : Any> Assert<T>.doesNotHaveClass(kclass: KClass<out T>) {
    if (kclass != actual::class) return
    expected("to not have class:${show(kclass)}")
}

/**
 * Asserts the value is not an instance of the expected kotlin class. Both
 * `assert("test").isNotInstanceOf(String::class)` and `assert("test").isNotInstanceOf(Any::class)` fails.
 * @see [isInstanceOf]
 * @see [doesNotHaveClass]
 */
fun <T : Any> Assert<T>.isNotInstanceOf(kclass: KClass<out T>) {
    if (!kclass.isInstance(actual)) return
    expected("to not be instance of:${show(kclass)}")
}


/**
 * Asserts the value is an instance of the expected kotlin class. Both `assert("test").isInstanceOf(String::class)` and
 * `assert("test").isInstanceOf(Any::class)` is successful.
 * @see [isNotInstanceOf]
 * @see [hasClass]
 */
fun <T : Any, S: T> Assert<T>.isInstanceOf(kclass: KClass<S>, f: (Assert<S>) -> Unit = {}) {
    if (kclass.isInstance(actual)) {
        @Suppress("UNCHECKED_CAST")
        assert(actual as S, name = name).all(f)
    } else {
        expected("to be instance of:${show(kclass)} but had class:${show(actual::class)}")
    }
}


/**
 * Returns an assert that compares only the given properties on the calling class
 * @param other Other value to compare to
 * @param properties properties of the type with which to compare
 *
 * ```
 * assert(person).isEqualToWithGivenProperties(other, Person::name, Person::age)
 * ```
 */
fun <T> Assert<T>.isEqualToWithGivenProperties(other: T, vararg properties: KProperty1<T, Any>) {
    properties.forEach {
        assert(it.get(actual), "${if (this.name != null) this.name + "." else ""}${it.name}")
            .isEqualTo(it.get(other))
    }
}
