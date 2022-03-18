package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.isEqualByComparingTo
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BigDecimalTest {
    @Test
    fun isEqualByComparingTo_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(BigDecimal(20)).isEqualByComparingTo(BigDecimal(21))
        }
        assertEquals("expected:<2[1]> but was:<2[0]>", error.message)
    }

    @Test
    fun isEqualByComparingTo_succeeds() {
        assertThat(BigDecimal(20)).isEqualByComparingTo(BigDecimal(20.00))
    }

    @Test
    fun isEqualByComparingTo_succeeds_string() {
        assertThat(BigDecimal(20)).isEqualByComparingTo("20.00")
    }
}
