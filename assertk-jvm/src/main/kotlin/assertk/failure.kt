@file:JvmName("FailureJVMKt")

package assertk

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun failWithNotInStacktrace(error: AssertionError): Nothing {
    val filtered = error.stackTrace
        .dropWhile { it.className.startsWith("assertk") }
        .toTypedArray()
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UnsafeCast")
    error.stackTrace = filtered
    throw error
}
