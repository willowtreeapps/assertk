package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ArrayTest {
    //region isEqualTo
    @Test fun isEqualTo_same_contents_passes() {
        assert(arrayOf("one")).isEqualTo(arrayOf("one"))
    }

    @Test fun isEqualTo_different_contents_fails() {
        val error = assertFails {
            assert(arrayOf("one")).isEqualTo(arrayOf("two"))
        }
        assertEquals("expected:<[\"[two]\"]> but was:<[\"[one]\"]>", error.message)
    }
    //endregion

    //region isNotEqualTo
    @Test fun isNotEqualTo_different_contents_passes() {
        assert(arrayOf("one")).isNotEqualTo(arrayOf("two"))
    }

    @Test fun isNotEqualTo_same_contents_fails() {
        val error = assertFails {
            assert(arrayOf("one")).isNotEqualTo(arrayOf("one"))
        }
        assertEquals("expected to not be equal to:<[\"one\"]>", error.message)
    }
    //endregion

    //region isEmpty
    @Test fun isEmpty_empty_passes() {
        assert(emptyArray<Any?>()).isEmpty()
    }

    @Test fun isEmpty_non_empty_fails() {
        val error = assertFails {
            assert(arrayOf<Any?>(null)).isEmpty()
        }
        assertEquals("expected to be empty but was:<[null]>", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test fun isNotEmpty_non_empty_passes() {
        assert(arrayOf<Any?>(null)).isNotEmpty()
    }

    @Test fun isNotEmpty_empty_fails() {
        val error = assertFails {
            assert(arrayOf<Any?>()).isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region isNullOrEmpty
    @Test fun isNullOrEmpty_null_passes() {
        assert(null as Array<Any?>?).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_empty_passes() {
        assert(emptyArray<Any?>()).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_non_empty_fails() {
        val error = assertFails {
            assert(arrayOf<Any?>(null)).isNullOrEmpty()
        }
        assertEquals("expected to be null or empty but was:<[null]>", error.message)
    }
    //endregion

    //region hasSize
    @Test fun hasSize_correct_size_passes() {
        assert(emptyArray<Any?>()).hasSize(0)
    }

    @Test fun hasSize_wrong_size_fails() {
        val error = assertFails {
            assert(emptyArray<Any?>()).hasSize(1)
        }
        assertEquals("expected [size]:<[1]> but was:<[0]> ([])", error.message)
    }
    //endregion

    //region hasSameSizeAs
    @Test fun hasSameSizeAs_equal_sizes_passes() {
        assert(emptyArray<Any?>()).hasSameSizeAs(emptyArray<Any?>())
    }

    @Test fun hasSameSizeAs_non_equal_sizes_fails() {
        val error = assertFails {
            assert(emptyArray<Any?>()).hasSameSizeAs(arrayOf<Any?>(null))
        }
        assertEquals("expected to have same size as:<[null]> (1) but was size:(0)", error.message)
    }
    //endregion

    //region contains
    @Test fun contains_element_present_passes() {
        assert(arrayOf(1, 2)).contains(2)
    }

    @Test fun contains_element_missing_fails() {
        val error = assertFails {
            assert(emptyArray<Any?>()).contains(1)
        }
        assertEquals("expected to contain:<1> but was:<[]>", error.message)
    }
    //endregion

    //region doesNotContain
    @Test fun doesNotContain_element_missing_passes() {
        assert(emptyArray<Any?>()).doesNotContain(1)
    }

    @Test fun doesNotContain_element_present_fails() {
        val error = assertFails {
            assert(arrayOf(1, 2)).doesNotContain(2)
        }
        assertEquals("expected to not contain:<2> but was:<[1, 2]>", error.message)
    }
    //endregion

    //region containsNone
    @Test fun containsNone_missing_elements_passes() {
        assert(emptyArray<Any?>()).containsNone(1)
    }

    @Test fun containsNone_present_element_fails() {
        val error = assertFails {
            assert(arrayOf(1, 2)).containsNone(2, 3)
        }
        assertEquals("expected to contain none of:<[2, 3]> some elements were not expected:<[2]>", error.message)
    }
    //region

    //region containsAll
    @Test fun containsAll_all_elements_passes() {
        assert(arrayOf(1, 2)).containsAll(2, 1)
    }

    @Test fun containsAll_some_elements_fails() {
        val error = assertFails {
            assert(arrayOf(1)).containsAll(1, 2)
        }
        assertEquals("expected to contain all:<[1, 2]> some elements were not found:<[2]>", error.message)
    }
    //endregion

    //region containsExactly
    @Test fun containsExactly_all_elements_in_same_order_passes() {
        assert(arrayOf(1, 2)).containsExactly(1, 2)
    }

    @Test fun containsExactly_all_elements_in_different_order_fails() {
        val error = assertFails {
            assert(arrayOf(1, 2)).containsExactly(2, 1)
        }
        assertEquals(
            """expected to contain exactly:
                | at index:0 expected:<2>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_missing_element_fails() {
        val error = assertFails {
            assert(arrayOf(1, 2)).containsExactly(3)
        }
        assertEquals(
            """expected to contain exactly:
                | at index:0 expected:<3>
                | at index:0 unexpected:<1>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_extra_element_fails() {
        val error = assertFails {
            assert(arrayOf(1, 2)).containsExactly(1, 2, 3)
        }
        assertEquals(
            """expected to contain exactly:
                | at index:2 expected:<3>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_missing_element_in_middle_fails() {
        val error = assertFails {
            assert(arrayOf(1, 3)).containsExactly(1, 2, 3)
        }
        assertEquals(
            """expected to contain exactly:
                | at index:1 expected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_extra_element_in_middle_fails() {
        val error = assertFails {
            assert(arrayOf(1, 2, 3)).containsExactly(1, 3)
        }
        assertEquals(
            """expected to contain exactly:
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }
    //endregion

    //region each
    @Test fun each_empty_list_passes() {
        assert(emptyArray<Int>()).each { it.isEqualTo(1) }
    }

    @Test fun each_content_passes() {
        assert(arrayOf(1, 2)).each { it.isGreaterThan(0) }
    }

    @Test fun each_non_matching_content_fails() {
        val error = assertFails {
            assert(arrayOf(1, 2, 3)).each { it.isLessThan(2) }
        }
        assertEquals(
            """The following assertions failed (2 failures)
              |${"\t"}expected [[1]] to be less than:<2> but was:<2> ([1, 2, 3])
              |${"\t"}expected [[2]] to be less than:<2> but was:<3> ([1, 2, 3])
            """.trimMargin(), error.message
        )
    }
    //endregion

    //region index
    @Test fun index_successful_assertion_passes() {
        assert(arrayOf("one", "two"), name = "subject").index(0) { it.isEqualTo("one") }
    }

    @Test fun index_unsuccessful_assertion_fails() {
        val error = assertFails {
            assert(arrayOf("one", "two"), name = "subject").index(0) { it.isEqualTo("wrong") }
        }
        assertEquals(
            "expected [subject[0]]:<\"[wrong]\"> but was:<\"[one]\"> ([\"one\", \"two\"])",
            error.message
        )
    }

    @Test fun index_out_of_range_fails() {
        val error = assertFails {
            assert(arrayOf("one", "two"), name = "subject").index(-1) { it.isEqualTo(listOf("one")) }
        }
        assertEquals("expected [subject] index to be in range:[0-2) but was:<-1>", error.message)
    }
    //endregion
}
