package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import java.math.BigInteger

/**
 * Asserts the number is 0.
 * @see [isNotZero]
 */
fun Assert<BigInteger>.isZero() {
    if (actual == BigInteger.ZERO) return
    expected("to be 0 but was:${show(actual)}")
}

/**
 * Asserts the number is not 0.
 * @see [isZero]
 */
fun Assert<BigInteger>.isNotZero() {
    if (actual != BigInteger.ZERO) return
    expected("to not be 0")
}