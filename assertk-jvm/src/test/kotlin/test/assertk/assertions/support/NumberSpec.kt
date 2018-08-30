package test.assertk.assertions.support

import assertk.assertions.isZero
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.assertEquals
import kotlin.test.assertFails


class BigDecimalSpec_a_Number_On_isZero() {

    @Test
    fun it_Given_a_zero_test_should_pass() {
        assertk.assert(BigDecimal.ZERO).isZero()
    }

    @Test
    fun it_Given_a_not_zero_test_should_fail() {
        val error = assertFails {
            assertk.assert(BigDecimal.ONE).isZero()
        }
        assertEquals("expected to be 0 but was:<1>", error.message)
    }
}

class BigIntegerSpec_a_Number_On_isZero() {

    @Test
    fun it_Given_a_zero_test_should_pass() {
        assertk.assert(BigInteger.ZERO).isZero()
    }

    @Test
    fun it_Given_a_not_zero_test_should_fail() {
        val error = assertFails {
            assertk.assert(BigInteger.ONE).isZero()
        }
        assertEquals("expected to be 0 but was:<1>", error.message)
    }
}