package assertk

actual
@Suppress("NOTHING_TO_INLINE", "UndocumentedPublicFunction")
internal inline fun failWithNotInStacktrace(error: AssertionError): Nothing {
    throw error
}
