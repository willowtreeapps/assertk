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
 * Returns an assert on the throwable's stack trace.
 */
fun <T : Throwable> Assert<T>.stackTrace() = prop("stack trace", { it.stackTrace.map { it.toString() } })

/**
 * Asserts the throwable has the expected message.
 */
@Deprecated(message = "Use message().isEqualTo(message) instead.",
        replaceWith = ReplaceWith("message().isEqualTo(message)"))
fun <T : Throwable> Assert<T>.hasMessage(message: String?) {
    assert(actual.message, "message").isNotNull {
        it.isEqualTo(message)
    }
}

/**
 * Asserts the throwable has the expected cause.
 * @see [hasNoCause]
 */
@Deprecated(message = "Use cause().isEqualTo(cause) instead.",
        replaceWith = ReplaceWith("cause().isEqualTo(cause)"))
fun <T : Throwable> Assert<T>.hasCause(cause: Throwable) {
    assert(actual.cause, "cause").isEqualTo(cause)
}

/**
 * Asserts the throwable has no cause.
 * @see [hasCause]
 */
@Deprecated(message = "Use cause().isNull() instead.", replaceWith = ReplaceWith("cause().isNull()"))
fun <T : Throwable> Assert<T>.hasNoCause() {
    if (actual.cause == null) return
    expected("[cause] to not exist but was:${show(actual.cause)}")
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
 * Asserts the throwable's cause is an instance of the expected kotlin class.
 * @see [hasCauseWithClass]
 * @see [hasRootCauseInstanceOf]
 */
@Deprecated(message = "Use cause().isNotNull { it.isInstanceOf(kclass) } instead.",
        replaceWith = ReplaceWith("cause().isNotNull { it.isInstanceOf(kclass) }"))
fun <T : Throwable> Assert<T>.hasCauseInstanceOf(kclass: KClass<T>) {
    assert(actual.cause, "cause").isNotNull {
        it.isInstanceOf(kclass)
    }
}

/**
 * Asserts the throwable's cause is an instance of the expected java class.
 * @see [hasCauseWithClass]
 * @see [hasRootCauseInstanceOf]
 */
@Deprecated(message = "Use cause().isNotNull { it.isInstanceOf(jclass) } instead.",
        replaceWith = ReplaceWith("cause().isNotNull { it.isInstanceOf(jclass) }"))
fun <T : Throwable> Assert<T>.hasCauseInstanceOf(jclass: Class<T>) {
    assert(actual.cause, "cause").isNotNull {
        it.isInstanceOf(jclass)
    }
}

/**
 * Asserts the throwable's cause matches the expected kotlin class.
 * @see [hasCauseInstanceOf]
 * @see [hasRootCauseWithClass]
 */
@Deprecated(message = "Use cause().isNotNull { it.kClass().isEqualTo(kclass) } instead.",
        replaceWith = ReplaceWith("cause().isNotNull { it.kClass().isEqualTo(kclass) }"))
fun <T : Throwable> Assert<T>.hasCauseWithClass(kclass: KClass<T>) {
    assert(actual.cause, "cause").isNotNull {
        it.kClass().isEqualTo(kclass)
    }
}

/**
 * Asserts the throwable's cause matches the expected java class.
 * @see [hasCauseInstanceOf]
 * @see [hasRootCauseWithClass]
 */
@Deprecated(message = "Use cause().isNotNull { it.jClass().isEqualTo(jclass) } instead.",
        replaceWith = ReplaceWith("cause().isNotNull { it.jClass().isEqualTo(jclass) }"))
fun <T : Throwable> Assert<T>.hasCauseWithClass(jclass: Class<T>) {
    assert(actual.cause, "cause").isNotNull {
        it.jClass().isEqualTo(jclass)
    }
}

/**
 * Asserts the throwable has the expected root cause.
 */
@Deprecated(message = "Use rootCause().isEqualTo(cause) instead.",
        replaceWith = ReplaceWith("rootCause().isEqualTo(cause)"))
fun <T : Throwable> Assert<T>.hasRootCause(cause: Throwable) {
    assert(actual.rootCause(), "root cause").isEqualTo(cause)

}

/**
 * Asserts the throwable's root cause is an instance of the expected kotlin class.
 * @see [hasRootCauseWithClass]
 * @see [hasCauseInstanceOf]
 */
@Deprecated(message = "Use rootCause().isInstanceOf(kclass) instead.",
        replaceWith = ReplaceWith("rootCause().isInstanceOf(kclass)"))
fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(kclass: KClass<T>) {
    assert(actual.rootCause(), "root cause").isInstanceOf(kclass)
}

/**
 * Asserts the throwable's root cause is an instance of the expected java class.
 * @see [hasRootCauseWithClass]
 * @see [hasCauseInstanceOf]
 */
@Deprecated(message = "Use rootCause().isInstanceOf(jclass) instead.",
        replaceWith = ReplaceWith("rootCause().isInstanceOf(jclass)"))
fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(jclass: Class<T>) {
    assert(actual.rootCause(), "root cause").isInstanceOf(jclass)
}

/**
 * Asserts the throwable's root cause matches the expected kotlin class.
 * @see [hasRootCauseInstanceOf]
 * @see [hasCauseWithClass]
 */
@Deprecated(message = "Use rootCause().isNotNull { it.kClass().isEqualTo(kclass) } instead.",
        replaceWith = ReplaceWith("rootCause().isNotNull { it.kClass().isEqualTo(kclass) }"))
fun <T : Throwable> Assert<T>.hasRootCauseWithClass(kclass: KClass<T>) {
    val assertion: Assert<Throwable?> = assert(actual.rootCause(), "root cause")
    assertion.isNotNull {
        it.kClass().isEqualTo(kclass)
    }
}

/**
 * Asserts the throwable's root cause matches the expected java class.
 * @see [hasRootCauseInstanceOf]
 * @see [hasCauseWithClass]
 */
@Deprecated(message = "Use rootCause().isNotNull { it.jClass().isEqualTo(jclass) } instead.",
        replaceWith = ReplaceWith("rootCause().isNotNull { it.jClass().isEqualTo(jclass) }"))
fun <T : Throwable> Assert<T>.hasRootCauseWithClass(jclass: Class<T>) {
    val assertion: Assert<Throwable?> = assert(actual.rootCause(), "root cause")
    assertion.isNotNull {
        it.jClass().isEqualTo(jclass)
    }
}

private fun Throwable.rootCause(): Throwable =
        this.cause?.rootCause() ?: this

/**
 * Asserts the throwable's stacktrace contains the expected string.
 */
@Deprecated(message = "Use stackTrace().contains(description) instead.",
        replaceWith = ReplaceWith("stackTrace().contains(description)"))
fun <T : Throwable> Assert<T>.hasStackTraceContaining(description: String) {
    stackTrace().contains(description)
}

