package test.assertk.assertions

import assertk.assert
import assertk.assertions.isNegative
import assertk.assertions.isNotZero
import assertk.assertions.isPositive
import assertk.assertions.isZero
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class NumberSpec_a_Number_On_isZero() {
    @Test
    fun it_Given_a_zero_test_should_pass() {
        assert(0).isZero()
    }

    @Test
    fun it_Given_a_not_zero_test_should_fail() {
        val error = assertFails {
            assert(1).isZero()
        }
        assertEquals("expected to be 0 but was:<1>", error.message)
    }
}

class NumberSpec_a_Number_On_isNotZero() {
    @Test
    fun it_Given_a_zero_test_should_fail() {
        val error = assertFails {
            assert(0).isNotZero()
        }
        assertEquals("expected to not be 0", error.message)
    }

    @Test
    fun it_Given_a_not_zero_test_should_pass() {
        assert(1).isNotZero()
    }
}

class NumberSpec_a_Number_On_isPositive() {
    @Test
    fun it_Given_a_positive_number_test_should_pass() {
        assert(1).isPositive()
    }

    @Test
    fun it_Given_a_zero_number_test_should_fail() {
        val error = assertFails {
            assert(0).isPositive()
        }
        assertEquals("expected to be positive but was:<0>", error.message)
    }

    @Test
    fun it_Given_a_negative_number_test_should_fail() {
        val error = assertFails {
            assert(-1).isPositive()
        }
        assertEquals("expected to be positive but was:<-1>", error.message)
    }
}

class NumberSpec_a_Number_On_isNegative() {
    @Test
    fun it_Given_a_zero_number_test_should_fail() {
        val error = assertFails {
            assert(0).isNegative()
        }
        assertEquals("expected to be negative but was:<0>", error.message)
    }

    @Test
    fun it_Give_a_positive_number_test_should_fail() {
        val error = assertFails {
            assert(1).isNegative()
        }
        assertEquals("expected to be negative but was:<1>", error.message)
    }

    @Test
    fun it_Given_a_negative_number_test_should_pass() {
        assert(-1).isNegative()
    }
}
