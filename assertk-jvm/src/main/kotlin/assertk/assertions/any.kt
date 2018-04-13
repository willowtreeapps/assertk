@file:JvmName("AnyJVMKt")
package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * Returns an assert on the java class of the value.
 */
fun <T : Any> Assert<T>.jClass() = prop("class", { it::class.java })


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
