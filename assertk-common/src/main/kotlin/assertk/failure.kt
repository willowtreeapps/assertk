package assertk

import com.willowtreeapps.opentest4k.AssertionFailedError
import com.willowtreeapps.opentest4k.MultipleFailuresError

/**
 * Assertions are run in a failure context which captures failures to report them.
 */
internal object FailureContext {
    internal var failure: Failure = SimpleFailure()

    /**
     * Run the given block of assertions in the given context. If we are already in a context a new one will not be
     * created.
     */
    fun <T> run(failure: Failure, f: () -> T): T {
        if (this.failure is SimpleFailure) {
            val tmp = this.failure
            this.failure = failure
            try {
                return f()
            } finally {
                this.failure = tmp
                failure()
            }
        } else {
            return f()
        }
    }
}

internal interface Failure {
    /**
     * Record a failure. Depending on the implementation this may throw an exception or collect the failure for later.
     */
    fun fail(error: AssertionError)

    /**
     * Triggers any collected failures.
     */
    operator fun invoke() {
    }
}

/**
 * Failure that immediately thrown an exception.
 */
internal class SimpleFailure : Failure {
    override fun fail(error: AssertionError) {
        failWithNotInStacktrace(error)
    }
}

/**
 * Failure that collects all failures and displays them at once.
 */
internal class SoftFailure(
    val message: String = defaultMessage,
    val failIf: (List<AssertionError>) -> Boolean = { it.isNotEmpty() }
) :
    Failure {
    private val failures: MutableList<AssertionError> = ArrayList()

    override fun fail(error: AssertionError) {
        failures.add(error)
    }

    override fun invoke() {
        if (failIf(failures)) {
            FailureContext.failure.fail(compositeErrorMessage(failures))
        }
    }

    private fun compositeErrorMessage(errors: List<AssertionError>): AssertionError {
        return if (errors.size == 1) {
            errors.first()
        } else {
            MultipleFailuresError(message, errors)
        }
    }

    companion object {
        const val defaultMessage = "The following assertions failed"
    }
}

/**
 * Fail the test with the given {@link AssertionError}.
 */
fun fail(error: AssertionError): Nothing {
    throw error
}

/**
 * Fail the test with the given message.
 */
fun fail(message: String, expected: Any? = null, actual: Any? = null): Nothing {
    throw AssertionFailedError(message, expected, actual, null)
}

fun notifyFailure(e: Throwable) {
    FailureContext.failure.fail(if (e is AssertionError) e else AssertionError(e))
}

internal expect inline fun failWithNotInStacktrace(error: AssertionError): Nothing
