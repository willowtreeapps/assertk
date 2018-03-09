package test.assertk.assertions

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import assertk.*
import assertk.assertions.*
import kotlin.test.assertEquals
import kotlin.test.assertFails

class IterableTest {
    @Nested inner class `contains()` {
        @Test fun `element present passes`() {
            assert(listOf(1, 2)).contains(2)
        }

        @Test fun `element missing fails`() {
            val error = assertFails {
                assert(emptyList<Any?>()).contains(1)
            }
            assertEquals("expected to contain:<1> but was:<[]>", error.message)
        }
    }

    @Nested inner class `doesNotContain()` {
        @Test fun `element missing passes`() {
            assert(emptyList<Any?>()).doesNotContain(1)
        }

        @Test fun `element present fails`() {
            val error = assertFails {
                assert(listOf(1, 2)).doesNotContain(2)
            }
            assertEquals("expected to not contain:<2> but was:<[1, 2]>", error.message)
        }
    }

    @Nested inner class `each()` {
        @Test fun `empty list passes`() {
            assert(emptyList<Int>()).each { it.isEqualTo(1) }
        }

        @Test fun `each content passes`() {
            assert(listOf(1, 2)).each { it.isGreaterThan(0) }
        }

        @Test fun `non-matching content fails`() {
            val error = assertFails {
                assert(listOf(1, 2, 3)).each { it.isLessThan(2) }
            }
            assertEquals(
                """The following 2 assertions failed:
                |- expected [[1]] to be less than:<2> but was:<2> ([1, 2, 3])
                |- expected [[2]] to be less than:<2> but was:<3> ([1, 2, 3])
            """.trimMargin(), error.message
            )
        }
    }
}