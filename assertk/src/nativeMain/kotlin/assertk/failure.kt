@file:Suppress("NOTHING_TO_INLINE")

package assertk

internal actual inline fun failWithNotInStacktrace(error: Throwable): Nothing {
    throw error
}

@PublishedApi
internal actual inline fun Throwable.isOutOfMemory(): Boolean = this is OutOfMemoryError
