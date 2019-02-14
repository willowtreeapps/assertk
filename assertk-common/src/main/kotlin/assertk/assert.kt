package assertk

import assertk.assertions.support.show
import kotlin.reflect.KProperty0

/**
 * Marks the assertion DSL.
 */
@DslMarker
annotation class AssertkDsl

/**
 * An assertion. Holds an actual value to assertion on and an optional name.
 * @see [assertThat]
 */
@AssertkDsl
sealed class Assert<out T>(val name: String?, internal val context: Any?) {
    /**
     * Transforms an assertion from one type to another. If the assertion is failing the resulting assertion will still
     * be failing, otherwise the mapping function is called. An optional name can be provided, otherwise this
     * assertion's name will be used.
     */
    fun <R> transform(name: String? = this.name, transform: (T) -> R): Assert<R> {
        return when (this) {
            is ValueAssert -> {
                try {
                    assertThat(transform(value), name)
                } catch (e: Throwable) {
                    notifyFailure(e)
                    FailingAssert<R>(e, name, context)
                }
            }
            is FailingAssert -> FailingAssert(error, name, context)
        }
    }

    /**
     * Allows checking the actual value of an assert. This can be used to build your own custom assertions.
     * ```
     * fun Assert<Int>.isTen() = given { actual ->
     *     if (actual == 10) return
     *     expected("to be 10 but was:${show(actual)}")
     * }
     * ```
     */
    inline fun given(assertion: (T) -> Unit) {
        if (this is ValueAssert) {
            try {
                assertion(value)
            } catch (e: Throwable) {
                notifyFailure(e)
            }
        }
    }

    /**
     * Asserts on the given value with an optional name.
     *
     * ```
     * assert(true, name = "true").isTrue()
     * ```
     */
    @Deprecated("Renamed assertThat", replaceWith = ReplaceWith("assertThat(actual, name)"))
    fun <R> assert(actual: R, name: String? = this.name): Assert<R> = assertThat(actual, name)

    /**
     * Asserts on the given value with an optional name.
     *
     * ```
     * assertThat(true, name = "true").isTrue()
     * ```
     */
    abstract fun <R> assertThat(actual: R, name: String? = this.name): Assert<R>

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Use `given` or `transform` to access the actual value instead")
    val actual: T
        get() = when (this) {
            is ValueAssert -> value
            is FailingAssert -> throw error
        }
}

@AssertkDsl
class ValueAssert<out T> internal constructor(val value: T, name: String?, context: Any?) :
    Assert<T>(name, context) {

    override fun <R> assertThat(actual: R, name: String?): Assert<R> =
        ValueAssert(actual, name, if (context != null || this.value === actual) context else this.value)
}

class FailingAssert<out T> internal constructor(val error: Throwable, name: String?, context: Any?) :
    Assert<T>(name, context) {
    override fun <R> assertThat(actual: R, name: String?): Assert<R> = FailingAssert(error, name, context)
}

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

    abstract fun doesNotThrowAnyException()

    internal class Value<out T> internal constructor(private val value: T) : AssertBlock<T>() {
        override fun thrownError(f: Assert<Throwable>.() -> Unit) {
            fail("expected exception but was:${show(value)}")
        }

        override fun returnedValue(f: Assert<T>.() -> Unit) {
            f(assertThat(value))
        }

        override fun doesNotThrowAnyException() {
            assertThat(value)
        }
    }

    internal class Error<out T> internal constructor(private val error: Throwable) : AssertBlock<T>() {
        override fun thrownError(f: Assert<Throwable>.() -> Unit) {
            f(assertThat(error))
        }

        override fun returnedValue(f: Assert<T>.() -> Unit) {
            fail("expected value but threw:${showError(error)}")
        }

        override fun doesNotThrowAnyException() {
            fail("expected to not throw an exception but threw:${showError(error)}")
        }
    }
}

/**
 * Calls platform specific function so that it is possible to show stacktrace if able
 *
 * TODO: use @OptionalExpectation (https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-optional-expectation/index.html) here once available and call default implementation of [show] for JS
 */
internal expect fun showError(e: Throwable): String

/**
 * Asserts on the given value with an optional name.
 *
 * ```
 * assert(true, name = "true").isTrue()
 * ```
 */
@Deprecated("Renamed assertThat", replaceWith = ReplaceWith("assertThat(actual, name)"))
fun <T> assert(actual: T, name: String? = null): Assert<T> = assertThat(actual, name)

/**
 * Asserts on the given value with an optional name.
 *
 * ```
 * assertThat(true, name = "true").isTrue()
 * ```
 */
fun <T> assertThat(actual: T, name: String? = null): Assert<T> = ValueAssert(actual, name, null)

/**
 * Asserts on the given property reference using its name, if no explicit name is specified. This method
 * should be preferred in cases, where property references can be used, as it uses the property's name
 * for the assertion automatically. The name may optionally be overridden, if needed.
 *
 * ```
 * data class Person(val name: String)
 *
 * val p = Person("Hugo")
 *
 * assertThat(p::name).contains("u")
 * ```
 */
fun <T> assertThat(getter: KProperty0<T>, name: String? = null): Assert<T> =
        assertThat(getter.get(), name ?: getter.name)

/**
 * All assertions in the given lambda are run.
 *
 * ```
 * assertThat("test", name = "test").all {
 *   startsWith("t")
 *   endsWith("t")
 * }
 * ```
 * @param message An optional message to show before all failures.
 * @param body The body to execute.
 */
fun <T> Assert<T>.all(message: String = SoftFailure.defaultMessage, body: Assert<T>.() -> Unit) {
    all(message, body, { it.isNotEmpty() })
}

/**
 * All assertions in the given lambda are run, with their failures collected. If `failIf` returns true then a failure
 * happens, otherwise they are ignored.
 *
 * ```
 * assert("test", name = "test").all(
 *   message = "my message",
 *   body = {
 *     startsWith("t")
 *     endsWith("t")
 *   }, {
 *     it.size > 1
 *   }
 * )
 * ```
 *
 * @param message An optional message to show before all failures.
 * @param body The body to execute.
 * @param failIf Fails if this returns true, ignores failures otherwise.
 */
// Hide for now, not sure if happy with api.
internal fun <T> Assert<T>.all(
    message: String,
    body: Assert<T>.() -> Unit,
    failIf: (List<AssertionError>) -> Boolean
) {
    FailureContext.run(SoftFailure(message, failIf)) {
        body()
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
@Deprecated("Renamed assertThat", replaceWith = ReplaceWith("assertThat(f)"))
fun <T> assert(f: () -> T): AssertBlock<T> = assertThat(f)

/**
 * Asserts on the given block. You can test that it returns a value or throws an exception.
 *
 * ```
 * assertThat { 1 + 1 }.returnedValue {
 *   isPositive()
 * }
 *
 * assertThat {
 *   throw Exception("error")
 * }.thrownError {
 *   hasMessage("error")
 * }
 * ```
 */
fun <T> assertThat(f: () -> T): AssertBlock<T> {
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
 * assertThat(exception).isNotNull().hasMessage("error")
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
