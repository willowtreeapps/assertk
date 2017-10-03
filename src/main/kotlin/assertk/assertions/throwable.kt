package assertk.assertions

import assertk.Assert
import assertk.assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import kotlin.reflect.KClass

/**
 * Asserts the throwable has the expected message.
 */
fun <T : Throwable> Assert<T>.hasMessage(message: String?) {
    assert(actual.message, "message").isNotNull {
        it.isEqualTo(message)
    }
}

/**
 * Asserts the throwable has the expected cause.
 * @see [hasNoCause]
 */
fun <T : Throwable> Assert<T>.hasCause(cause: Throwable) {
    assert(actual.cause, "cause").isNotNull {
        it.isEqualTo(cause)
    }
}

/**
 * Asserts the throwable has no cause.
 * @see [hasCause]
 */
fun <T : Throwable> Assert<T>.hasNoCause() {
    if (actual.cause == null) return
    expected("[cause] to not exist but was:${show(actual.cause)}")
}

/**
 * Asserts the throwable has a message starting with the expected string.
 */
fun <T : Throwable> Assert<T>.hasMessageStartingWith(prefix: String) {
    assert(actual.message, "message").isNotNull {
        it.startsWith(prefix)
    }
}

/**
 * Asserts the throwable has a message containing the expected string.
 */
fun <T : Throwable> Assert<T>.hasMessageContaining(string: String) {
    assert(actual.message, "message").isNotNull {
        it.contains(string)
    }
}

/**
 * Asserts the throwable has a messaging matching the expected regular expression.
 */
fun <T : Throwable> Assert<T>.hasMessageMatching(regex: Regex) {
    assert(actual.message, "message").isNotNull {
        it.matches(regex)
    }
}

/**
* Asserts the throwable has a message ending with the expected string.
*/
fun <T : Throwable> Assert<T>.hasMessageEndingWith(suffix: String) {
    assert(actual.message, "message").isNotNull {
        it.endsWith(suffix)
    }
}

/**
 * Asserts the throwable's cause is an instance of the expected kotlin class.
 * @see [hasCauseWithClass]
 * @see [hasRootCauseInstanceOf]
 */
fun <T : Throwable> Assert<T>.hasCauseInstanceOf(kclass: KClass<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.isInstanceOf(kclass)
    }
}

/**
 * Asserts the throwable's cause is an instance of the expected java class.
 * @see [hasCauseWithClass]
 * @see [hasRootCauseInstanceOf]
 */
fun <T : Throwable> Assert<T>.hasCauseInstanceOf(jclass: Class<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.isInstanceOf(jclass)
    }
}

/**
 * Asserts the throwable's cause matches the expected kotlin class.
 * @see [hasCauseInstanceOf]
 * @see [hasRootCauseWithClass]
 */
fun <T : Throwable> Assert<T>.hasCauseWithClass(kclass: KClass<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.hasClass(kclass)
    }
}

/**
 * Asserts the throwable's cause matches the expected java class.
 * @see [hasCauseInstanceOf]
 * @see [hasRootCauseWithClass]
 */
fun <T : Throwable> Assert<T>.hasCauseWithClass(jclass: Class<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.hasClass(jclass)
    }
}

/**
 * Asserts the throwable has the expected root cause.
 */
fun <T : Throwable> Assert<T>.hasRootCause(cause: Throwable) {
    assert(actual.rootCause(), "root cause").isEqualTo(cause)

}

/**
 * Asserts the throwable's root cause is an instance of the expected kotlin class.
 * @see [hasRootCauseWithClass]
 * @see [hasCauseInstanceOf]
 */
fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(kclass: KClass<out T>) {
    assert(actual.rootCause(), "root cause").isInstanceOf(kclass)
}

/**
 * Asserts the throwable's root cause is an instance of the expected java class.
 * @see [hasRootCauseWithClass]
 * @see [hasCauseInstanceOf]
 */
fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(jclass: Class<out T>) {
    assert(actual.rootCause(), "root cause").isInstanceOf(jclass)
}

/**
 * Asserts the throwable's root cause matches the expected kotlin class.
 * @see [hasRootCauseInstanceOf]
 * @see [hasCauseWithClass]
 */
fun <T : Throwable> Assert<T>.hasRootCauseWithClass(kclass: KClass<out T>) {
    assert(actual.rootCause(), "root cause").hasClass(kclass)
}

/**
 * Asserts the throwable's root cause matches the expected java class.
 * @see [hasRootCauseInstanceOf]
 * @see [hasCauseWithClass]
 */
fun <T : Throwable> Assert<T>.hasRootCauseWithClass(jclass: Class<out T>) {
    assert(actual.rootCause(), "root cause").hasClass(jclass)
}

private fun Throwable.rootCause(): Throwable =
        this.cause?.rootCause() ?: this

/**
 * Asserts the throwable's stacktrace contains the expected string.
 */
fun <T : Throwable> Assert<T>.hasStackTraceContaining(description: String) {
    assert(actual.stackTrace.map { it.toString() }, "stack trace").contains(description)
}

