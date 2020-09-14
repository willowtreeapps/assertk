package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show

/**
 * Asserts the string is equal to the expected string.
 * @param ignoreCase true to compare ignoring case, the default if false.
 * @see [isNotEqualTo]
 */
fun Assert<String?>.isEqualTo(other: String?, ignoreCase: Boolean = false) = given { actual ->
    if (actual.equals(other, ignoreCase)) return
    fail(other, actual)
}

/**
 * Asserts the string is not equal to the expected string.
 * @param ignoreCase true to compare ignoring case, the default if false.
 * @see [isEqualTo]
 */
fun Assert<String?>.isNotEqualTo(other: String?, ignoreCase: Boolean = false) = given { actual ->
    if (!actual.equals(other, ignoreCase)) return
    if (ignoreCase) {
        expected(":${show(other)} not to be equal to (ignoring case):${show(actual)}")
    } else {
        expected("to not be equal to:${show(actual)}")
    }
}