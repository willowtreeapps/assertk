@file:Suppress("DEPRECATION")

package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import assertk.showError

/**
 * Asserts the given [assertk.Result] successful returned a value, returning it's result if it did or failing if it didn't.
 *
 * ```
 * assertThat { 1 + 1 }.isSuccess().isEqualTo(2)
 * ```
 */
fun <T> Assert<assertk.Result<T>>.isSuccess(): Assert<T> = transform { actual ->
    if (actual.isSuccess) {
        @Suppress("UNCHECKED_CAST")
        actual.getOrNull() as T
    } else {
        expected("success but was failure:${showError(actual.exceptionOrNull()!!)}")
    }
}

/**
 * Asserts the given [assertk.Result] threw an exception, returning that exception if it was or failing it if didn't.
 *
 * ```
 * assertThat { throw Exception("error") }.isFailure().hasMessage("error")
 * ```
 */
fun <T> Assert<assertk.Result<T>>.isFailure(): Assert<Throwable> = transform { actual ->
    actual.exceptionOrNull() ?: expected("failure but was success:${show(actual.getOrNull())}")
}
