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
            val result = f()
            this.failure = tmp
            failure()
            return result
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
internal class SoftFailure : Failure {
    private val failures: MutableList<AssertionError> = ArrayList()

    override fun fail(error: AssertionError) {
        failures.add(error)
    }

    override fun invoke() {
        if (!failures.isEmpty()) {
            FailureContext.failure.fail(compositeErrorMessage(failures))
        }
    }

    private fun compositeErrorMessage(errors: List<AssertionError>): AssertionError {
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
fun fail(error: AssertionError) {
    FailureContext.failure.fail(error)
}

/**
 * Fail the test with the given message.
 */
fun fail(message: String, expected: Any? = null, actual: Any? = null) {
    FailureContext.failure.fail(AssertionFailedError(message, expected, actual, null))
}

internal expect inline fun failWithNotInStacktrace(error: AssertionError): Nothing
