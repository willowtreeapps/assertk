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
fun <T : Any> Assert<T>.kClass() = prop("class") { it::class }

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
fun <T> Assert<T>.isEqualTo(expected: Any?) = given { actual ->
    if (actual == expected) return
    fail(expected, actual)
}

/**
 * Asserts the value is not equal to the expected one, using `!=`.
 * @see [isEqualTo]
 * @see [isNotSameAs]
 */
fun <T> Assert<T>.isNotEqualTo(expected: Any?) = given { actual ->
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
fun <T> Assert<T>.isSameAs(expected: T) = given { actual ->
    if (actual === expected) return
    expected(":${show(expected)} and:${show(actual)} to refer to the same object")
}

/**
 * Asserts the value is not the same as the expected one, using `!==`.
 * @see [isSameAs]
 * @see [isNotEqualTo]
 */
fun <T> Assert<T>.isNotSameAs(expected: Any?) = given { actual ->
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
fun <T> Assert<T>.hasToString(string: String) {
    toStringFun().isEqualTo(string)
}

/**
 * Asserts the value has the expected hash code from it's [hashCode].
 */
fun Assert<Any>.hasHashCode(hashCode: Int) {
    hashCodeFun().isEqualTo(hashCode)
}

// nullable
/**
 * Asserts the value is null.
 */
fun <T : Any> Assert<T?>.isNull() = given { actual ->
    if (actual == null) return
    expected("to be null but was:${show(actual)}")
}

/**
 * Asserts the value is not null. You can pass in an optional lambda to run additional assertions on the non-null value.
 *
 * ```
 * val name: String? = ...
 * assertThat(name).isNotNull() {
 *   it.hasLength(4)
 * }
 * ```
 */
@Deprecated(
    message = "Use isNotNull() instead",
    replaceWith = ReplaceWith("isNotNull().let(f)"),
    level = DeprecationLevel.ERROR
)
fun <T : Any> Assert<T?>.isNotNull(f: (Assert<T>) -> Unit) {
    isNotNull().let(f)
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
 * assertThat(person).prop("name", { it.name }).isEqualTo("Sue")
 * ```
 */
fun <T, P> Assert<T>.prop(name: String, extract: (T) -> P): Assert<P> =
    transform("${if (this.name != null) this.name + "." else ""}$name", extract)

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
 * `assertThat("test").doesNotHaveClass(String::class)` is fails but `assertThat("test").doesNotHaveClass(Any::class)` is
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
 * `assertThat("test").isNotInstanceOf(String::class)` and `assertThat("test").isNotInstanceOf(Any::class)` fails.
 * @see [isInstanceOf]
 * @see [doesNotHaveClass]
 */
fun <T : Any> Assert<T>.isNotInstanceOf(kclass: KClass<out T>) = given { actual ->
    if (!kclass.isInstance(actual)) return
    expected("to not be instance of:${show(kclass)}")
}

/**
 * Asserts the value is an instance of the expected kotlin class. Both `assertThat("test").isInstanceOf(String::class)` and
 * `assertThat("test").isInstanceOf(Any::class)` is successful.
 * @see [isNotInstanceOf]
 * @see [hasClass]
 */
@Deprecated(
    message = "Use isInstanceOf(kclass) instead.",
    replaceWith = ReplaceWith("isInstanceOf(kclass).let(f)"),
    level = DeprecationLevel.ERROR
)
fun <T : Any, S : T> Assert<T>.isInstanceOf(kclass: KClass<S>, f: (Assert<S>) -> Unit) {
    isInstanceOf(kclass).let(f)
}

/**
 * Asserts the value is an instance of the expected kotlin class. Both `assertThat("test").isInstanceOf(String::class)` and
 * `assertThat("test").isInstanceOf(Any::class)` is successful.
 * @see [isNotInstanceOf]
 * @see [hasClass]
 */
fun <T : Any, S : T> Assert<T>.isInstanceOf(kclass: KClass<S>) = transform(name) { actual ->
    if (kclass.isInstance(actual)) {
        @Suppress("UNCHECKED_CAST")
        actual as S
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
fun <T> Assert<T>.isEqualToWithGivenProperties(other: T, vararg properties: KProperty1<T, Any>) {
    all {
        for (prop in properties) {
            transform("${if (this.name != null) this.name + "." else ""}${prop.name}", prop::get)
                .isEqualTo(prop.get(other))
        }
    }
}
