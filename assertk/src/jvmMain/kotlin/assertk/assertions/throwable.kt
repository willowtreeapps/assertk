@file:JvmName("ThrowableJVMKt")

package assertk.assertions

import assertk.Assert

/**
 * Returns an assert on the throwable's stack trace.
 */
fun Assert<Throwable>.havingStackTrace() = having("stackTrace") { it.stackTrace.map(StackTraceElement::toString) }

@Deprecated(
  message = "Function stackTrace has been renamed to havingStackTrace",
  replaceWith = ReplaceWith("havingStackTrace()"),
  level = DeprecationLevel.WARNING
)
fun Assert<Throwable>.stackTrace() = havingStackTrace()
