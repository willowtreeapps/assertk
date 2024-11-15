package test.assertk.assertions.support

import assertk.assertThat
import assertk.assertions.isNegative
import assertk.assertions.isPositive
import assertk.assertions.isZero
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BigDecimalSpecNumberOnIsZero {

    @Test
    fun itGivenZeroTestShouldPass() {
        assertThat(BigDecimal.ZERO).isZero()
    }

    @Test
    fun itGivenNoZeroTestShouldFail() {
        val error = assertFailsWith<AssertionError> {
            assertThat(BigDecimal.ONE).isZero()
        }
        assertEquals("expected to be 0 but was:<1>", error.message)
    }
}

class BigIntegerSpecNumberOnIsZero {

    @Test
    fun itGivenZeroTestShouldPass() {
        assertThat(BigInteger.ZERO).isZero()
    }

    @Test
    fun it_Given_a_not_zero_test_should_fail() {
        val error = assertFailsWith<AssertionError> {
            assertThat(BigInteger.ONE).isZero()
        }
        assertEquals("expected to be 0 but was:<1>", error.message)
    }
}

class BigDecimalSpecNumberOnIsPositive {
    @Test
    fun itGivenPositiveNumberTestShouldPass() {
        assertThat(BigDecimal.ONE).isPositive()
    }

    @Test
    fun itGivenZeroNumberTestShouldFail() {
        val error = assertFailsWith<AssertionError> {
            assertThat(BigDecimal.ZERO).isPositive()
        }
        assertEquals("expected to be positive but was:<0>", error.message)
    }

    @Test
    fun itGivenNegativeNumberTestShouldFail() {
        val error = assertFailsWith<AssertionError> {
            assertThat(BigDecimal.ONE.negate()).isPositive()
        }
        assertEquals("expected to be positive but was:<-1>", error.message)
    }
}

class NumberSpecNumberOnIsNegative {
    @Test
    fun itGivenZeroNumberTestShouldFail() {
        val error = assertFailsWith<AssertionError> {
            assertThat(BigDecimal.ZERO).isNegative()
        }
        assertEquals("expected to be negative but was:<0>", error.message)
    }

    @Test
    fun itGivePositiveNumberTestShouldFail() {
        val error = assertFailsWith<AssertionError> {
            assertThat(BigDecimal.ONE).isNegative()
        }
        assertEquals("expected to be negative but was:<1>", error.message)
    }

    @Test
    fun itGivenNegativeNumberTestShouldPass() {
        assertThat(BigDecimal.ONE.negate()).isNegative()
    }
}
