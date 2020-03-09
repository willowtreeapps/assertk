package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import assertk.assertions.support.show
import test.assertk.opentestPackageName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ArrayTest {
    //region isEqualTo
    @Test fun isEqualTo_same_contents_passes() {
        assertThat(arrayOf("one")).isEqualTo(arrayOf("one"))
    }

    @Test fun isEqualTo_different_contents_fails() {
        val error = assertFails {
            assertThat(arrayOf("one")).isEqualTo(arrayOf("two"))
        }
        assertEquals("expected:<[\"[two]\"]> but was:<[\"[one]\"]>", error.message)
    }
    //endregion

    //region isNotEqualTo
    @Test fun isNotEqualTo_different_contents_passes() {
        assertThat(arrayOf("one")).isNotEqualTo(arrayOf("two"))
    }

    @Test fun isNotEqualTo_same_contents_fails() {
        val error = assertFails {
            assertThat(arrayOf("one")).isNotEqualTo(arrayOf("one"))
        }
        assertEquals("expected to not be equal to:<[\"one\"]>", error.message)
    }
    //endregion

    //region isEmpty
    @Test fun isEmpty_empty_passes() {
        assertThat(emptyArray<Any?>()).isEmpty()
    }

    @Test fun isEmpty_non_empty_fails() {
        val error = assertFails {
            assertThat(arrayOf<Any?>(null)).isEmpty()
        }
        assertEquals("expected to be empty but was:<[null]>", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test fun isNotEmpty_non_empty_passes() {
        assertThat(arrayOf<Any?>(null)).isNotEmpty()
    }

    @Test fun isNotEmpty_empty_fails() {
        val error = assertFails {
            assertThat(arrayOf<Any?>()).isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region isNullOrEmpty
    @Test fun isNullOrEmpty_null_passes() {
        assertThat(null as Array<Any?>?).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_empty_passes() {
        assertThat(emptyArray<Any?>()).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_non_empty_fails() {
        val error = assertFails {
            assertThat(arrayOf<Any?>(null)).isNullOrEmpty()
        }
        assertEquals("expected to be null or empty but was:<[null]>", error.message)
    }
    //endregion

    //region hasSize
    @Test fun hasSize_correct_size_passes() {
        assertThat(emptyArray<Any?>()).hasSize(0)
    }

    @Test fun hasSize_wrong_size_fails() {
        val error = assertFails {
            assertThat(emptyArray<Any?>()).hasSize(1)
        }
        assertEquals("expected [size]:<[1]> but was:<[0]> ([])", error.message)
    }
    //endregion

    //region hasSameSizeAs
    @Test fun hasSameSizeAs_equal_sizes_passes() {
        assertThat(emptyArray<Any?>()).hasSameSizeAs(emptyArray<Any?>())
    }

    @Test fun hasSameSizeAs_non_equal_sizes_fails() {
        val error = assertFails {
            assertThat(emptyArray<Any?>()).hasSameSizeAs(arrayOf<Any?>(null))
        }
        assertEquals("expected to have same size as:<[null]> (1) but was size:(0)", error.message)
    }
    //endregion

    //region contains
    @Test fun contains_element_present_passes() {
        assertThat(arrayOf(1, 2)).contains(2)
    }

    @Test fun contains_element_missing_fails() {
        val error = assertFails {
            assertThat(emptyArray<Any?>()).contains(1)
        }
        assertEquals("expected to contain:<1> but was:<[]>", error.message)
    }
    //endregion

    //region doesNotContain
    @Test fun doesNotContain_element_missing_passes() {
        assertThat(emptyArray<Any?>()).doesNotContain(1)
    }

    @Test fun doesNotContain_element_present_fails() {
        val error = assertFails {
            assertThat(arrayOf(1, 2)).doesNotContain(2)
        }
        assertEquals("expected to not contain:<2> but was:<[1, 2]>", error.message)
    }
    //endregion

    //region containsNone
    @Test fun containsNone_missing_elements_passes() {
        assertThat(emptyArray<Any?>()).containsNone(1)
    }

    @Test fun containsNone_present_element_fails() {
        val error = assertFails {
            assertThat(arrayOf(1, 2)).containsNone(2, 3)
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
        assertThat(arrayOf(1, 2)).containsAll(2, 1)
    }

    @Test fun containsAll_extra_elements_passes() {
        assertThat(arrayOf(1, 2, 3)).containsAll(1, 2)
    }

    @Test fun containsAll_some_elements_fails() {
        val error = assertFails {
            assertThat(arrayOf(1)).containsAll(1, 2)
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
        assertThat(arrayOf(1, 2)).containsOnly(2, 1)
    }

    @Test fun containsOnly_more_elements_fails() {
        val error = assertFails {
            assertThat(arrayOf(1, 2, 3)).containsOnly(2, 1)
        }
        assertEquals(
            """expected to contain only:<[2, 1]> but was:<[1, 2, 3]>
                | extra elements found:<[3]>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsOnly_less_elements_fails() {
        val error = assertFails {
            assertThat(arrayOf(1, 2, 3)).containsOnly(2, 1, 3, 4)
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
            assertThat(arrayOf(1)).containsOnly(2)
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

    //region containsExactly
    @Test fun containsExactly_all_elements_in_same_order_passes() {
        assertThat(arrayOf(1, 2)).containsExactly(1, 2)
    }

    @Test fun containsExactly_all_elements_in_different_order_fails() {
        val error = assertFails {
            assertThat(arrayOf(1, 2)).containsExactly(2, 1)
        }
        assertEquals(
            """expected to contain exactly:<[2, 1]> but was:<[1, 2]>
                | at index:0 expected:<2>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_missing_element_fails() {
        val error = assertFails {
            assertThat(arrayOf(1, 2)).containsExactly(3)
        }
        assertEquals(
            """expected to contain exactly:<[3]> but was:<[1, 2]>
                | at index:0 expected:<3>
                | at index:0 unexpected:<1>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_same_indexes_are_together() {
        val error = assertFails {
            assertThat(arrayOf(1, 1)).containsExactly(2, 2)
        }
        assertEquals(
            """expected to contain exactly:<[2, 2]> but was:<[1, 1]>
                | at index:0 expected:<2>
                | at index:0 unexpected:<1>
                | at index:1 expected:<2>
                | at index:1 unexpected:<1>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_extra_element_fails() {
        val error = assertFails {
            assertThat(arrayOf(1, 2)).containsExactly(1, 2, 3)
        }
        assertEquals(
            """expected to contain exactly:<[1, 2, 3]> but was:<[1, 2]>
                | at index:2 expected:<3>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_missing_element_in_middle_fails() {
        val error = assertFails {
            assertThat(arrayOf(1, 3)).containsExactly(1, 2, 3)
        }
        assertEquals(
            """expected to contain exactly:<[1, 2, 3]> but was:<[1, 3]>
                | at index:1 expected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_extra_element_in_middle_fails() {
        val error = assertFails {
            assertThat(arrayOf(1, 2, 3)).containsExactly(1, 3)
        }
        assertEquals(
            """expected to contain exactly:<[1, 3]> but was:<[1, 2, 3]>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }
    //endregion

    //region each
    @Test fun each_empty_list_passes() {
        assertThat(emptyArray<Int>()).each { it.isEqualTo(1) }
    }

    @Test fun each_content_passes() {
        assertThat(arrayOf(1, 2)).each { it.isGreaterThan(0) }
    }

    @Test fun each_non_matching_content_fails() {
        val error = assertFails {
            assertThat(arrayOf(1, 2, 3)).each { it.isLessThan(2) }
        }
        assertEquals(
            """The following assertions failed (2 failures)
              |${"\t"}${opentestPackageName}AssertionFailedError: expected [[1]] to be less than:<2> but was:<2> ([1, 2, 3])
              |${"\t"}${opentestPackageName}AssertionFailedError: expected [[2]] to be less than:<2> but was:<3> ([1, 2, 3])
            """.trimMargin(), error.message
        )
    }
    //endregion

    //region index
    @Test fun index_successful_assertion_passes() {
        assertThat(arrayOf("one", "two"), name = "subject").index(0).isEqualTo("one")
    }

    @Test fun index_unsuccessful_assertion_fails() {
        val error = assertFails {
            assertThat(arrayOf("one", "two"), name = "subject").index(0).isEqualTo("wrong")
        }
        assertEquals(
            "expected [subject[0]]:<\"[wrong]\"> but was:<\"[one]\"> ([\"one\", \"two\"])",
            error.message
        )
    }

    @Test fun index_out_of_range_fails() {
        val error = assertFails {
            assertThat(arrayOf("one", "two"), name = "subject").index(-1).isEqualTo(listOf("one"))
        }
        assertEquals("expected [subject] index to be in range:[0-2) but was:<-1>", error.message)
    }
    //endregion

    //region extracting
    @Test fun single_extracting_function_passes() {
        assertThat(arrayOf("one", "two")).extracting { it.length }.containsExactly(3, 3)
    }

    @Test fun single_extracting_function_fails() {
        val error = assertFails {
            assertThat(arrayOf("one", "two")).extracting { it.length }.containsExactly(2, 2)
        }
        assertEquals(
            """expected to contain exactly:<[2, 2]> but was:<[3, 3]>
            | at index:0 expected:<2>
            | at index:0 unexpected:<3>
            | at index:1 expected:<2>
            | at index:1 unexpected:<3> (["one", "two"])
            """.trimMargin(), error.message
        )
    }

    @Test fun pair_extracting_function_passes() {
        assertThat(listOf(Thing("one", 1, '1'), Thing("two", 2, '2')))
            .extracting(Thing::one, Thing::two)
            .containsExactly("one" to 1, "two" to 2)
    }

    @Test fun pair_extracting_function_fails() {
        val error = assertFails {
            assertThat(arrayOf(Thing("one", 1, '1'), Thing("two", 2, '2')))
                .extracting(Thing::one, Thing::two)
                .containsExactly("one" to 2, "two" to 1)
        }
        assertEquals(
            """expected to contain exactly:<[("one", 2), ("two", 1)]> but was:<[("one", 1), ("two", 2)]>
            | at index:0 expected:<("one", 2)>
            | at index:0 unexpected:<("one", 1)>
            | at index:1 expected:<("two", 1)>
            | at index:1 unexpected:<("two", 2)> ([Thing(one=one, two=1, three=1), Thing(one=two, two=2, three=2)])
            """.trimMargin(),
            error.message
        )
    }

    @Test fun triple_extracting_function_passes() {
        assertThat(arrayOf(Thing("one", 1, '1'), Thing("two", 2, '2')))
            .extracting(Thing::one, Thing::two, Thing::three)
            .containsExactly(Triple("one", 1, '1'), Triple("two", 2, '2'))
    }

    @Test fun triple_extracting_function_fails() {
        val error = assertFails {
            assertThat(arrayOf(Thing("one", 1, '1'), Thing("two", 2, '2')))
                .extracting(Thing::one, Thing::two, Thing::three)
                .containsExactly(Triple("one", 1, '2'), Triple("two", 2, '3'))
        }
        assertEquals(
            """expected to contain exactly:<[("one", 1, '2'), ("two", 2, '3')]> but was:<[("one", 1, '1'), ("two", 2, '2')]>
            | at index:0 expected:<("one", 1, '2')>
            | at index:0 unexpected:<("one", 1, '1')>
            | at index:1 expected:<("two", 2, '3')>
            | at index:1 unexpected:<("two", 2, '2')> ([Thing(one=one, two=1, three=1), Thing(one=two, two=2, three=2)])
            """.trimMargin(),
            error.message
        )
    }
    //region extracting

    data class Thing(val one: String, val two: Int, val three: Char)
}
