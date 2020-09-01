package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import test.assertk.opentestPackageName
import assertk.assertions.support.show
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

$T:$N:$E = ByteArray:byteArray:Byte, IntArray:intArray:Int, ShortArray:shortArray:Short, LongArray:longArray:Long, CharArray:charArray:Char

class $TContainsTest {
    //region contains
    @Test fun contains_element_present_passes() {
        assertThat($NOf(1.to$E(), 2.to$E())).contains(2.to$E())
    }

    @Test fun contains_element_missing_fails() {
        val error = assertFails {
            assertThat($NOf()).contains(1.to$E())
        }
        assertEquals("expected to contain:<${show(1.to$E(), "")}> but was:<[]>", error.message)
    }
    //endregion

    //region doesNotContain
    @Test fun doesNotContain_element_missing_passes() {
        assertThat($NOf()).doesNotContain(1.to$E())
    }

    @Test fun doesNotContain_element_present_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E())).doesNotContain(2.to$E())
        }
        assertEquals("expected to not contain:<${show(2.to$E(), "")}> but was:<[${show(1.to$E(), "")}, ${show(2.to$E(), "")}]>", error.message)
    }
    //endregion

    //region containsNone
    @Test fun containsNone_missing_elements_passes() {
        assertThat($NOf()).containsNone(1.to$E())
    }

    @Test fun containsNone_present_element_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E())).containsNone(2.to$E(), 3.to$E())
        }
        assertEquals(
            """expected to contain none of:<[${show(2.to$E(), "")}, ${show(3.to$E(), "")}]> but was:<[${show(1.to$E(), "")}, ${show(2.to$E(), "")}]>
                | elements not expected:<[${show(2.to$E(), "")}]>
            """.trimMargin(), error.message
        )
    }
    //region

    //region containsAll
    @Test fun containsAll_all_elements_passes() {
        assertThat($NOf(1.to$E(), 2.to$E())).containsAll(2.to$E(), 1.to$E())
    }

    @Test fun containsAll_some_elements_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E())).containsAll(1.to$E(), 2.to$E())
        }
        assertEquals(
            """expected to contain all:<[${show(1.to$E(), "")}, ${show(2.to$E(), "")}]> but was:<[${show(1.to$E(), "")}]>
                | elements not found:<[${show(2.to$E(), "")}]>
            """.trimMargin(), error.message
        )
    }
    //endregion

    //region containsOnly
    @Test fun containsOnly_only_elements_passes() {
        assertThat($NOf(1.to$E(), 2.to$E())).containsOnly(2.to$E(), 1.to$E())
    }

    @Test fun containsOnly_duplicate_elements_passes() {
        assertThat($NOf(1.to$E(), 2.to$E(), 2.to$E())).containsOnly(2.to$E(), 1.to$E())
    }

    @Test fun containsOnly_duplicate_elements_passes2() {
        assertThat($NOf(1.to$E(), 2.to$E())).containsOnly(2.to$E(), 2.to$E(), 1.to$E())
    }

    @Test fun containsOnly_more_elements_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E(), 3.to$E())).containsOnly(2.to$E(), 1.to$E())
        }
        assertEquals(
            """expected to contain only:<[${show(2.to$E(), "")}, ${show(1.to$E(), "")}]> but was:<[${show(1.to$E(), "")}, ${show(2.to$E(), "")}, ${show(3.to$E(), "")}]>
                | extra elements found:<[${show(3.to$E(), "")}]>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsOnly_less_elements_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E(), 3.to$E())).containsOnly(2.to$E(), 1.to$E(), 3.to$E(), 4.to$E())
        }
        assertEquals(
            """expected to contain only:<[${show(2.to$E(), "")}, ${show(1.to$E(), "")}, ${show(3.to$E(), "")}, ${show(4.to$E(), "")}]> but was:<[${show(1.to$E(), "")}, ${show(2.to$E(), "")}, ${show(3.to$E(), "")}]>
                | elements not found:<[${show(4.to$E(), "")}]>
            """.trimMargin(),
            error.message
        )
    }

    @Test fun containsOnly_different_elements_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E())).containsOnly(2.to$E())
        }
        assertEquals(
            """expected to contain only:<[${show(2.to$E(), "")}]> but was:<[${show(1.to$E(), "")}]>
                | elements not found:<[${show(2.to$E(), "")}]>
                | extra elements found:<[${show(1.to$E(), "")}]>
            """.trimMargin(),
            error.message
        )
    }
    //endregion

    //region containsExactly
    @Test fun containsExactly_all_elements_in_same_order_passes() {
        assertThat($NOf(1.to$E(), 2.to$E())).containsExactly(1.to$E(), 2.to$E())
    }

    @Test fun containsExactly_all_elements_in_different_order_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E())).containsExactly(2.to$E(), 1.to$E())
        }
        assertEquals(
            """expected to contain exactly:<${show(listOf(2.to$E(), 1.to$E()), "")}> but was:<${show(listOf(1.to$E(), 2.to$E()), "")}>
                | at index:0 expected:<${show(2.to$E(), "")}>
                | at index:1 unexpected:<${show(2.to$E(), "")}>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_missing_element_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E())).containsExactly(3.to$E())
        }
        assertEquals(
            """expected to contain exactly:<${show(listOf(3.to$E()), "")}> but was:<${show(listOf(1.to$E(), 2.to$E()), "")}>
                | at index:0 expected:<${show(3.to$E(), "")}>
                | at index:0 unexpected:<${show(1.to$E(), "")}>
                | at index:1 unexpected:<${show(2.to$E(), "")}>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_same_indexes_are_together() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 1.to$E())).containsExactly(2.to$E(), 2.to$E())
        }
        assertEquals(
            """expected to contain exactly:<${show(listOf(2.to$E(), 2.to$E()), "")}> but was:<${show(listOf(1.to$E(), 1.to$E()), "")}>
                | at index:0 expected:<${show(2.to$E(), "")}>
                | at index:0 unexpected:<${show(1.to$E(), "")}>
                | at index:1 expected:<${show(2.to$E(), "")}>
                | at index:1 unexpected:<${show(1.to$E(), "")}>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_extra_element_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E())).containsExactly(1.to$E(), 2.to$E(), 3.to$E())
        }
        assertEquals(
            """expected to contain exactly:<${show(listOf(1.to$E(), 2.to$E(), 3.to$E()), "")}> but was:<${show(listOf(1.to$E(), 2.to$E()), "")}>
                | at index:2 expected:<${show(3.to$E(), "")}>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_missing_element_in_middle_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 3.to$E())).containsExactly(1.to$E(), 2.to$E(), 3.to$E())
        }
        assertEquals(
            """expected to contain exactly:<${show(listOf(1.to$E(), 2.to$E(), 3.to$E()), "")}> but was:<${show(listOf(1.to$E(), 3.to$E()), "")}>
                | at index:1 expected:<${show(2.to$E(), "")}>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_extra_element_in_middle_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E(), 3.to$E())).containsExactly(1.to$E(), 3.to$E())
        }
        assertEquals(
            """expected to contain exactly:<${show(listOf(1.to$E(), 3.to$E()), "")}> but was:<${show(listOf(1.to$E(), 2.to$E(), 3.to$E()), "")}>
                | at index:1 unexpected:<${show(2.to$E(), "")}>
            """.trimMargin(), error.message
        )
    }
    //endregion

    //region containsExactlyInAnyOrder
    @Test fun containsExactlyInAnyOrder_only_elements_passes() {
        assertThat($NOf(1.to$E(), 2.to$E())).containsExactlyInAnyOrder(2.to$E(), 1.to$E())
    }

    @Test fun containsExactlyInAnyOrder_only_elements_passes2() {
        assertThat($NOf(1.to$E(), 2.to$E(), 1.to$E())).containsExactlyInAnyOrder(2.to$E(), 1.to$E(), 1.to$E())
    }

    @Test fun containsExactlyInAnyOrder_duplicate_elements_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E(), 2.to$E())).containsExactlyInAnyOrder(2.to$E(), 1.to$E())
        }
        assertEquals(
                """expected to contain exactly in any order:<${show(listOf(2.to$E(), 1.to$E()), "")}> but was:<${show(listOf(1.to$E(), 2.to$E(), 2.to$E()), "")}>
                | extra elements found:<[${show(2.to$E(), "")}]>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactlyInAnyOrder_duplicate_elements_fails2() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E())).containsExactlyInAnyOrder(2.to$E(), 2.to$E(), 1.to$E())
        }
        assertEquals(
                """expected to contain exactly in any order:<${show(listOf(2.to$E(), 2.to$E(), 1.to$E()), "")}> but was:<${show(listOf(1.to$E(), 2.to$E()), "")}>
                | elements not found:<[${show(2.to$E(), "")}]>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactlyInAnyOrder_more_elements_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E(), 3.to$E())).containsExactlyInAnyOrder(2.to$E(), 1.to$E())
        }
        assertEquals(
                """expected to contain exactly in any order:<${show(listOf(2.to$E(), 1.to$E()), "")}> but was:<${show(listOf(1.to$E(), 2.to$E(), 3.to$E()), "")}>
                | extra elements found:<[${show(3.to$E(), "")}]>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactlyInAnyOrder_less_elements_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E(), 3.to$E())).containsExactlyInAnyOrder(2.to$E(), 1.to$E(), 3.to$E(), 4.to$E())
        }
        assertEquals(
                """expected to contain exactly in any order:<${show(listOf(2.to$E(), 1.to$E(), 3.to$E(), 4.to$E()), "")}> but was:<${show(listOf(1.to$E(), 2.to$E(), 3.to$E()), "")}>
                | elements not found:<[${show(4.to$E(), "")}]>
            """.trimMargin(),
                error.message
        )
    }

    @Test fun containsExactlyInAnyOrder_different_elements_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E())).containsExactlyInAnyOrder(2.to$E())
        }
        assertEquals(
                """expected to contain exactly in any order:<[${show(2.to$E(), "")}]> but was:<[${show(1.to$E(), "")}]>
                | elements not found:<[${show(2.to$E(), "")}]>
                | extra elements found:<[${show(1.to$E(), "")}]>
            """.trimMargin(),
                error.message
        )
    }
    //endregion

    //region each
    @Test fun each_empty_list_passes() {
        assertThat($NOf()).each { it.isEqualTo(1) }
    }

    @Test fun each_content_passes() {
        assertThat($NOf(1.to$E(), 2.to$E())).each { it.isGreaterThan(0.to$E()) }
    }

    @Test fun each_non_matching_content_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E(), 3.to$E())).each { it.isLessThan(2.to$E()) }
        }
        assertEquals(
            """The following assertions failed (2 failures)
                |	${opentestPackageName}AssertionFailedError: expected [[1]] to be less than:<${show(2.to$E(), "")}> but was:<${show(2.to$E(), "")}> ([${show(1.to$E(), "")}, ${show(2.to$E(), "")}, ${show(3.to$E(), "")}])
                |	${opentestPackageName}AssertionFailedError: expected [[2]] to be less than:<${show(2.to$E(), "")}> but was:<${show(3.to$E(), "")}> ([${show(1.to$E(), "")}, ${show(2.to$E(), "")}, ${show(3.to$E(), "")}])
            """.trimMargin().lines(), error.message!!.lines()
        )
    }
    //endregion

    //region index
    @Test fun index_successful_assertion_passes() {
        assertThat($NOf(1.to$E(), 2.to$E()), name = "subject").index(0).isEqualTo(1.to$E())
    }

    @Test fun index_unsuccessful_assertion_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E()), name = "subject").index(0).isGreaterThan(2.to$E())
        }
        assertEquals(
            "expected [subject[0]] to be greater than:<${show(2.to$E(), "")}> but was:<${show(1.to$E(), "")}> ([${show(1.to$E(), "")}, ${show(2.to$E(), "")}])",
            error.message
        )
    }

    @Test fun index_out_of_range_fails() {
        val error = assertFails {
            assertThat($NOf(1.to$E(), 2.to$E()), name = "subject").index(-1).isEqualTo(listOf(1.to$E()))
        }
        assertEquals("expected [subject] index to be in range:[0-2) but was:<-1>", error.message)
    }
    //endregion
}
