package test.assertk.assertions

import assertk.assert
import assertk.assertions.isNegative
import assertk.assertions.isNotZero
import assertk.assertions.isPositive
import assertk.assertions.isZero
import test.assertk.Assertions
import kotlin.test.Test

class NumberSpec_a_Number_On_isZero() {
    @Test
    fun it_Given_a_zero_test_should_pass() {
        assert(0).isZero()
    }

    @Test
    fun it_Given_a_not_zero_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(1).isZero()
        }.hasMessage("expected to be 0 but was:<1>")
    }
}

class NumberSpec_a_Number_On_isNotZero() {
    @Test
    fun it_Given_a_zero_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(0).isNotZero()
        }.hasMessage("expected to not be 0")
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
    fun it_Given_a_zero_or_negative_number_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(0).isPositive()
        }.hasMessage("expected to be positive but was:<0>")

        Assertions.assertThatThrownBy {
            assert(-1).isPositive()
        }.hasMessage("expected to be positive but was:<-1>")
    }
}

class NumberSpec_a_Number_On_isNegative() {
    @Test
    fun it_Given_a_zero_or_negative_number_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(0).isNegative()
        }.hasMessage("expected to be negative but was:<0>")

        Assertions.assertThatThrownBy {
            assert(1).isNegative()
        }.hasMessage("expected to be negative but was:<1>")
    }

    @Test
    fun it_Given_a_negative_number_test_should_pass() {
        assert(-1).isNegative()
    }
}
