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
            assertEquals(
                """expected to contain exactly:
                | at index:0 expected:<2>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
            )
        }

        @Test fun `missing element fails`() {
            val error = assertFails {
                assert(listOf(1, 2)).containsExactly(3)
            }
            assertEquals(
                """expected to contain exactly:
                | at index:0 expected:<3>
                | at index:0 unexpected:<1>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
            )
        }

        @Test fun `extra element fails`() {
            val error = assertFails {
                assert(listOf(1, 2)).containsExactly(1, 2, 3)
            }
            assertEquals(
                """expected to contain exactly:
                | at index:2 expected:<3>
            """.trimMargin(), error.message
            )
        }

        @Test fun `missing element in middle fails`() {
            val error = assertFails {
                assert(listOf(1, 3)).containsExactly(1, 2, 3)
            }
            assertEquals(
                """expected to contain exactly:
                | at index:1 expected:<2>
            """.trimMargin(), error.message
            )
        }

        @Test fun `extra element in middle fails`() {
            val error = assertFails {
                assert(listOf(1, 2, 3)).containsExactly(1, 3)
            }
            assertEquals(
                """expected to contain exactly:
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
            )
        }
    }
}