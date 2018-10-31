package test.assertk.assertions

import assertk.assert
import assertk.assertions.isCloseTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ComparableJvmTest {
    @Test
    fun isCloseToFloat_with_delta_out_of_range_fails() {
        val error = assertFails {
            assert(10.1f).isCloseTo(15.0f, 3f)
        }
        assertEquals("expected <10.1> to be close to <15> with delta of <3>, but was not", error.message)
    }

    @Test
    fun isCloseToDouble_with_delta_out_of_range_fails() {
        val error = assertFails {
            assert(10.1).isCloseTo(15.0, 3.0)
        }
        assertEquals("expected <10.1> to be close to <15> with delta of <3>, but was not", error.message)
    }

}