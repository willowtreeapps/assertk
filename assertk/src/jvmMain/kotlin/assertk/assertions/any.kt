@file:JvmName("AnyJVMKt")

package assertk.assertions

import assertk.Assert
import assertk.all
import assertk.assertions.support.appendName
import assertk.assertions.support.expected
import assertk.assertions.support.show
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.IllegalCallableAccessException
import kotlin.reflect.full.memberProperties

/**
 * Returns an assert on the java class of the value.
 */
fun <T : Any> Assert<T>.jClass() = having("class") { it::class.java }

/**
 * Asserts the value has the expected java class. This is an exact match, so
 * `assertThat("test").hasClass(String::class.java)` is successful but `assertThat("test").hasClass(Any::class.java)` fails.
 * @see [doesNotHaveClass]
 * @see [isInstanceOf]
 */
fun <T : Any> Assert<T>.hasClass(jclass: Class<out T>) = given { actual ->
    if (jclass == actual.javaClass) return
    expected("to have class:${show(jclass)} but was:${show(actual.javaClass)}")
}

/**
 * Asserts the value does not have the expected java class. This is an exact match, so
 * `assertThat("test").doesNotHaveClass(String::class.java)` is fails but `assertThat("test").doesNotHaveClass(Any::class.java)`
 * is successful.
 * @see [hasClass]
 * @see [isNotInstanceOf]
 */
fun <T : Any> Assert<T>.doesNotHaveClass(jclass: Class<out T>) = given { actual ->
    if (jclass != actual.javaClass) return
    expected("to not have class:${show(jclass)}")
}

/**
 * Asserts the value is an instance of the expected java class. Both `assertThat("test").isInstanceOf(String::class.java)`
 * and `assertThat("test").isInstanceOf(Any::class.java)` is successful.
 * @see [isNotInstanceOf]
 * @see [hasClass]
 */
fun <T : Any> Assert<Any?>.isInstanceOf(jclass: Class<T>): Assert<T> = transform { actual ->
    if (jclass.isInstance(actual)) {
        @Suppress("UNCHECKED_CAST")
        actual as T
    } else {
        val but = if (actual == null) "was null" else "had class:${show(actual.javaClass)}"
        expected("to be instance of:${show(jclass)} but $but")
    }
}

/**
 * Asserts the value is not an instance of the expected java class. Both `assertThat("test").isNotInstanceOf(String::class)`
 * and `assertThat("test").isNotInstanceOf(Any::class)` fails.
 * @see [isInstanceOf]
 * @see [doesNotHaveClass]
 */
fun <T : Any> Assert<T?>.isNotInstanceOf(jclass: Class<out T>) = given { actual ->
    if (!jclass.isInstance(actual)) return
    expected("to not be instance of:${show(jclass)}")
}

/**
 * Like [isEqualTo] but reports exactly which properties differ. Only supports data classes. Note: you should
 * _not_ use this if your data class has a custom [Any.equals] since it can be misleading.
 */
fun <T : Any> Assert<T>.isDataClassEqualTo(expected: T) = given { actual ->
    if (!actual::class.isData) {
        throw IllegalArgumentException("only supports data classes")
    }
    all {
        isDataClassEqualToImpl(expected, actual::class)
    }
}

private fun <T> Assert<T>.isDataClassEqualToImpl(expected: T, kclass: KClass<*>?): Unit = given { actual ->
    if (actual == expected) return
    val compareProps = actual != null && expected != null
    if (compareProps && kclass != null && kclass.isData) {
        for (memberProp in kclass.memberProperties) {
            @Suppress("UNCHECKED_CAST")
            val force = memberProp as KProperty1<T, Any?>
            having(force).isDataClassEqualToImpl(force.get(expected), force.returnType.classifier as? KClass<*>)
        }
    } else {
        isEqualTo(expected)
    }
}

/**
 * Returns an assert that compares all accessible properties except the given properties on the calling class.
 * @param other Other value to compare to
 * @param properties properties of the type with which been ignored
 *
 * ```
 * assertThat(person).isEqualToIgnoringGivenProperties(other, Person::name, Person::age)
 * ```
 */
fun <T : Any> Assert<T>.isEqualToIgnoringGivenProperties(other: T, vararg properties: KProperty1<T, Any?>) {
    all {
        for (prop in other::class.memberProperties) {
            if (!properties.contains(prop)) {
                @Suppress("UNCHECKED_CAST")
                val force = prop as KProperty1<T, Any?>
                @Suppress("SwallowedException")
                val otherValue = try {
                    prop.get(other)
                } catch (e: IllegalCallableAccessException) {
                    // ignore in-accessible properties
                    continue
                }
                transform(appendName(prop.name, separator = "."), force::get)
                    .isEqualTo(otherValue)
            }
        }
    }
}
