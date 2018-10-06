@file:JvmName("ThrowableJVMKt")

package assertk.assertions

import assertk.Assert

/**
 * Returns an assert on the throwable's stack trace.
 */
fun <T : Throwable> Assert<T>.stackTrace() = prop("stack trace", { it.stackTrace.map { it.toString() } })
