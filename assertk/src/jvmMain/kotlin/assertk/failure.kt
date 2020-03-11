@file:JvmName("FailureJVMKt")
@file:Suppress("NOTHING_TO_INLINE")

package assertk

internal actual inline fun failWithNotInStacktrace(error: Throwable): Nothing {
    val filtered = error.stackTrace
            .dropWhile { it.className.startsWith("assertk") }
            .toTypedArray()
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UnsafeCast")
    error.stackTrace = filtered
    throw error
}

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
internal actual inline fun Throwable.addSuppressed(error: Throwable) {
    throw NotImplementedError()
}

internal actual inline fun Throwable.isOutOfMemory(): Boolean = this is OutOfMemoryError
