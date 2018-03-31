package assertk.assertions

import assertk.Assert
import assertk.all
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.staticProperties

/**
 * Returns an assert on the kotlin class of the value.
 */
fun <T : Any> Assert<T>.kClass() = prop("class", { it::class })

/**
 * Returns an assert on the java class of the value.
 */
fun <T : Any> Assert<T>.jClass() = prop("class", { it::class.java })

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
 * Asserts the value has the expected kotlin class. This is an exact match, so `assert("test").hasClass(String::class)`
 * is succesful but `assert("test").hasClass(Any::class)` fails.
 * @see [doesNotHaveClass]
 * @see [isInstanceOf]
 */
@Deprecated(message = "Use kClass().isEqualTo(kclass) instead.",
        replaceWith = ReplaceWith("kClass().isEqualTo(kclass)"))
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
@Deprecated(message = "Use jClass().isEqualTo(jclass) instead.",
        replaceWith = ReplaceWith("jClass().isEqualTo(jclass)"))
fun <T : Any> Assert<T>.hasClass(jclass: Class<out T>) {
    if (jclass == actual.javaClass) return
    expected("to have class:${show(jclass)} but was:${show(actual.javaClass)}")
}

/**
 * Asserts the value does not have the expected kotlin class. This is an exact match, so
 * `assert("test").doesNotHaveClass(String::class)` is fails but `assert("test").doesNotHaveClass(Any::class)` is
 * successful.
 * @see [hasClass]
 * @see [isNotInstanceOf]
 */
@Deprecated(message = "Use kClass().isNotEqualTo(kclass) instead.",
        replaceWith = ReplaceWith("kClass().isNotEqualTo(kclass)"))
fun <T : Any> Assert<T>.doesNotHaveClass(kclass: KClass<out T>) {
    val jclass: Class<out T> = kclass.java
    if (jclass != actual.javaClass) return
    expected("to not have class:${show(jclass)}")
}

/**
 * Asserts the value does not have the expected java class. This is an exact match, so
 * `assert("test").doesNotHaveClass(String::class.java)` is fails but `assert("test").doesNotHaveClass(Any::class.java)`
 * is successful.
 * @see [hasClass]
 * @see [isNotInstanceOf]
 */
@Deprecated(message = "Use jClass().isNotEqualTo(jclass) instead.",
        replaceWith = ReplaceWith("jClass().isNotEqualTo(jclass)"))
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
@Deprecated(message = "Use toStringFun().isEqualTo(string) instead.",
        replaceWith = ReplaceWith("toStringFun().isEqualTo(string)"))
fun <T> Assert<T>.hasToString(string: String) {
    val result = actual.toString()
    if (result == string) return
    expected("toString() to be:${show(string)} but was:${show(result)}")
}

/**
 * Asserts the value has the expected hash code from it's [hashCode].
 */
@Deprecated(message = "Use hashCodeFun().isEqualTo(hashCode) instead.",
        replaceWith = ReplaceWith("hashCodeFun().isEqualTo(hashCode)"))
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
 * Returns an assert that asserts on the given property.
 * @param callable The function to get the property value out of the value of the current assert. The same of this
 * callable will be shown in failure messages.
 *
 * ```
 * assert(person).prop(Person::name).isEqualTo("Sue")
 * ```
 */
fun <T, P> Assert<T>.prop(callable: KCallable<P>) = prop(callable.name) { callable.call(it) }

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
