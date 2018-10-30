package test.assertk.assertions

import assertk.assertions.isCloseTo
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ComparableJvmTest {
    @Test
    fun isCloseTo_with_delta_out_of_range_fails() {
        val error = assertFails {
            assertk.assert(10.1).isCloseTo(15.0f, 3)
        }
        assertEquals("expected <15.0f> to be close with <10.1> with delta of <3> but not", error.message)
    }

}