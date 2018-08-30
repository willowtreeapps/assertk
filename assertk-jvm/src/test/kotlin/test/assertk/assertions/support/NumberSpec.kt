package test.assertk.assertions.support

import assertk.assertions.isNegative
import assertk.assertions.isPositive
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

class BigDecimalSpec_a_Number_On_isPositive() {
    @Test
    fun it_Given_a_positive_number_test_should_pass() {
        assertk.assert(BigDecimal.ONE).isPositive()
    }

    @Test
    fun it_Given_a_zero_number_test_should_fail() {
        val error = assertFails {
            assertk.assert(BigDecimal.ZERO).isPositive()
        }
        assertEquals("expected to be positive but was:<0>", error.message)
    }

    @Test
    fun it_Given_a_negative_number_test_should_fail() {
        val error = assertFails {
            assertk.assert(BigDecimal.ONE.negate()).isPositive()
        }
        assertEquals("expected to be positive but was:<-1>", error.message)
    }
}

class NumberSpec_a_Number_On_isNegative() {
    @Test
    fun it_Given_a_zero_number_test_should_fail() {
        val error = assertFails {
            assertk.assert(BigDecimal.ZERO).isNegative()
        }
        assertEquals("expected to be negative but was:<0>", error.message)
    }

    @Test
    fun it_Give_a_positive_number_test_should_fail() {
        val error = assertFails {
            assertk.assert(BigDecimal.ONE).isNegative()
        }
        assertEquals("expected to be negative but was:<1>", error.message)
    }

    @Test
    fun it_Given_a_negative_number_test_should_pass() {
        assertk.assert(BigDecimal.ONE.negate()).isNegative()
    }
}