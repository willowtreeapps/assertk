package test.assertk.assertions

import assertk.assertions.isCloseTo
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ComparableJvmTest {
    @Test
    fun isCloseToFloat_with_delta_out_of_range_fails() {
        val error = assertFails {
            assertk.assert(10.1f).isCloseTo(15.0f, 3.5f)
        }
        assertEquals("expected <10.1f> to be close to <15.0f> with delta of <3.5f>, but was not", error.message)
    }

    @Test
    fun isCloseToDouble_with_delta_out_of_range_fails() {
        val error = assertFails {
            assertk.assert(10.1).isCloseTo(15.0, 3.5)
        }
        assertEquals("expected <10.1> to be close to <15.0> with delta of <3.5>, but was not", error.message)
    }
}