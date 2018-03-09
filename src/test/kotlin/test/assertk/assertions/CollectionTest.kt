package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CollectionTest {
    @Nested inner class `isEmpty()` {
        @Test fun `empty passes`() {
            assert(emptyList<Any?>()).isEmpty()
        }

        @Test fun `non-empty fails`() {
            val error = assertFails {
                assert(listOf(null)).isEmpty()
            }
            assertEquals("expected to be empty but was:<[null]>", error.message)
        }
    }

    @Nested inner class `isNotEmpty()` {
        @Test fun `non-empty passes`() {
            assert(listOf(null)).isNotEmpty()
        }

        @Test fun `empty fails`() {
            val error = assertFails {
                assert(emptyList<Any?>()).isNotEmpty()
            }
            assertEquals("expected to not be empty", error.message)
        }
    }

    @Nested inner class `isNullOrEmpty()` {
        @Test fun `null passes`() {
            assert(null as List<Any?>?).isNullOrEmpty()
        }

        @Test fun `empty passes`() {
            assert(emptyList<Any?>()).isNullOrEmpty()
        }

        @Test fun `non-empty fails`() {
            val error = assertFails {
                assert(listOf(null)).isNullOrEmpty()
            }
            assertEquals("expected to be null or empty but was:<[null]>", error.message)
        }
    }

    @Nested inner class `hasSize()` {
        @Test fun `correct size passes`() {
            assert(emptyList<Any?>()).hasSize(0)
        }

        @Test fun `wrong size fails`() {
            val error = assertFails {
                assert(emptyList<Any?>()).hasSize(1)
            }
            assertEquals("expected [size]:<[1]> but was:<[0]> ([])", error.message)
        }
    }

    @Nested inner class `hasSameSizeAs()` {
        @Test fun `equal sizes passes`() {
            assert(emptyList<Any?>()).hasSameSizeAs(emptyList<Any?>())
        }

        @Test fun `non-equal sizes fails`() {
            val error = assertFails {
                assert(emptyList<Any?>()).hasSameSizeAs(listOf(null))
            }
            assertEquals("expected to have same size as:<[null]> (1) but was size:(0)", error.message)
        }
    }

    @Nested inner class `containsNone()` {
        @Test fun `missing elements passes`() {
            assert(emptyList<Any?>()).containsNone(1)
        }

        @Test fun `present element fails`() {
            val error = assertFails {
                assert(listOf(1, 2)).containsNone(2, 3)
            }
            assertEquals("expected to contain none of:<[2, 3]> but was:<[1, 2]>", error.message)
        }
    }

    @Nested inner class `containsAll()` {
        @Test fun `all elements passes`() {
            assert(listOf(1, 2)).containsAll(2, 1)
        }

        @Test fun `some elements fails`() {
            val error = assertFails {
                assert(listOf(1)).containsAll(1, 2)
            }
            assertEquals("expected to contain all:<[1, 2]> but was:<[1]>", error.message)
        }
    }
}
