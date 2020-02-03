package assertk

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun failWithNotInStacktrace(error: Throwable): Nothing {
    throw error
}
