@file:JvmName("ThrowableJVMKt")

package assertk.assertions

import assertk.Assert

/**
 * Returns an assert on the throwable's stack trace.
 */
fun Assert<Throwable>.stackTrace() = prop("stackTrace") { it.stackTrace.map(StackTraceElement::toString) }
