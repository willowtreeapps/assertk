package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import java.math.BigDecimal


/**
 * Asserts the BigDecimal is 0.
 * @see [isNotZero]
 */
fun Assert<BigDecimal>.isZero() {
    if (actual == BigDecimal.ZERO) return
    expected("to be zero was $actual")
}


/**
 * Asserts the number is not 0.
 * @see [isZero]
 */
fun Assert<BigDecimal>.isNotZero() {
    if (actual != BigDecimal.ZERO) return
    expected("to be non zero was $actual")
}