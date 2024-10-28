package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.fail

/**
 * Asserts the boolean is true.
 * @see [isFalse]
 */
fun Assert<Boolean?>.isTrue() = given { actual ->
    if (actual == true) return
    fail(true, actual)
}

/**
 * Asserts the boolean is false.
 * @see [isTrue]
 */
fun Assert<Boolean?>.isFalse() = given { actual ->
    if (actual == false) return
    fail(false, actual)
}
