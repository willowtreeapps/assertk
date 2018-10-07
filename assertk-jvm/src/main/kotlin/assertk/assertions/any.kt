@file:JvmName("AnyJVMKt")

package assertk.assertions

import assertk.Assert
import assertk.all
import assertk.assertions.support.expected
import assertk.assertions.support.show
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * Returns an assert on the java class of the value.
 */
fun <T : Any> Assert<T>.jClass() = prop("class", { it::class.java })


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
 * Asserts the value does not have the expected java class. This is an exact match, so
 * `assert("test").doesNotHaveClass(String::class.java)` is fails but `assert("test").doesNotHaveClass(Any::class.java)`
 * is successful.
 * @see [hasClass]
 * @see [isNotInstanceOf]
 */
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
fun <T : Any, S : T> Assert<T>.isInstanceOf(jclass: Class<S>): Assert<S> {
    if (jclass.isInstance(actual)) {
        @Suppress("UNCHECKED_CAST")
        return assert(actual as S, name = name)
    } else {
        expected("to be instance of:${show(jclass)} but had class:${show(actual.javaClass)}")
    }
}

/**
 * Asserts the value is an instance of the expected java class. Both `assert("test").isInstanceOf(String::class.java)`
 * and `assert("test").isInstanceOf(Any::class.java)` is successful.
 * @see [isNotInstanceOf]
 * @see [hasClass]
 */
@Deprecated(message = "Use isInstanceOf(jclass) instead.", replaceWith = ReplaceWith("isInstanceOf(jclass).let(f)"))
fun <T : Any, S : T> Assert<T>.isInstanceOf(jclass: Class<S>, f: (Assert<S>) -> Unit) {
    if (jclass.isInstance(actual)) {
        @Suppress("UNCHECKED_CAST")
        assert(actual as S, name = name).all(listOf(f))
    } else {
        expected("to be instance of:${show(jclass)} but had class:${show(actual.javaClass)}")
    }
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
 * Like [Assert.isEqualTo] but reports exactly which properties differ. Only supports data classes. Note: you should
 * _not_ use this if your data class has a custom [Any.equals] since it can be misleading.
 */
fun <T : Any> Assert<T>.isDataClassEqualTo(expected: T) {
    if (!actual::class.isData) {
        throw IllegalArgumentException("only supports data classes")
    }
    isDataClassEqualToImpl(expected, actual::class)
}

private fun <T> Assert<T>.isDataClassEqualToImpl(expected: T, kclass: KClass<*>?) {
    if (actual == expected) return
    if (kclass != null && kclass.isData) {
        all(kclass.memberProperties.map { prop ->
            fun Assert<T>.() =
                prop(prop).isDataClassEqualToImpl(prop.call(expected), prop.returnType.classifier as? KClass<*>)
        })
    } else {
        isEqualTo(expected)
    }
}
