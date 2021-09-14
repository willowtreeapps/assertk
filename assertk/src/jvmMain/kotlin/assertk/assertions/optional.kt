package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import java.util.*

/**
 * Asserts that optional's value is present
 * @see isEmpty
 */
fun <T> Assert<Optional<T>>.isPresent(): Assert<T> {
    return transform { optional ->
        if (optional.isPresent) {
            return assertThat(optional.get())
        }
        expected("optional to not be empty")
    }
}

/**
 * Asserts optional's value is not present.
 * @see isPresent
 */
fun Assert<Optional<*>>.isEmpty() {
    given { actual ->
        if (!actual.isPresent) return
        expected("optional to be empty but was:${show(actual.get())}")
    }
}

/**
 * Asserts the optional has the expected value.
 */
fun <T> Assert<Optional<T>>.hasValue(expected: T) = isPresent().isEqualTo(expected)