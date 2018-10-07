package test.assertk.assertions

import assertk.assert
import assertk.assertions.isNotZero
import assertk.assertions.isZero
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFails

class BigDecimalTest {
    @Test
    fun bigdecimal_zero_isZero() {
        assert(BigDecimal(0.00)).isZero()
    }

    @Test
    fun bigdecimal_fails_isZero() {
        val error = assertFails {
            assert(BigDecimal(1.00)).isZero()
        }

        assertEquals("expected to be zero was 1", error.message)
    }

    @Test
    fun bigdecimal_zero_isNotZero() {
        assert(BigDecimal(1.00)).isNotZero()
    }

    @Test
    fun bigdecimal_fails_isNotZero() {
        val error = assertFails {
            assert(BigDecimal(0.00)).isNotZero()
        }
        assertEquals("expected to be non zero was 0", error.message)
    }
}