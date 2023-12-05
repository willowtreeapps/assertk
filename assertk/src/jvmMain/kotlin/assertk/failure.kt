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

@PublishedApi
internal actual inline fun Throwable.isFatal(): Boolean =
    // https://github.com/ReactiveX/RxJava/blob/6a44e5d0543a48f1c378dc833a155f3f71333bc2/src/main/java/io/reactivex/exceptions/Exceptions.java#L66
    this is VirtualMachineError || this is ThreadDeath || this is LinkageError
