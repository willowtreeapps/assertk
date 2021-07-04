package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import com.willowtreeapps.opentest4k.AssertionFailedError
import java.util.*

/**
 * Extracts an assertion against an guaranteed present value. If no value is present than
 * an the assertion fail.
 *
 * Examples:
 *
 * ```
 * val optional = Optional.of(12)
 * assertThat(optional).value().isGreaterThan(11) // Passes
 * assertThat(optional).value().isEqualTo(12)     // Passes
 *
 * val empty = Optional.empty<Int>()
 * assertThat(empty).value().isInstanceOf(Int::class) // Fails
 * assertThat(empty).value().isEqualTo(0) // Fails
 *
 * ```
 */
fun <T> Assert<Optional<T>>.value(): Assert<T> {
    return transform { actual ->
        actual.orElseThrow {
            AssertionFailedError(
                "Value should be present."
            )
        }
    }
}

/**
 * Asserts that an optional has a value.
 *
 * ```
 * var optional: Optional<Int> = Optional.of(12)
 * assertThat(optional).hasValue() // Passes
 *
 * optional = Optional.empty<Int>()
 * assertThat(optional).hasValue() // Fails
 *
 * ```
 */
fun Assert<Optional<*>>.hasValue() = given { actual ->
    if (actual.isPresent) return@given
    expected("value to be present.")
}

/**
 * Asserts that an optional is empty.
 *
 * ```
 * val empty = Optional.empty<Int>()
 * val nonEmpty = Optional.of(12)
 * assertThat(empty).isEmpty()    // Passes
 * assertThat(nonEmpty).isEmpty() // Fails
 * ```
 */
fun Assert<Optional<*>>.isEmpty() = given { actual ->
    if (actual.isEmpty) return@given
    expected("to be empty, but found ${show(actual.get())}")
}
