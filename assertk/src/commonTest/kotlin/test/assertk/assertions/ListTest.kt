package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.containsAllInOrder
import assertk.assertions.containsExactly
import assertk.assertions.index
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ListTest {
    //region containsExactly
    @Test fun containsExactly_all_elements_in_same_order_passes() {
        assertThat(listOf(1, 2)).containsExactly(1, 2)
    }

    @Test fun containsExactly_all_elements_in_different_order_fails() {
        val error = assertFails {
            assertThat(listOf(1, 2)).containsExactly(2, 1)
        }
        assertEquals(
            """expected to contain exactly:<[2, 1]> but was:<[1, 2]>
                | at index:0 expected:<2>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }

    // https://github.com/willowtreeapps/assertk/issues/185
    @Test fun containsExactly_elements_in_different_order_fails2() {
        val error = assertFails {
            assertThat(listOf("1", "2", "3")).containsExactly("2", "3", "1")
        }
        assertEquals(
            """expected to contain exactly:<["2", "3", "1"]> but was:<["1", "2", "3"]>
                | at index:0 unexpected:<"1">
                | at index:2 expected:<"1">
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_same_indexes_are_together() {
        val error = assertFails {
            assertThat(listOf(1, 1)).containsExactly(2, 2)
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

    @Test fun containsExactly_missing_element_fails() {
        val error = assertFails {
            assertThat(listOf(1, 2)).containsExactly(3)
        }
        assertEquals(
            """expected to contain exactly:<[3]> but was:<[1, 2]>
                | at index:0 expected:<3>
                | at index:0 unexpected:<1>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_extra_element_fails() {
        val error = assertFails {
            assertThat(listOf(1, 2)).containsExactly(1, 2, 3)
        }
        assertEquals(
            """expected to contain exactly:<[1, 2, 3]> but was:<[1, 2]>
                | at index:2 expected:<3>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_missing_element_in_middle_fails() {
        val error = assertFails {
            assertThat(listOf(1, 3)).containsExactly(1, 2, 3)
        }
        assertEquals(
            """expected to contain exactly:<[1, 2, 3]> but was:<[1, 3]>
                | at index:1 expected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_extra_element_in_middle_fails() {
        val error = assertFails {
            assertThat(listOf(1, 2, 3)).containsExactly(1, 3)
        }
        assertEquals(
            """expected to contain exactly:<[1, 3]> but was:<[1, 2, 3]>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_custom_list_impl_passes() {
        assertThat(MyList("one", "two")).containsExactly("one", "two")
    }
    //endregion

    //region index
    @Test fun index_successful_assertion_passes() {
        assertThat(listOf("one", "two"), name = "subject").index(0).isEqualTo("one")
    }

    @Test fun index_unsuccessful_assertion_fails() {
        val error = assertFails {
            assertThat(listOf("one", "two"), name = "subject").index(0).isEqualTo("wrong")
        }
        assertEquals(
            "expected [subject[0]]:<\"[wrong]\"> but was:<\"[one]\"> ([\"one\", \"two\"])",
            error.message
        )
    }

    @Test fun index_out_of_range_fails() {
        val error = assertFails {
            assertThat(listOf("one", "two"), name = "subject").index(-1).isEqualTo(listOf("one"))
        }
        assertEquals("expected [subject] index to be in range:[0-2) but was:<-1>", error.message)
    }
    //endregion

    //region containsAllInOrder
    @Test fun containsAllInOrder_fails_if_sublist_is_empty_and_actual_is_not_empty() {
        val emptySubList: List<String> = emptyList()
        val given: List<String> = listOf("Jason", "Jane", "Anne", "Darius", "Lee")
        assertFails { assertThat(given).containsAllInOrder(emptySubList) }
    }

    @Test fun containsAllInOrder_passes_when_sublist_and_actual_list_is_empty() {
        val sublist: List<String> = emptyList()
        val actualList: List<String> = emptyList()
        assertThat(actualList).containsAllInOrder(sublist)
    }

    @Test fun containsAllInOrder_fails_if_sublist_is_contained_in_actual_but_not_in_exact_order() {
        val actualList: List<String> = listOf("John", "Victoria", "Lee-Anne")
        val sublist: List<String> = listOf("John", "Lee-Anne", "Victoria")
        assertFails { assertThat(actualList).containsAllInOrder(sublist) }
    }

    @Test fun containsAllInOrder_passes_if_actual_contains_sublist_in_exact_order() {
        val actualList: List<String> = listOf("John", "Victoria", "Lee-Anne", "Darius", "Victor")
        val sublist: List<String> = listOf("Victoria", "Lee-Anne", "Darius")
        assertThat(actualList).containsAllInOrder(sublist)
    }

    @Test fun containsAllInOrder_passes_if_sublist_is_the_head_of_the_list() {
        val actualList: List<String?> = listOf("1", "2", null, "4")
        val sublist: List<String?> = listOf("1", "2", null, "4")
        assertThat(actualList).containsAllInOrder(sublist)
    }

    @Test fun containsAllInOrder_passes_if_sublist_is_the_tail_of_the_list() {
        val actualList: List<String?> = listOf("1", "2", null, "4")
        val sublist: List<String?> = listOf(null, "4")
        assertThat(actualList).containsAllInOrder(sublist)
    }

    @Test fun containsAllInOrder_passes_if_sublist_is_exactly_the_same_size_and_order() {
        val actualList: List<Int> = listOf(1, 2, 3)
        val sublist:List<Int> = listOf(1, 2, 3)
        assertThat(actualList).containsAllInOrder(sublist)
    }

    @Test fun containsAllInOrder_passes_if_actual_list_contain_fully_matched_sublist_after_partial_match() {
        val sublist: List<String> = listOf("Gordan","Jayce","Ann-Lee")
        val partialList: List<String> = listOf("Gordan", "Jayce")
        val actualList: List<String> = listOf("Andy", "John") + partialList + listOf("Elly") + sublist
        assertThat(actualList).containsAllInOrder(sublist)
    }

}

class MyList<E>(private vararg val elements: E) : AbstractList<E>() {
    override val size: Int get() = elements.size

    override fun get(index: Int): E = elements[index]

    override fun equals(other: Any?): Boolean {
        return other is MyList<*> && super.equals(other)
    }

    override fun hashCode(): Int = elements.hashCode()
}
