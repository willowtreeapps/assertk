@file:JvmName("ThrowableJVMKt")
package assertk.assertions

import assertk.Assert

/**
 * Returns an assert on the throwable's stack trace.
 */
fun <T : Throwable> Assert<T>.stackTrace() = prop("stack trace", { it.stackTrace.map { it.toString() } })

/**
 * Asserts the throwable's cause is an instance of the expected java class.
 * @see [hasCauseWithClass]
 * @see [hasRootCauseInstanceOf]
 */
@Deprecated(message = "Use cause().isNotNull { it.isInstanceOf(jclass) } instead.",
        replaceWith = ReplaceWith("cause().isNotNull { it.isInstanceOf(jclass) }"))
fun <T : Throwable> Assert<T>.hasCauseInstanceOf(jclass: Class<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.isInstanceOf(jclass)
    }
}

/**
 * Asserts the throwable's cause matches the expected java class.
 * @see [hasCauseInstanceOf]
 * @see [hasRootCauseWithClass]
 */
@Deprecated(message = "Use cause().isNotNull { it.jClass().isEqualTo(jclass) } instead.",
        replaceWith = ReplaceWith("cause().isNotNull { it.jClass().isEqualTo(jclass) }"))
fun <T : Throwable> Assert<T>.hasCauseWithClass(jclass: Class<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.jClass().isEqualTo(jclass)
    }
}

/**
 * Asserts the throwable's root cause is an instance of the expected java class.
 * @see [hasRootCauseWithClass]
 * @see [hasCauseInstanceOf]
 */
@Deprecated(message = "Use rootCause().isInstanceOf(jclass) instead.",
        replaceWith = ReplaceWith("rootCause().isInstanceOf(jclass)"))
fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(jclass: Class<out T>) {
    hasRootCauseInstanceOf(jclass.kotlin)
}

/**
 * Asserts the throwable's root cause matches the expected java class.
 * @see [hasRootCauseInstanceOf]
 * @see [hasCauseWithClass]
 */
@Deprecated(message = "Use rootCause().isNotNull { it.jClass().isEqualTo(jclass) } instead.",
        replaceWith = ReplaceWith("rootCause().isNotNull { it.jClass().isEqualTo(jclass) }"))
fun <T : Throwable> Assert<T>.hasRootCauseWithClass(jclass: Class<out T>) {
    hasRootCauseWithClass(jclass.kotlin)
}

/**
 * Asserts the throwable's stacktrace contains the expected string.
 */
@Deprecated(message = "Use stackTrace().contains(description) instead.",
        replaceWith = ReplaceWith("stackTrace().contains(description)"))
fun <T : Throwable> Assert<T>.hasStackTraceContaining(description: String) {
    assert(actual.stackTrace.map { it.toString() }, "stack trace").contains(description)
}

