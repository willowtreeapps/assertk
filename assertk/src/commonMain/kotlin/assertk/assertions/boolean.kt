package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected

/**
 * Asserts the boolean is true.
 * @see [isFalse]
 */
fun Assert<Boolean>.isTrue(message: String = "to be true") = given { actual ->
    if (actual) return
    expected(message)
}

/**
 * Asserts the boolean is false.
 * @see [isTrue]
 */
fun Assert<Boolean>.isFalse(message: String = "to be false") = given { actual ->
    if (!actual) return
    expected(message)
}
