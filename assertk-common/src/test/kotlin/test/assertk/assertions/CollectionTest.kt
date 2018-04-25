package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CollectionTest {
    //region isEmpty
    @Test fun isEmpty_empty_passes() {
        assert(emptyList<Any?>()).isEmpty()
    }

    @Test fun isEmpty_non_empty_fails() {
        val error = assertFails {
            assert(listOf<Any?>(null)).isEmpty()
        }
        assertEquals("expected to be empty but was:<[null]>", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test fun isNotEmpty_non_empty_passes() {
        assert(listOf<Any?>(null)).isNotEmpty()
    }

    @Test fun isNotEmpty_empty_fails() {
        val error = assertFails {
            assert(emptyList<Any?>()).isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region isNullOrEmpty
    @Test fun isNullOrEmpty_null_passes() {
        assert(null as List<Any?>?).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_empty_passes() {
        assert(emptyList<Any?>()).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_non_empty_fails() {
        val error = assertFails {
            assert(listOf<Any?>(null)).isNullOrEmpty()
        }
        assertEquals("expected to be null or empty but was:<[null]>", error.message)
    }
    //endregion

    //region hasSize
    @Test fun hasSize_correct_size_passes() {
        assert(emptyList<Any?>()).hasSize(0)
    }

    @Test fun hasSize_wrong_size_fails() {
        val error = assertFails {
            assert(emptyList<Any?>()).hasSize(1)
        }
        assertEquals("expected [size]:<[1]> but was:<[0]> ([])", error.message)
    }
    //endregion

    //region hasSameSizeAs
    @Test fun hasSameSizeAs_equal_sizes_passes() {
        assert(emptyList<Any?>()).hasSameSizeAs(emptyList<Any?>())
    }

    @Test fun hasSameSizeAs_non_equal_sizes_fails() {
        val error = assertFails {
            assert(emptyList<Any?>()).hasSameSizeAs(listOf<Any?>(null))
        }
        assertEquals("expected to have same size as:<[null]> (1) but was size:(0)", error.message)
    }
    //endregion

    //region containsNone
    @Test fun containsNone_missing_elements_passes() {
        assert(emptyList<Any?>()).containsNone(1)
    }

    @Test fun containsNone_present_element_fails() {
        val error = assertFails {
            assert(listOf(1, 2)).containsNone(2, 3)
        }
        assertEquals("expected to contain none of:<[2, 3]> some elements were not expected:<[2]>", error.message)
    }
    //region

    //region containsAll
    @Test fun containsAll_all_elements_passes() {
        assert(listOf(1, 2)).containsAll(2, 1)
    }

    @Test fun containsAll_some_elements_fails() {
        val error = assertFails {
            assert(listOf(1)).containsAll(1, 2)
        }
        assertEquals("expected to contain all:<[1, 2]> some elements were not found:<[2]>", error.message)
    }
    //endregion
}
