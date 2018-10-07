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
@Deprecated(
    message = "Use cause().isNotNull { it.isInstanceOf(jclass) } instead.",
    replaceWith = ReplaceWith("cause().isNotNull { it.isInstanceOf(jclass) }"),
    level = DeprecationLevel.ERROR
)
fun <T : Throwable> Assert<T>.hasCauseInstanceOf(jclass: Class<out T>) {
    cause().isNotNull().isInstanceOf(jclass)
}

/**
 * Asserts the throwable's cause matches the expected java class.
 * @see [hasCauseInstanceOf]
 * @see [hasRootCauseWithClass]
 */
@Deprecated(
    message = "Use cause().isNotNull { it.jClass().isEqualTo(jclass) } instead.",
    replaceWith = ReplaceWith("cause().isNotNull { it.jClass().isEqualTo(jclass) }"),
    level = DeprecationLevel.ERROR
)
fun <T : Throwable> Assert<T>.hasCauseWithClass(jclass: Class<out T>) {
    cause().isNotNull().jClass().isEqualTo(jclass)
}

/**
 * Asserts the throwable's root cause is an instance of the expected java class.
 * @see [hasRootCauseWithClass]
 * @see [hasCauseInstanceOf]
 */
@Deprecated(
    message = "Use rootCause().isInstanceOf(jclass) instead.",
    replaceWith = ReplaceWith("rootCause().isInstanceOf(jclass)"),
    level = DeprecationLevel.ERROR
)
fun Assert<Throwable>.hasRootCauseInstanceOf(jclass: Class<Throwable>) {
    rootCause().isInstanceOf(jclass)
}

/**
 * Asserts the throwable's root cause matches the expected java class.
 * @see [hasRootCauseInstanceOf]
 * @see [hasCauseWithClass]
 */
@Deprecated(
    message = "Use rootCause().jClass().isEqualTo(jclass) instead.",
    replaceWith = ReplaceWith("rootCause().jClass().isEqualTo(jclass) }"),
    level = DeprecationLevel.ERROR
)
fun Assert<Throwable>.hasRootCauseWithClass(jclass: Class<Throwable>) {
    rootCause().jClass().isEqualTo(jclass)
}

/**
 * Asserts the throwable's stacktrace contains the expected string.
 */
@Deprecated(
    message = "Use stackTrace().contains(description) instead.",
    replaceWith = ReplaceWith("stackTrace().contains(description)"),
    level = DeprecationLevel.ERROR
)
fun <T : Throwable> Assert<T>.hasStackTraceContaining(description: String) {
    stackTrace().contains(description)
}

