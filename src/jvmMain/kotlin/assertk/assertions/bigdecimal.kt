package assertk.assertions

import assertk.Assert
import java.math.BigDecimal

/**
 * Asserts that <code>actual.compareTo(BigDecimal(expected) == 0</code>.
 */
fun Assert<BigDecimal>.isEqualByComparingTo(expected: String) = isEqualByComparingTo(BigDecimal(expected))
