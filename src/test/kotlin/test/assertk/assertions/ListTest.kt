package test.assertk.assertions

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import assertk.*
import assertk.assertions.containsExactly
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ListTest {
    @Nested inner class `containsExactly()` {
        @Test fun `all elements in same order passes`() {
            assert(listOf(1, 2)).containsExactly(1, 2)
        }

        @Test fun `all elements in different order fails`() {
            val error = assertFails {
                assert(listOf(1, 2)).containsExactly(2, 1)
            }
            assertEquals("expected to contain exactly:<[2, 1]> but was:<[1, 2]>", error.message)
        }

        @Test fun `missing element fails`() {
            val error = assertFails {
                assert(listOf(1, 2)).containsExactly(3)
            }
            assertEquals("expected to contain exactly:<[3]> but was:<[1, 2]>", error.message)
        }
    }
}