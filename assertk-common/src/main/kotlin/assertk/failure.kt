package assertk

import com.willowtreeapps.opentest4k.AssertionFailedError
import com.willowtreeapps.opentest4k.MultipleFailuresError
import com.willowtreeapps.opentest4k.failures

/**
 * Failure that collects all failures and displays them at once.
 */
internal fun softFailure(f: (SoftFailure) -> Unit) {
    SoftFailure().apply(f)()
}

internal class SoftFailure {
    private val errors = mutableListOf<Throwable>()

    fun add(e: Throwable) {
        if (e is MultipleFailuresError) {
            errors.addAll(e.failures)
        } else {
            errors.add(e)
        }
    }

    operator fun invoke() {
        if (!errors.isEmpty()) {
            throw compositeErrorMessage(errors)
        }
    }

    fun compositeErrorMessage(errors: List<Throwable>): Throwable {
        return if (errors.size == 1) {
            errors.first()
        } else {
            MultipleFailuresError("The following assertions failed", errors)
        }
    }
}

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

internal expect inline fun failWithNotInStacktrace(error: AssertionError): Nothing
