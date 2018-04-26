package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import kotlin.reflect.KClass

/**
 * Returns an assert on the throwable's message.
 */
fun <T : Throwable> Assert<T>.message() = prop("message", Throwable::message)

/**
 * Returns an assert on the throwable's cause.
 */
fun <T : Throwable> Assert<T>.cause() = prop("cause", Throwable::cause)

/**
 * Returns an assert on the throwable's root cause.
 */
fun <T : Throwable> Assert<T>.rootCause() = prop("rootCause", Throwable::rootCause)

/**
 * Asserts the throwable has the expected message.
 */
fun <T : Throwable> Assert<T>.hasMessage(message: String?) {
    message().isEqualTo(message)
}

/**
 * Asserts the throwable has the expected cause.
 * @see [hasNoCause]
 */
fun <T : Throwable> Assert<T>.hasCause(cause: Throwable) {
    cause().isEqualTo(cause)
}

/**
 * Asserts the throwable has no cause.
 * @see [hasCause]
 */
fun <T : Throwable> Assert<T>.hasNoCause() {
    cause().isNull()
}

/**
 * Asserts the throwable has the expected root cause.
 */
fun <T : Throwable> Assert<T>.hasRootCause(cause: Throwable) {
    rootCause().isEqualTo(cause)
}

/**
 * Asserts the throwable has a message starting with the expected string.
 */
@Deprecated(message = "Use message().isNotNull { it.startsWith(prefix) } instead.",
        replaceWith = ReplaceWith("message().isNotNull { it.startsWith(prefix) }"))
fun <T : Throwable> Assert<T>.hasMessageStartingWith(prefix: String) {
    assert(actual.message, "message").isNotNull {
        it.startsWith(prefix)
    }
}

/**
 * Asserts the throwable has a message containing the expected string.
 */
@Deprecated(message = "Use message().isNotNull { it.contains(string) } instead.",
        replaceWith = ReplaceWith("message().isNotNull { it.contains(string) }"))
fun <T : Throwable> Assert<T>.hasMessageContaining(string: String) {
    assert(actual.message, "message").isNotNull {
        it.contains(string)
    }
}

/**
 * Asserts the throwable has a messaging matching the expected regular expression.
 */
@Deprecated(message = "Use message().isNotNull { it.matches(regex) } instead.",
        replaceWith = ReplaceWith("message().isNotNull { it.matches(regex) }"))
fun <T : Throwable> Assert<T>.hasMessageMatching(regex: Regex) {
    assert(actual.message, "message").isNotNull {
        it.matches(regex)
    }
}

/**
 * Asserts the throwable has a message ending with the expected string.
 */
@Deprecated(message = "Use message().isNotNull { it.endsWith(suffix) } instead.",
        replaceWith = ReplaceWith("message().isNotNull { it.endsWith(suffix) }"))
fun <T : Throwable> Assert<T>.hasMessageEndingWith(suffix: String) {
    assert(actual.message, "message").isNotNull {
        it.endsWith(suffix)
    }
}

/**
 * Asserts the throwable's cause matches the expected kotlin class.
 * @see [hasCauseInstanceOf]
 * @see [hasRootCauseWithClass]
 */
@Deprecated(message = "Use cause().isNotNull { it.kClass().isEqualTo(kclass) } instead.",
        replaceWith = ReplaceWith("cause().isNotNull { it.kClass().isEqualTo(kclass) }"))
fun <T : Throwable> Assert<T>.hasCauseWithClass(kclass: KClass<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.kClass().isEqualTo(kclass)
    }
}

/**
 * Asserts the throwable's root cause matches the expected kotlin class.
 * @see [hasRootCauseInstanceOf]
 * @see [hasCauseWithClass]
 */
@Deprecated(message = "Use rootCause().isNotNull { it.kClass().isEqualTo(kclass) } instead.",
        replaceWith = ReplaceWith("rootCause().isNotNull { it.kClass().isEqualTo(kclass) }"))
fun <T : Throwable> Assert<T>.hasRootCauseWithClass(kclass: KClass<out T>) {
    assert(actual.rootCause(), "root cause").isNotNull {
        it.kClass().isEqualTo(kclass)
    }
}

/**
 * Asserts the throwable's cause is an instance of the expected kotlin class.
 * @see [hasCauseWithClass]
 * @see [hasRootCauseInstanceOf]
 */
@Deprecated(message = "Use cause().isNotNull { it.isInstanceOf(kclass) } instead.",
        replaceWith = ReplaceWith("cause().isNotNull { it.isInstanceOf(kclass) }"))
fun <T : Throwable> Assert<T>.hasCauseInstanceOf(kclass: KClass<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.isInstanceOf(kclass)
    }
}

/**
 * Asserts the throwable's root cause is an instance of the expected kotlin class.
 * @see [hasRootCauseWithClass]
 * @see [hasCauseInstanceOf]
 */
@Deprecated(message = "Use rootCause().isInstanceOf(kclass) instead.",
        replaceWith = ReplaceWith("rootCause().isInstanceOf(kclass)"))
fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(kclass: KClass<out T>) {
    assert(actual.rootCause(), "root cause").isNotNull {
        it.isInstanceOf(kclass)
    }
}

private fun Throwable.rootCause(): Throwable = this.cause?.rootCause() ?: this

