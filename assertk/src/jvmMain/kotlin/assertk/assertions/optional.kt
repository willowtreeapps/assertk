package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import java.util.*

/**
 * Asserts that optionals value is present
 *
 * @receiver Assert<Optional<T>>
 * @return Assert<T>
 */
fun <T> Assert<Optional<T>>.isPresent(): Assert<T> {
    return transform { optional ->
        if (!optional.isEmpty) {
            return assertThat(optional.get())
        }
        expected("optional to not be empty")
    }
}

/**
 * Asserts optionals value is not present.
 *
 * @receiver Assert<Optional<*>>
 */
fun Assert<Optional<*>>.isNotPresent() {
    given { actual ->
        if (actual.isEmpty) return
        expected("optional to empty", actual = actual.get())
    }
}

fun <T> Assert<Optional<T>>.hasValue(expected: T) = isPresent().isEqualTo(expected)