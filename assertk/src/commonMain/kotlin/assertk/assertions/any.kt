package assertk.assertions

import assertk.Assert
import assertk.all
import assertk.assertions.support.appendName
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * Returns an assert on the kotlin class of the value.
 */
fun Assert<Any>.kClass() = having("class") { it::class }

/**
 * Returns an assert on the toString method of the value.
 */
fun Assert<Any?>.toStringFun() = having("toString", Any?::toString)

/**
 * Returns an assert on the hasCode method of the value.
 */
fun Assert<Any>.hashCodeFun() = having("hashCode", Any::hashCode)

/**
 * Asserts the value is equal to the expected one, using `==`.
 * @see [isNotEqualTo]
 * @see [isSameInstanceAs]
 */
fun <T> Assert<T>.isEqualTo(expected: T) = given { actual ->
    if (actual == expected) return
    fail(expected, actual)
}

/**
 * Asserts the value is not equal to the expected one, using `!=`.
 * @see [isEqualTo]
 * @see [isNotSameInstanceAs]
 */
fun Assert<Any?>.isNotEqualTo(expected: Any?) = given { actual ->
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
 * @see [isNotSameInstanceAs]
 * @see [isEqualTo]
 */
@Deprecated("renamed to isSameInstanceAs", replaceWith = ReplaceWith("isSameInstanceAs(expected)"))
fun <T> Assert<T>.isSameAs(expected: T) = isSameInstanceAs(expected)

/**
 * Asserts the value is the same as the expected one, using `===`.
 * @see [isNotSameInstanceAs]
 * @see [isEqualTo]
 */
fun <T> Assert<T>.isSameInstanceAs(expected: T) = given { actual ->
    if (actual === expected) return
    expected(":${show(expected)} and:${show(actual)} to refer to the same object")
}

/**
 * Asserts the value is not the same as the expected one, using `!==`.
 * @see [isSameInstanceAs]
 * @see [isNotEqualTo]
 */
@Deprecated("renamed to isNotSameInstanceAs", replaceWith = ReplaceWith("isNotSameInstanceAs(expected)"))
fun Assert<Any?>.isNotSameAs(expected: Any?) = isNotSameInstanceAs(expected)

/**
 * Asserts the value is not the same as the expected one, using `!==`.
 * @see [isSameInstanceAs]
 * @see [isNotEqualTo]
 */
fun Assert<Any?>.isNotSameInstanceAs(expected: Any?) = given { actual ->
    if (actual !== expected) return
    expected(":${show(expected)} to not refer to the same object")
}

/**
 * Asserts the value is in the expected values, using `in`.
 * @see [isNotIn]
 */
fun <T> Assert<T>.isIn(vararg values: T) = given { actual ->
    if (actual in values) return
    expected(":${show(values)} to contain:${show(actual)}")
}

/**
 * Asserts the value is not in the expected values, using `!in`.
 * @see [isIn]
 */
fun <T> Assert<T>.isNotIn(vararg values: T) = given { actual ->
    if (actual !in values) return
    expected(":${show(values)} to not contain:${show(actual)}")
}

/**
 * Asserts the value has the expected string from it's [toString].
 */
fun Assert<Any?>.hasToString(string: String) {
    toStringFun().isEqualTo(string)
}

/**
 * Asserts the value has the expected hash code from it's [hashCode].
 */
fun Assert<Any>.hasHashCode(hashCode: Int) {
    hashCodeFun().isEqualTo(hashCode)
}

/**
 * Asserts the value is null.
 */
fun Assert<Any?>.isNull() = given { actual ->
    if (actual == null) return
    expected("to be null but was:${show(actual)}")
}

/**
 * Asserts the value is not null. You can pass in an optional lambda to run additional assertions on the non-null value.
 *
 * ```
 * val name: String? = ...
 * assertThat(name).isNotNull().hasLength(4)
 * ```
 */
fun <T : Any> Assert<T?>.isNotNull(): Assert<T> = transform { actual ->
    actual ?: expected("to not be null")
}

/**
 * Returns an assert that asserts on the given property of the value.
 * @param name The name of the property to show in failure messages.
 * @param extract The function to extract the property value out of the value of the current assert.
 *
 * ```
 * assertThat(person).having("name", { it.name }).isEqualTo("Sue")
 * ```
 */
fun <T, P> Assert<T>.having(name: String, extract: (T) -> P): Assert<P> =
    transform(appendName(name, separator = "."), extract)

@Deprecated(
    message = "Function prop has been renamed to having",
    replaceWith = ReplaceWith("having(name, extract)"),
    level = DeprecationLevel.WARNING
)
fun <T, P> Assert<T>.prop(name: String, extract: (T) -> P): Assert<P> =
    transform(appendName(name, separator = "."), extract)


/**
 * Returns an assert that asserts on the given property.
 *
 * Example:
 * ```
 * assertThat(person).having(Person::name).isEqualTo("Sue")
 * ```
 *
 * @param property Property on which to assert. The name of this
 * property will be shown in failure messages.
 */
fun <T, P> Assert<T>.having(property: KProperty1<T, P>): Assert<P> =
    having(property.name) { property.get(it) }

@Deprecated(
    message = "Function prop has been renamed to having",
    replaceWith = ReplaceWith("having(property)"),
    level = DeprecationLevel.WARNING
)
fun <T, P> Assert<T>.prop(property: KProperty1<T, P>): Assert<P> =
    having(property.name) { property.get(it) }

/**
 * Returns an assert that asserts on the result of calling the given function.
 *
 * Example:
 * ```
 * assertThat(person).having(Person::nameAsLowerCase).isEqualTo("sue")
 * ```
 *
 * @param callable Callable on which to assert. The name of this
 * callable will be shown in the failure messages.
 */
fun <T, R, F> Assert<T>.having(callable: F): Assert<R> where F : (T) -> R, F : KCallable<R> =
    having(callable.name, callable)

@Deprecated(
    message = "Function prop has been renamed to having",
    replaceWith = ReplaceWith("having(callable)"),
    level = DeprecationLevel.WARNING
)
fun <T, R, F> Assert<T>.prop(callable: F): Assert<R> where F : (T) -> R, F : KCallable<R> =
    having(callable.name, callable)

/**
 * Asserts the value has the expected kotlin class. This is an exact match, so `assertThat("test").hasClass<String>()`
 * is successful but `assertThat("test").hasClass<Any>()` fails.
 * @see [doesNotHaveClass]
 * @see [isInstanceOf]
 */
inline fun <reified T : Any> Assert<Any>.hasClass() = hasClass(T::class)

/**
 * Asserts the value has the expected kotlin class. This is an exact match, so `assertThat("test").hasClass(String::class)`
 * is successful but `assertThat("test").hasClass(Any::class)` fails.
 * @see [doesNotHaveClass]
 * @see [isInstanceOf]
 */
fun <T : Any> Assert<T>.hasClass(kclass: KClass<out T>) = given { actual ->
    if (kclass == actual::class) return
    expected("to have class:${show(kclass)} but was:${show(actual::class)}")
}

/**
 * Asserts the value does not have the expected kotlin class. This is an exact match, so
 * `assertThat("test").doesNotHaveClass<String>()` fails but `assertThat("test").doesNotHaveClass<Any>()` is
 * successful.
 * @see [hasClass]
 * @see [isNotInstanceOf]
 */
inline fun <reified T : Any> Assert<Any>.doesNotHaveClass() = doesNotHaveClass(T::class)

/**
 * Asserts the value does not have the expected kotlin class. This is an exact match, so
 * `assertThat("test").doesNotHaveClass(String::class)` fails but `assertThat("test").doesNotHaveClass(Any::class)` is
 * successful.
 * @see [hasClass]
 * @see [isNotInstanceOf]
 */
fun <T : Any> Assert<T>.doesNotHaveClass(kclass: KClass<out T>) = given { actual ->
    if (kclass != actual::class) return
    expected("to not have class:${show(kclass)}")
}

/**
 * Asserts the value is not an instance of the expected kotlin class. Both
 * `assertThat("test").isNotInstanceOf<String>()` and `assertThat("test").isNotInstanceOf<String>()` fail.
 * @see [isInstanceOf]
 * @see [doesNotHaveClass]
 */
inline fun <reified T : Any> Assert<Any>.isNotInstanceOf() = isNotInstanceOf(T::class)

/**
 * Asserts the value is not an instance of the expected kotlin class. Both
 * `assertThat("test").isNotInstanceOf(String::class)` and `assertThat("test").isNotInstanceOf(Any::class)` fail.
 * @see [isInstanceOf]
 * @see [doesNotHaveClass]
 */
fun <T : Any> Assert<T>.isNotInstanceOf(kclass: KClass<out T>) = given { actual ->
    if (!kclass.isInstance(actual)) return
    expected("to not be instance of:${show(kclass)}")
}

/**
 * Asserts the value is an instance of the expected kotlin class. Both `assertThat("test").isInstanceOf<String>()` and
 * `assertThat("test").isInstanceOf<Any>()` are successful.
 * @see [isNotInstanceOf]
 * @see [hasClass]
 */
inline fun <reified T : Any> Assert<Any>.isInstanceOf() = isInstanceOf(T::class)

/**
 * Asserts the value is an instance of the expected kotlin class. Both `assertThat("test").isInstanceOf(String::class)` and
 * `assertThat("test").isInstanceOf(Any::class)` are successful.
 * @see [isNotInstanceOf]
 * @see [hasClass]
 */
fun <T : Any> Assert<Any>.isInstanceOf(kclass: KClass<T>): Assert<T> = transform(name) { actual ->
    if (kclass.isInstance(actual)) {
        @Suppress("UNCHECKED_CAST")
        actual as T
    } else {
        expected("to be instance of:${show(kclass)} but had class:${show(actual::class)}")
    }
}

/**
 * Asserts the value corresponds to the expected one using the given correspondence function to compare them. This is
 * useful when the objects don't have an [equals] implementation.
 *
 * @see [isEqualTo]
 * @see [doesNotCorrespond]
 */
fun <T, E> Assert<T>.corresponds(expected: E, correspondence: (T, E) -> Boolean) = given { actual ->
    if (correspondence(actual, expected)) return
    fail(expected, actual)
}

/**
 * Asserts the value does not correspond to the expected one using the given correspondence function to compare them.
 * This is useful when the objects don't have an [equals] implementation.
 *
 * @see [corresponds]
 * @see [isNotEqualTo]
 */
fun <T, E> Assert<T>.doesNotCorrespond(expected: E, correspondence: (T, E) -> Boolean) = given { actual ->
    if (!correspondence(actual, expected)) return
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
 * Returns an assert that compares only the given properties on the calling class
 * @param other Other value to compare to
 * @param properties properties of the type with which to compare
 *
 * ```
 * assertThat(person).isEqualToWithGivenProperties(other, Person::name, Person::age)
 * ```
 */
fun <T> Assert<T>.isEqualToWithGivenProperties(other: T, vararg properties: KProperty1<T, Any?>) {
    all {
        for (prop in properties) {
            transform(appendName(prop.name, separator = "."), prop::get)
                .isEqualTo(prop.get(other))
        }
    }
}
