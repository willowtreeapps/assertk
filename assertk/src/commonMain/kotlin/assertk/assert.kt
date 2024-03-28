package assertk

import assertk.assertions.isInstanceOf
import assertk.assertions.support.display
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
sealed class Assert<out T>(val name: String?, internal val context: AssertingContext) {
    /**
     * Transforms an assertion from one type to another. If the assertion is failing the resulting assertion will still
     * be failing, otherwise the mapping function is called. An optional name can be provided, otherwise this
     * assertion's name will be used.
     */
    @Suppress("TooGenericExceptionCaught")
    inline fun <R> transform(name: String? = this.name, transform: (T) -> R): Assert<R> {
        return when (this) {
            is ValueAssert -> {
                try {
                    assertThat(transform(value), name)
                } catch (e: Throwable) {
                    notifyFailure(e)
                    failing<R>(e, name)
                }
            }
            is FailingAssert -> failing(error, name)
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
    @Suppress("TooGenericExceptionCaught")
    inline fun given(assertion: (T) -> Unit) {
        if (this is ValueAssert) {
            try {
                assertion(value)
            } catch (e: Throwable) {
                notifyFailure(e)
            }
        }
    }

    @PublishedApi
    internal fun <R> failing(error: Throwable, name: String? = this.name): Assert<R> {
        return FailingAssert(error, name, context)
    }

    /**
     * Asserts on the given value with an optional name.
     *
     * ```
     * assertThat(true, name = "true").isTrue()
     * ```
     */
    abstract fun <R> assertThat(actual: R, name: String? = this.name): Assert<R>
}

@PublishedApi
internal class ValueAssert<out T>(val value: T, name: String?, context: AssertingContext) :
    Assert<T>(name, context) {

    override fun <R> assertThat(actual: R, name: String?): Assert<R> {
        val newContext = if (context.originatingSubject != null || this.value == actual) {
            context
        } else {
            context.copy(originatingSubject = this.value)
        }
        return ValueAssert(actual, name, newContext)
    }
}

@PublishedApi
internal class FailingAssert<out T>(val error: Throwable, name: String?, context: AssertingContext) :
    Assert<T>(name, context) {
    override fun <R> assertThat(actual: R, name: String?): Assert<R> = FailingAssert(error, name, context)
}

internal data class AssertingContext(
    val originatingSubject: Any? = null,
    val displayOriginatingSubject: () -> String
)

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
 * assertThat(true, name = "true").isTrue()
 * ```
 */
fun <T> assertThat(
    actual: T,
    name: String? = null,
    displayActual: (T) -> String = { display(it) }
): Assert<T> = ValueAssert(
    value = actual,
    name = name,
    context = AssertingContext { displayActual(actual) }
)

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
    SoftFailure(message).run {
        body()
    }
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
    SoftFailure().run {
        body()
    }
}

/**
 * Runs all assertions in the given lambda and reports any failures.
 */
inline fun assertAll(f: () -> Unit) {
    Failure.soft().run { f() }
}

/**
 * Asserts that the given block will throw an exception rather than complete successfully.
 */
inline fun assertFailure(f: () -> Unit): Assert<Throwable> {
    @Suppress("TooGenericExceptionCaught") // Intentionally capturing all exceptions.
    try {
        f()
    } catch (t: Throwable) {
        return assertThat(t)
    }
    fail("expected failure but lambda completed successfully")
}

/**
 * Asserts that the given block will throw an exception with expected type rather than complete successfully.
 */
inline fun <reified T: Throwable> assertFailureWith(f: () -> Unit): Assert<T> {
    val assertFailure = assertFailure(f)
    assertFailure.isInstanceOf(T::class)
    @Suppress("UNCHECKED_CAST")
    return assertFailure as Assert<T>
}