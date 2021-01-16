package assertk

import assertk.Failure.Companion.soft
import com.willowtreeapps.opentest4k.AssertionFailedError
import com.willowtreeapps.opentest4k.MultipleFailuresError
import com.willowtreeapps.opentest4k.failures

/**
 * Assertions are run in a failure context which captures failures to report them.
 */
internal object FailureContext {
    private val failureRef = ThreadLocalRef<MutableList<Failure>> { mutableListOf(SimpleFailure) }

    fun pushFailure(failure: Failure) {
        failureRef.value.add(failure)
    }

    fun popFailure() {
        failureRef.value.apply {
            if (size > 1) {
                removeAt(size - 1)
            }
        }
    }

    fun fail(error: Throwable) {
        if (error.isOutOfMemory()) throw error
        failureRef.value.last().fail(error)
    }
}

/**
 * Interface for reporting failures. They should be collected by [fail] and then triggered by [invoke]. The default
 * implementation throws an exception immediately. The [soft] implementation will collect failures and throw an
 * exception when [invoke] is called.
 */
@PublishedApi
internal interface Failure {
    /**
     * Record a failure. Depending on the implementation this may throw an exception or collect the failure for later.
     */
    fun fail(error: Throwable)

    /**
     * Triggers any collected failures.
     */
    operator fun invoke() {
    }

    /**
     * Pushes this failure making it the current context for use with [fail]. You should prefer using [run] instead as
     * it will properly pop the failure for you.
     */
    fun pushFailure() {
        FailureContext.pushFailure(this)
    }

    /**
     * Pops this failure making the current context throw immediately again. You should prefer using [run] instead as
     * it will properly call this for you.
     */
    fun popFailure() {
        FailureContext.popFailure()
    }

    companion object {
        /**
         * Returns a new soft failure.
         */
        fun soft(): Failure = SoftFailure()
    }
}

/**
 * Run the given block of assertions with its Failure.
 */
@PublishedApi
internal inline fun <F: Failure, T> F.run(f: F.() -> T): T {
    pushFailure()
    try {
        return f()
    } finally {
        popFailure()
        invoke()
    }
}

/**
 * Failure that immediately thrown an exception.
 */
internal object SimpleFailure : Failure {
    override fun fail(error: Throwable) {
        failWithNotInStacktrace(error)
    }
}

/**
 * Failure that collects all failures and displays them at once.
 */
internal class SoftFailure(
    val message: String = defaultMessage,
    val failIf: (List<Throwable>) -> Boolean = { it.isNotEmpty() }
) :
    Failure {
    private val failures: MutableList<Throwable> = ArrayList()

    override fun fail(error: Throwable) {
        // flatten multiple failures into this one.
        if (error is MultipleFailuresError) {
            failures.addAll(error.failures)
        } else {
            failures.add(error)
        }
    }

    val count: Int
        get() = failures.size

    override fun invoke() {
        if (failIf(failures)) {
            FailureContext.fail(compositeErrorMessage(failures))
        }
    }

    private fun compositeErrorMessage(errors: List<Throwable>): Throwable {
        return when (errors.size) {
            0 -> AssertionFailedError(message)
            1 -> errors.first()
            else -> MultipleFailuresError(message, errors).apply {
                errors.forEach(this::addSuppressed)
            }
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

internal val NONE: Any = Any()

/**
 * Fail the test with the given message.
 */
fun fail(message: String, expected: Any? = NONE, actual: Any? = NONE): Nothing {
    if (expected === NONE && actual === NONE) {
        throw AssertionFailedError(message)
    } else {
        throw AssertionFailedError(
            message,
            if (expected === NONE) null else expected,
            if (actual === NONE) null else actual
        )
    }
}

fun notifyFailure(e: Throwable) {
    FailureContext.fail(e)
}

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
internal expect inline fun Throwable.addSuppressed(error: Throwable)

internal expect inline fun Throwable.isOutOfMemory(): Boolean

internal expect inline fun failWithNotInStacktrace(error: Throwable): Nothing

