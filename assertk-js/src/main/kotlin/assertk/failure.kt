package assertk

internal actual inline fun failWithNotInStacktrace(error: AssertionError): Nothing {
    throw error
}
