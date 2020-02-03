package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CollectionTest {
    //region isEmpty
    @Test fun isEmpty_empty_passes() {
        assertThat(emptyList<Any?>()).isEmpty()
    }

    @Test fun isEmpty_non_empty_fails() {
        val error = assertFails {
            assertThat(listOf<Any?>(null)).isEmpty()
        }
        assertEquals("expected to be empty but was:<[null]>", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test fun isNotEmpty_non_empty_passes() {
        assertThat(listOf<Any?>(null)).isNotEmpty()
    }

    @Test fun isNotEmpty_empty_fails() {
        val error = assertFails {
            assertThat(emptyList<Any?>()).isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region isNullOrEmpty
    @Test fun isNullOrEmpty_null_passes() {
        assertThat(null as List<Any?>?).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_empty_passes() {
        assertThat(emptyList<Any?>()).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_non_empty_fails() {
        val error = assertFails {
            assertThat(listOf<Any?>(null)).isNullOrEmpty()
        }
        assertEquals("expected to be null or empty but was:<[null]>", error.message)
    }
    //endregion

    //region hasSize
    @Test fun hasSize_correct_size_passes() {
        assertThat(emptyList<Any?>()).hasSize(0)
    }

    @Test fun hasSize_wrong_size_fails() {
        val error = assertFails {
            assertThat(emptyList<Any?>()).hasSize(1)
        }
        assertEquals("expected [size]:<[1]> but was:<[0]> ([])", error.message)
    }
    //endregion

    //region hasSameSizeAs
    @Test fun hasSameSizeAs_equal_sizes_passes() {
        assertThat(emptyList<Any?>()).hasSameSizeAs(emptyList<Any?>())
    }

    @Test fun hasSameSizeAs_non_equal_sizes_fails() {
        val error = assertFails {
            assertThat(emptyList<Any?>()).hasSameSizeAs(listOf<Any?>(null))
        }
        assertEquals("expected to have same size as:<[null]> (1) but was size:(0)", error.message)
    }
    //endregion

    //region containsNone
    @Test fun containsNone_missing_elements_passes() {
        assertThat(emptyList<Any?>()).containsNone(1)
    }

    @Test fun containsNone_present_element_fails() {
        val error = assertFails {
            assertThat(listOf(1, 2)).containsNone(2, 3)
        }
        assertEquals(
            """expected to contain none of:<[2, 3]> but was:<[1, 2]>
                | elements not expected:<[2]>
            """.trimMargin(), error.message
        )
    }
    //region

    //region containsAll
    @Test fun containsAll_all_elements_passes() {
        assertThat(listOf(1, 2)).containsAll(2, 1)
    }

    @Test fun containsAll_some_elements_fails() {
        val error = assertFails {
            assertThat(listOf(1)).containsAll(1, 2)
        }
        assertEquals(
            """expected to contain all:<[1, 2]> but was:<[1]>
                | elements not found:<[2]>
            """.trimMargin(), error.message
        )
    }
    //endregion

    //region containsOnly
    @Test fun containsOnly_only_elements_passes() {
        assertThat(listOf(1, 2)).containsOnly(2, 1)
    }

    @Test fun containsOnly_more_elements_fails() {
        val error = assertFails {
            assertThat(listOf(1, 2, 3)).containsOnly(2, 1)
        }
        assertEquals(
            """expected to contain only:<[2, 1]> but was:<[1, 2, 3]>
                | extra elements found:<[3]>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsOnly_less_elements_fails() {
        val error = assertFails {
            assertThat(listOf(1, 2, 3)).containsOnly(2, 1, 3, 4)
        }
        assertEquals(
            """expected to contain only:<[2, 1, 3, 4]> but was:<[1, 2, 3]>
                | elements not found:<[4]>
            """.trimMargin(),
            error.message
        )
    }

    @Test fun containsOnly_different_elements_fails() {
        val error = assertFails {
            assertThat(listOf(1)).containsOnly(2)
        }
        assertEquals(
            """expected to contain only:<[2]> but was:<[1]>
                | elements not found:<[2]>
                | extra elements found:<[1]>
            """.trimMargin(),
            error.message
        )
    }
    //endregion
}
