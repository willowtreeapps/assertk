package assertk

import assertk.assertions.support.show

/**
 * An assertion. Holds an actual value to assertion on and an optional name.
 * @see [assert]
 */
class Assert<out T> internal constructor(val name: String? = null, val actual: T)

/**
 * An assertion on a block of code. Can assert that it either throws and error or returns a value.
 */
sealed class AssertBlock<out T> {
    /**
     * Runs the given lambda if the block throws an error, otherwise fails.
     */
    abstract fun thrownError(f: Assert<Throwable>.() -> Unit)

    /**
     * Runs the given lambda if the block returns a value, otherwise fails.
     */
    abstract fun returnedValue(f: Assert<T>.() -> Unit)

    internal class Value<out T> internal constructor(private val value: T) : AssertBlock<T>() {
        override fun thrownError(f: Assert<Throwable>.() -> Unit) = fail("expected exception but was:${show(value)}")

        override fun returnedValue(f: Assert<T>.() -> Unit) {
            f(Assert(actual = value))
        }
    }

    internal class Error<out T> internal constructor(private val error: Throwable) : AssertBlock<T>() {
        override fun thrownError(f: Assert<Throwable>.() -> Unit) {
            f(Assert(actual = error))
        }

        override fun returnedValue(f: Assert<T>.() -> Unit) = fail("expected value but threw:${show(error)}")
    }
}

/**
 * Asserts on the given value with an optional name.
 *
 * ```
 * assert(true, name = "true").isTrue()
 * ```
 */
fun <T> assert(actual: T, name: String? = null): Assert<T> = Assert(name, actual)

/**
 * Asserts on the given value with an optional name. All assertions in the given lambda are run.
 *
 * ```
 * assert("test", name = "test") {
 *   hasLength(4)
 *   endsWith("t")
 * }
 * ```
 */
fun <T> assert(actual: T, name: String? = null, f: Assert<T>.() -> Unit) {
    FailureContext.run(SoftFailure()) {
        f(Assert(name, actual))
    }
}

/**
 * Asserts on the given block. You can test that it returns a value or throws an exception.
 *
 * ```
 * assert { 1 + 1 }.returnedValue {
 *   isPositive()
 * }
 *
 * assert {
 *   throw Exception("error")
 * }.thrownError {
 *   hasMessage("error")
 * }
 * ```
 */
fun <T> assert(f: () -> T): AssertBlock<T> {
    return FailureContext.run(SoftFailure()) {
        @Suppress("TooGenericExceptionCaught")
        try {
            AssertBlock.Value(f())
        } catch (e: Throwable) {
            AssertBlock.Error(e)
        }
    }
}

/**
 * Runs all assertions in the given lambda and reports any failures.
 */
fun assertAll(f: () -> Unit) {
    FailureContext.run(SoftFailure(), f)
}

/**
 * Catches any exceptions thrown in the given lambda and returns it. This is an easy way to assert on expected thrown
 * exceptions.
 *
 * ```
 * val exception = catch { throw Exception("error") }
 * assert(exception).isNotNull {
 *   hasMessage("error")
 * }
 * ```
 */
fun catch(f: () -> Unit): Throwable? {
    @Suppress("TooGenericExceptionCaught")
    try {
        f()
        return null
    } catch (e: Throwable) {
        return e
    }
}
