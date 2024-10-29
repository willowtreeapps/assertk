package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected

/**
 * Asserts the boolean is true.
 * @see [isFalse]
 */
fun Assert<Boolean?>.isTrue() = given { actual ->
    if (actual == true) return
    expected("to be true but was $actual")
}

/**
 * Asserts the boolean is false.
 * @see [isTrue]
 */
fun Assert<Boolean?>.isFalse() = given { actual ->
    if (actual == false) return
    expected("to be false but was $actual")
}
