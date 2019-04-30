package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.isNegative
import assertk.assertions.isNotZero
import assertk.assertions.isPositive
import assertk.assertions.isZero
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class NumberTest {

    //region isZero
    @Test fun isZero_value_zero_passes() {
        assertThat(0).isZero()
    }

    @Test fun isZero_value_non_zero_fails() {
        val error = assertFails {
            assertThat(1).isZero()
        }
        assertEquals("expected to be 0 but was:<1>", error.message)
    }
    //endregion

    //region isNonZero
    @Test fun isNonZero_value_non_zero_passes() {
        assertThat(1).isNotZero()
    }

    @Test fun isNonZero_value_zero_fails() {
        val error = assertFails {
            assertThat(0).isNotZero()
        }
        assertEquals("expected to not be 0", error.message)
    }
    //endregion

    //region isPositive
    @Test fun isPositive_value_positive_passes() {
        assertThat(1).isPositive()
    }

    @Test fun isPositive_value_zero_fails() {
        val error = assertFails {
            assertThat(0).isPositive()
        }
        assertEquals("expected to be positive but was:<0>", error.message)
    }

    @Test fun isPositive_value_negative_fails() {
        val error = assertFails {
            assertThat(-1).isPositive()
        }
        assertEquals("expected to be positive but was:<-1>", error.message)
    }
    //endregion

    //region isNegative
    @Test fun isNegative_value_negative_passes() {
        assertThat(-1).isNegative()
    }

    @Test fun isNegative_value_zero_fails() {
        val error = assertFails {
            assertThat(0).isNegative()
        }
        assertEquals("expected to be negative but was:<0>", error.message)
    }

    @Test fun isNegative_value_positive_fails() {
        val error = assertFails {
            assertThat(1).isNegative()
        }
        assertEquals("expected to be negative but was:<1>", error.message)
    }
    //endregion
}
