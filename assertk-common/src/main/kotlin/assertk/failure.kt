package assertk

import com.willowtreeapps.opentest4k.AssertionFailedError
import com.willowtreeapps.opentest4k.MultipleFailuresError
import com.willowtreeapps.opentest4k.failures

/**
 * Fail the test with the given {@link AssertionError}.
 */
fun fail(error: AssertionError): Nothing {
    failWithNotInStacktrace(error)
}

/**
 * Fail the test with the given message.
 */
fun fail(message: String, expected: Any? = null, actual: Any? = null): Nothing {
    failWithNotInStacktrace(AssertionFailedError(message, expected, actual, null))
}

/**
 * Used by methods that collect multiple failures, ex: `assertAll`.
 */
internal inline fun collectFailures(f: () -> Unit, catcher: (Throwable) -> Unit) {
    try {
        f()
    } catch (e: Throwable) {
        throwIfFatal(e)
        if (e is MultipleFailuresError) {
            for (failure in e.failures) {
                catcher(failure)
            }
        } else {
            catcher(e)
        }
    }
}

/**
 * Excludes assertk from the stacktrace.
 */
internal expect inline fun failWithNotInStacktrace(error: AssertionError): Nothing

/**
 * Throws given exception if it can't be handled. Ex: out of memory.
 */
internal expect inline fun throwIfFatal(e: Throwable)
