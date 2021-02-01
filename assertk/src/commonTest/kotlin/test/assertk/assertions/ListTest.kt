package test.assertk.assertions

import assertk.assertThat
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
}

class MyList<E>(private vararg val elements: E) : AbstractList<E>() {
    override val size: Int get() = elements.size

    override fun get(index: Int): E = elements[index]

    override fun equals(other: Any?): Boolean {
        return other is MyList<*> && super.equals(other)
    }

    override fun hashCode(): Int = elements.hashCode()
}