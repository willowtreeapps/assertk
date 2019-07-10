package assertk

import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import assertk.assertions.support.show
import kotlin.contracts.contract
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
    @Deprecated(
        "Renamed assertThat",
        replaceWith = ReplaceWith("assertThat(actual, name)"),
        level = DeprecationLevel.ERROR
    )
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
    @Deprecated(
        message = "Use `given` or `transform` to access the actual value instead",
        level = DeprecationLevel.ERROR
    )
    val actual: T
        get() = when (this) {
            is ValueAssert -> value
            is FailingAssert -> throw error
        }
}

@PublishedApi
internal class ValueAssert<out T>(val value: T, name: String?, context: Any?) :
    Assert<T>(name, context) {

    override fun <R> assertThat(actual: R, name: String?): Assert<R> =
        ValueAssert(actual, name, if (context != null || this.value === actual) context else this.value)
}

internal class FailingAssert<out T>(val error: Throwable, name: String?, context: Any?) :
    Assert<T>(name, context) {
    override fun <R> assertThat(actual: R, name: String?): Assert<R> = FailingAssert(error, name, context)
}

/**
 * Runs the given lambda if the block throws an error, otherwise fails.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "Use isFailure().all(f) instead",
    replaceWith = ReplaceWith("isFailure().all(f)", imports = ["assertk.assertions.isFailure", "assertk.all"])
)
fun <T> Assert<Result<T>>.thrownError(f: Assert<Throwable>.() -> Unit) {
    isFailure().all(f)
}

/**
 * Runs the given lambda if the block returns a value, otherwise fails.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "Use isSuccess().all(f) instead",
    replaceWith = ReplaceWith("isSuccess().all(f)", imports = ["assertk.assertions.isSuccess", "assertk.all"])
)
fun <T> Assert<Result<T>>.returnedValue(f: Assert<T>.() -> Unit) {
    isSuccess().all(f)
}

@Suppress("DEPRECATION")
@Deprecated(
    message = "Use isSuccess() instead",
    replaceWith = ReplaceWith("isSuccess()", imports = ["assertk.assertions.isSuccess", "assertk.assertions"])
)
fun <T> Assert<Result<T>>.doesNotThrowAnyException() {
    isSuccess()
}

@Suppress("DEPRECATION")
@Deprecated(message = "Use Assert<Result<T>> instead")
typealias AssertBlock<T> = Assert<Result<T>>

@Suppress("DEPRECATION")
@Deprecated(message = "Temporary replacement for kotlin.Result until https://youtrack.jetbrains.com/issue/KT-32450 is fixed.")
sealed class Result<out T> {
    companion object {
        fun <T> success(value: T): Result<T> = Success(value)
        fun <T> failure(error: Throwable): Result<T> = Failure(error)

        inline fun <R> runCatching(block: () -> R): Result<R> {
            return try {
                success(block())
            } catch (e: Throwable) {
                failure(e)
            }
        }
    }

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    fun exceptionOrNull(): Throwable? = when (this) {
        is Success -> null
        is Failure -> error
    }

    private data class Success<T>(val value: T) : Result<T>() {
        override fun toString(): String = "Success($value)"
    }

    private data class Failure<T>(val error: Throwable) : Result<T>() {
        override fun toString(): String = "Failure($error)"
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
@Deprecated("Renamed assertThat", replaceWith = ReplaceWith("assertThat(actual, name)"), level = DeprecationLevel.ERROR)
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
fun <T> Assert<T>.all(message: String, body: Assert<T>.() -> Unit) {
    all(message, body, { it.isNotEmpty() })
}

/**
 * All assertions in the given lambda are run.
 *
 * ```
 * assertThat("test", name = "test").all {
 *   startsWith("t")
 *   endsWith("t")
 * }
 * ```
 * @param body The body to execute.
 */
fun <T> Assert<T>.all(body: Assert<T>.() -> Unit) {
    all(SoftFailure.defaultMessage, body, { it.isNotEmpty() })
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
    SoftFailure(message, failIf).run {
        body()
    }
}

/**
 * Asserts on the given block returning an `Assert<Result<T>>`. You can test that it returns a value or throws an exception.
 *
 * ```
 * assert { 1 + 1 }.isSuccess().isPositive()
 *
 * assert {
 *   throw Exception("error")
 * }.isFailure().hasMessage("error")
 * ```
 */
@Suppress("DEPRECATION")
@Deprecated("Renamed assertThat", replaceWith = ReplaceWith("assertThat(f)"), level = DeprecationLevel.ERROR)
fun <T> assert(f: () -> T): Assert<Result<T>> = assertThat(f)

/**
 * Asserts on the given block returning an `Assert<Result<T>>`. You can test that it returns a value or throws an exception.
 *
 * ```
 * assertThat { 1 + 1 }.isSuccess().isPositive()
 *
 * assertThat {
 *   throw Exception("error")
 * }.isFailure().hasMessage("error")
 * ```
 */
@Suppress("DEPRECATION")
inline fun <T> assertThat(f: () -> T): Assert<Result<T>> = assertThat(Result.runCatching(f))

/**
 * Runs all assertions in the given lambda and reports any failures.
 */
inline fun assertAll(f: () -> Unit) {
    Failure.soft().run(f)
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
@Suppress("TooGenericExceptionCaught")
@Deprecated("Use assertThat { }.isFailure() instead")
inline fun catch(f: () -> Unit): Throwable? {
    try {
        f()
        return null
    } catch (e: Throwable) {
        return e
    }
}
