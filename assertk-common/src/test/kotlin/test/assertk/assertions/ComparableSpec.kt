package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

private val lowInt: Int = 0
private val highInt: Int = 2

class AssertSpecComparable_a_comparable_On_isGreaterThan() {

    @Test
    fun it_Checking_if_highInt_is_greater_than_lowInt_should_pass() {
        assert(highInt).isGreaterThan(lowInt)
    }

    @Test
    fun it_Checking_the_same_Type_should_fail() {
        val error = assertFails {
            assert(lowInt).isGreaterThan(lowInt)
        }
        assertEquals("expected to be greater than:<0> but was:<0>", error.message)
    }

    @Test
    fun it_Checking_if_lowInt_is_greater_than_highInt_should_fail() {
        val error = assertFails {
            assert(lowInt).isGreaterThan(highInt)
        }
        assertEquals("expected to be greater than:<2> but was:<0>", error.message)
    }
}

class AssertSpecComparable_a_comparable_On_isLessThan() {

    @Test
    fun it_Checking_if_lowInt_is_less_than_highInt_should_pass() {
        assert(lowInt).isLessThan(highInt)
    }

    @Test
    fun it_Checking_the_same_Type_should_fail() {
        val error = assertFails {
            assert(lowInt).isLessThan(lowInt)
        }
        assertEquals("expected to be less than:<0> but was:<0>", error.message)
    }

    @Test
    fun it_Checking_if_highInt_is_less_than_lowInt_should_fail() {
        val error = assertFails {
            assert(highInt).isLessThan(lowInt)
        }
        assertEquals("expected to be less than:<0> but was:<2>", error.message)
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
        val error = assertFails {
            assert(lowInt).isGreaterThanOrEqualTo(highInt)
        }
        assertEquals("expected to be greater than or equal to:<2> but was:<0>", error.message)
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
        val error = assertFails {
            assert(highInt).isLessThanOrEqualTo(lowInt)
        }
        assertEquals("expected to be less than or equal to:<0> but was:<2>", error.message)
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
        val error = assertFails {
            assert(lowInt - 1).isBetween(lowInt, highInt)
        }
        assertEquals("expected to be between:<0> and <2> but was:<-1>", error.message)
    }

    @Test
    fun it_Checking_above_the_upper_bound_should_fail() {
        val error = assertFails {
            assert(highInt + 1).isBetween(lowInt, highInt)
        }
        assertEquals("expected to be between:<0> and <2> but was:<3>", error.message)
    }
}

class AssertSpecComparable_a_comparable_On_isStrictlyBetween() {
    @Test
    fun it_Checking_inside_the_range_should_pass() {
        assert(lowInt + 1).isStrictlyBetween(lowInt, highInt)
    }

    @Test
    fun it_Checking_the_lower_bound_should_fail() {
        val error = assertFails {
            assert(lowInt).isStrictlyBetween(lowInt, highInt)
        }
        assertEquals("expected to be strictly between:<0> and <2> but was:<0>", error.message)
    }

    @Test
    fun it_Checking_the_upper_bound_should_fail() {
        val error = assertFails {
            assert(highInt).isStrictlyBetween(lowInt, highInt)
        }
        assertEquals("expected to be strictly between:<0> and <2> but was:<2>", error.message)
    }

    @Test
    fun it_Checking_below_the_lower_bound_should_fail() {
        val error = assertFails {
            assert(lowInt - 1).isStrictlyBetween(lowInt, highInt)
        }
        assertEquals("expected to be strictly between:<0> and <2> but was:<-1>", error.message)
    }

    @Test
    fun it_Checking_above_the_upper_bound_should_fail() {
        val error = assertFails {
            assert(highInt + 1).isStrictlyBetween(lowInt, highInt)
        }
        assertEquals("expected to be strictly between:<0> and <2> but was:<3>", error.message)
    }
}
