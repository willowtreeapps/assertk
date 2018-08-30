package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Asserts the number is 0.
 * @see [isNotZero]
 */
fun <T : Number> Assert<T>.isZero() {
    if (actual.toDouble() == 0.0) return
    expected("to be 0 but was:${show(actual)}")
}

/**
 * Asserts the number is not 0.
 * @see [isZero]
 */
fun <T : Number> Assert<T>.isNotZero() {
    if (actual.toDouble() != 0.0) return
    expected("to not be 0")
}

/**
 * Asserts the number is greater than 0.
 * @see [isNegative]
 */
fun <T : Number> Assert<T>.isPositive() {
    if (actual.toDouble() > 0) return
    expected("to be positive but was:${show(actual)}")
}

/**
 * Asserts the number is less than 0.
 * @see [isPositive]
 */
fun <T : Number> Assert<T>.isNegative() {
    if (actual.toDouble() < 0) return
    expected("to be negative but was:${show(actual)}")
}
