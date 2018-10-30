package test.assertk.assertions

import assertk.assert
import assertk.assertions.isCloseTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ComparableJvmTest {
    @Test
    fun isCloseTo_with_delta_out_of_range_fails() {
        val error = assertFails {
            assert(10.1).isCloseTo(15.0f, 3)
        }
        assertEquals("expected <15> to be close with <10.1> with delta of <3> but not", error.message)
    }

}