package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected

/**
 * Asserts the boolean is true.
 * @see [isFalse]
 */
fun Assert<Boolean>.isTrue() = given { actual ->
    if (actual) return
    expected("to be true")
}

/**
 * Asserts the boolean is false.
 * @see [isTrue]
 */
fun Assert<Boolean>.isFalse() = given { actual ->
    if (!actual) return
    expected("to be false")
}
