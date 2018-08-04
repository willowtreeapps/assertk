package assertk

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun failWithNotInStacktrace(error: AssertionError): Nothing {
    throw error
}