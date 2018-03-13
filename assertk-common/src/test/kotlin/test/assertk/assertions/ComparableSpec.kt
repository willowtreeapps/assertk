package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import kotlin.test.Test
import test.assertk.Assertions

private val lowInt: Int = 0
private val highInt: Int = 2

class AssertSpecComparable_a_comparable_On_isGreaterThan() {

    @Test
    fun it_Checking_if_highInt_is_greater_than_lowInt_should_pass() {
        assert(highInt).isGreaterThan(lowInt)
    }

    @Test
    fun it_Checking_the_same_Type_should_fail() {
        Assertions.assertThatThrownBy {
            assert(lowInt).isGreaterThan(lowInt)
        }.hasMessage("expected to be greater than:<0> but was:<0>")
    }

    @Test
    fun it_Checking_if_lowInt_is_greater_than_highInt_should_fail() {
        Assertions.assertThatThrownBy {
            assert(lowInt).isGreaterThan(highInt)
        }.hasMessage("expected to be greater than:<2> but was:<0>")
    }
}

class AssertSpecComparable_a_comparable_On_isLessThan() {

    @Test
    fun it_Checking_if_lowInt_is_less_than_highInt_should_pass() {
        assert(lowInt).isLessThan(highInt)
    }

    @Test
    fun it_Checking_the_same_Type_should_fail() {
        Assertions.assertThatThrownBy {
            assert(lowInt).isLessThan(lowInt)
        }.hasMessage("expected to be less than:<0> but was:<0>")
    }

    @Test
    fun it_Checking_if_highInt_is_less_than_lowInt_should_fail() {
        Assertions.assertThatThrownBy {
            assert(highInt).isLessThan(lowInt)
        }.hasMessage("expected to be less than:<0> but was:<2>")
    }
}

class AssertSpecComparable_a_comparable_On_isGreaterThanOrEqualTo() {

    @Test
    fun it_Checking_if_highInt_is_greater_or_equal_to_lowInt_should_pass() {
        assert(highInt).isGreaterThanOrEqualTo(lowInt)
    }

    @Test
    fun it_Checking_the_same_Type_should_pass() {
        assert(lowInt).isGreaterThanOrEqualTo(lowInt)
    }

    @Test
    fun it_Checking_if_lowInt_is_greater_than_or_equal_to_highInt_should_fail() {
        Assertions.assertThatThrownBy {
            assert(lowInt).isGreaterThanOrEqualTo(highInt)
        }.hasMessage("expected to be greater than or equal to:<2> but was:<0>")
    }
}

class AssertSpecComparable_a_comparable_On_isLessThanOrEqualTo() {

    @Test
    fun it_Checking_if_lowInt_is_less_than_or_equal_to_highInt_should_pass() {
        assert(lowInt).isLessThanOrEqualTo(highInt)
    }

    @Test
    fun it_Checking_the_same_Type_should_pass() {
        assert(lowInt).isLessThanOrEqualTo(lowInt)
    }

    @Test
    fun it_Checking_if_highInt_is_less_than_or_equal_to_lowInt_should_fail() {
        Assertions.assertThatThrownBy {
            assert(highInt).isLessThanOrEqualTo(lowInt)
        }.hasMessage("expected to be less than or equal to:<0> but was:<2>")
    }
}

class AssertSpecComparable_a_comparable_On_isBetween() {
    @Test
    fun it_Checking_inside_the_range_should_pass() {
        assert(lowInt + 1).isBetween(lowInt, highInt)
    }

    @Test
    fun it_Checking_the_lower_bound_should_pass() {
        assert(lowInt).isBetween(lowInt, highInt)
    }

    @Test
    fun it_Checking_the_upper_bound_should_pass() {
        assert(highInt).isBetween(lowInt, highInt)
    }

    @Test
    fun it_Checking_below_the_lower_bound_should_fail() {
        Assertions.assertThatThrownBy {
            assert(lowInt - 1).isBetween(lowInt, highInt)
        }.hasMessage("expected to be between:<0> and <2> but was:<-1>")
    }

    @Test
    fun it_Checking_above_the_upper_bound_should_fail() {
        Assertions.assertThatThrownBy {
            assert(highInt + 1).isBetween(lowInt, highInt)
        }.hasMessage("expected to be between:<0> and <2> but was:<3>")
    }
}

class AssertSpecComparable_a_comparable_On_isStrictlyBetween() {
    @Test
    fun it_Checking_inside_the_range_should_pass() {
        assert(lowInt + 1).isStrictlyBetween(lowInt, highInt)
    }

    @Test
    fun it_Checking_the_lower_bound_should_fail() {
        Assertions.assertThatThrownBy {
            assert(lowInt).isStrictlyBetween(lowInt, highInt)
        }.hasMessage("expected to be strictly between:<0> and <2> but was:<0>")
    }

    @Test
    fun it_Checking_the_upper_bound_should_fail() {
        Assertions.assertThatThrownBy {
            assert(highInt).isStrictlyBetween(lowInt, highInt)
        }.hasMessage("expected to be strictly between:<0> and <2> but was:<2>")
    }

    @Test
    fun it_Checking_below_the_lower_bound_should_fail() {
        Assertions.assertThatThrownBy {
            assert(lowInt - 1).isStrictlyBetween(lowInt, highInt)
        }.hasMessage("expected to be strictly between:<0> and <2> but was:<-1>")
    }

    @Test
    fun it_Checking_above_the_upper_bound_should_fail() {
        Assertions.assertThatThrownBy {
            assert(highInt + 1).isStrictlyBetween(lowInt, highInt)
        }.hasMessage("expected to be strictly between:<0> and <2> but was:<3>")
    }
}
