@file:JvmName("AnyJVMKt")

package assertk.assertions

import assertk.Assert
import assertk.all
import assertk.assertions.support.expected
import assertk.assertions.support.appendName
import assertk.assertions.support.show
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Returns an assert on the java class of the value.
 */
fun <T : Any> Assert<T>.jClass() = prop("class") { it::class.java }

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
fun <T : Any, S : T> Assert<T>.isInstanceOf(jclass: Class<S>): Assert<S> = transform { actual ->
    if (jclass.isInstance(actual)) {
        @Suppress("UNCHECKED_CAST")
        actual as S
    } else {
        expected("to be instance of:${show(jclass)} but had class:${show(actual.javaClass)}")
    }
}

/**
 * Asserts the value is not an instance of the expected java class. Both `assertThat("test").isNotInstanceOf(String::class)`
 * and `assertThat("test").isNotInstanceOf(Any::class)` fails.
 * @see [isInstanceOf]
 * @see [doesNotHaveClass]
 */
fun <T : Any> Assert<T>.isNotInstanceOf(jclass: Class<out T>) = given { actual ->
    if (!jclass.isInstance(actual)) return
    expected("to not be instance of:${show(jclass)}")
}

/**
 * Returns an assert that asserts on the given property.
 * @param callable The function to get the property value out of the value of the current assert. The name of this
 * callable will be shown in failure messages.
 *
 * ```
 * assertThat(person).prop(Person::name).isEqualTo("Sue")
 * ```
 *
 * @see prop
 */
@Deprecated(
    "Use an overload with explicit name and extract",
    ReplaceWith("this.prop(\"NAME\") { callable.call(it) }", "assertk.assertions.prop")
)
@Suppress("SwallowedException")
fun <T, P> Assert<T>.prop(callable: KCallable<P>) = prop(callable.name) {
    try {
        callable.call(it)
    } catch (e: InvocationTargetException) {
        // unwrap cause for a more helpful error message.
        throw e.cause!!
    }
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
            prop(force).isDataClassEqualToImpl(force.get(expected), force.returnType.classifier as? KClass<*>)
        }
    } else {
        isEqualTo(expected)
    }
}

/**
 * Returns an assert that compares for all properties except the given properties on the calling class
 * @param other Other value to compare to
 * @param properties properties of the type with which been ignored
 *
 * ```
 * assertThat(person).isEqualToIgnoringGivenProperties(other, Person::name, Person::age)
 * ```
 */
fun <T : Any> Assert<T>.isEqualToIgnoringGivenProperties(other: T, vararg properties: KProperty1<T, Any?>) {
    all {
        for (prop in other::class.members) {
            if (prop is KProperty1<*, *> && !properties.contains(prop)) {
                @Suppress("UNCHECKED_CAST")
                val force = prop as KProperty1<T, Any?>
                transform(appendName(prop.name, separator = "."), force::get)
                    .isEqualTo(prop.get(other))
            }
        }
    }
}