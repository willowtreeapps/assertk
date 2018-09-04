package test.assertk.assertions.support

import assertk.assertions.isNegative
import assertk.assertions.isPositive
import assertk.assertions.isZero
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.assertEquals
import kotlin.test.assertFails


class BigDecimalSpecNumberOnIsZero {

    @Test
    fun itGivenZeroTestShouldPass() {
        assertk.assert(BigDecimal.ZERO).isZero()
    }

    @Test
    fun itGivenNoZeroTestShouldFail() {
        val error = assertFails {
            assertk.assert(BigDecimal.ONE).isZero()
        }
        assertEquals("expected to be 0 but was:<1>", error.message)
    }
}

class BigIntegerSpecNumberOnisZero {

    @Test
    fun itGivenZeroTestShouldPass() {
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

class BigDecimalSpecNumberOnIsPositive {
    @Test
    fun itGivenPositiveNumberTestShouldPass() {
        assertk.assert(BigDecimal.ONE).isPositive()
    }

    @Test
    fun itGivenZeroNumberTestShouldFail() {
        val error = assertFails {
            assertk.assert(BigDecimal.ZERO).isPositive()
        }
        assertEquals("expected to be positive but was:<0>", error.message)
    }

    @Test
    fun itGivenNegativeNumberTestShouldFail() {
        val error = assertFails {
            assertk.assert(BigDecimal.ONE.negate()).isPositive()
        }
        assertEquals("expected to be positive but was:<-1>", error.message)
    }
}

class NumberSpecNumberOnIsNegative {
    @Test
    fun itGivenZeroNumberTestShouldFail() {
        val error = assertFails {
            assertk.assert(BigDecimal.ZERO).isNegative()
        }
        assertEquals("expected to be negative but was:<0>", error.message)
    }

    @Test
    fun itGivePositiveNumberTestShouldFail() {
        val error = assertFails {
            assertk.assert(BigDecimal.ONE).isNegative()
        }
        assertEquals("expected to be negative but was:<1>", error.message)
    }

    @Test
    fun itGivenNegativeNumberTestShouldPass() {
        assertk.assert(BigDecimal.ONE.negate()).isNegative()
    }
}