package test.assertk.coroutines.assertions

import assertk.assertThat
import assertk.assertions.support.show
import assertk.coroutines.assertions.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@OptIn(ExperimentalCoroutinesApi::class)
class FlowTest {
    //region isEmpty
    @Test fun isEmpty_empty_passes() = runTest {
        assertThat(flowOf<Any?>()).isEmpty()
    }

    @Test fun isEmpty_non_empty_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf<Any?>(null)).isEmpty()
        }
        assertEquals("expected to be empty but received:<null>", error.message)
    }

    @Test fun isEmpty_non_empty_in_flow_that_doesnt_complete_fails() = runTest {
        val error = assertFails {
            assertThat(nonCompletingFlowOf(null)).isEmpty()
        }
        assertEquals("expected to be empty but received:<null>", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test fun isNotEmpty_non_empty_passes() = runTest {
        assertThat(flowOf<Any?>(null)).isNotEmpty()
    }

    @Test fun isNotEmpty_empty_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf<Any?>()).isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region hasCount
    @Test fun hasSize_correct_size_passes() = runTest {
        assertThat(flowOf<Any?>()).hasCount(0)
    }

    @Test fun hasSize_wrong_size_fails() = runTest {
        val flow = flowOf<Any?>()
        val error = assertFails {
            assertThat(flow).hasCount(1)
        }
        assertEquals("expected [count()]:<[1]> but was:<[0]> ${show(flow, "()")}", error.message)
    }
    //endregion

    //region contains
    @Test fun contains_element_present_passes() = runTest {
        assertThat(flowOf(1, 2)).contains(2)
    }

    @Test fun contains_element_present_in_flow_that_doesnt_complete_passes() = runTest {
        assertThat(nonCompletingFlowOf(1, 2)).contains(2)
    }

    @Test fun contains_element_missing_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf<Any?>()).contains(1)
        }
        assertEquals("expected to contain:<1> but received:<[]>", error.message)
    }
    //endregion

    //region doesNotContain
    @Test fun doesNotContain_element_missing_passes() = runTest {
        assertThat(flowOf<Any?>()).doesNotContain(1)
    }

    @Test fun doesNotContain_element_present_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf(1, 2)).doesNotContain(2)
        }
        assertEquals("expected to not contain:<2> but received:<[1, 2]>", error.message)
    }

    @Test fun doesNotContain_element_present_in_flow_that_doesnt_complete_fails() = runTest {
        val error = assertFails {
            assertThat(nonCompletingFlowOf(1, 2)).doesNotContain(2)
        }
        assertEquals("expected to not contain:<2> but received:<[1, 2]>", error.message)
    }
    //endregion

    //region containsNone
    @Test fun containsNone_missing_elements_passes() = runTest {
        assertThat(flowOf<Any?>()).containsNone(1)
    }

    @Test fun containsNone_present_element_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf(1, 2)).containsNone(2, 3)
        }
        assertEquals(
            """expected to contain none of:<[2, 3]> but received:<[1, 2]>
                | element not expected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsNone_present_element_in_flow_that_doesnt_complete_fails() = runTest {
        val error = assertFails {
            assertThat(nonCompletingFlowOf(1, 2)).containsNone(2, 3)
        }
        assertEquals(
            """expected to contain none of:<[2, 3]> but received:<[1, 2]>
                | element not expected:<2>
            """.trimMargin(), error.message
        )
    }
    //region

    //region containsAll
    @Test fun containsAll_all_elements_passes() = runTest {
        assertThat(flowOf(1, 2)).containsAll(2, 1)
    }

    @Test fun containsAll_all_elements_in_flow_that_doesnt_complete_passes() = runTest {
        assertThat(nonCompletingFlowOf(1, 2)).containsAll(2, 1)
    }

    @Test fun containsAll_some_elements_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf(1)).containsAll(1, 2)
        }
        assertEquals(
            """expected to contain all:<[1, 2]> but received:<[1]>
                | elements not found:<[2]>
            """.trimMargin(), error.message
        )
    }
    //endregion

    //region containsOnly
    @Test fun containsOnly_only_elements_passes() = runTest {
        assertThat(flowOf(1, 2)).containsOnly(2, 1)
    }

    @Test fun containsOnly_more_elements_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf(1, 2, 3)).containsOnly(2, 1)
        }
        assertEquals(
            """expected to contain only:<[2, 1]> but received:<[1, 2, 3]>
                | extra elements found:<[3]>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsOnly_less_elements_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf(1, 2, 3)).containsOnly(2, 1, 3, 4)
        }
        assertEquals(
            """expected to contain only:<[2, 1, 3, 4]> but received:<[1, 2, 3]>
                | elements not found:<[4]>
            """.trimMargin(),
            error.message
        )
    }

    @Test fun containsOnly_different_elements_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf(1)).containsOnly(2)
        }
        assertEquals(
            """expected to contain only:<[2]> but received:<[1]>
                | elements not found:<[2]>
                | extra elements found:<[1]>
            """.trimMargin(),
            error.message
        )
    }
    //endregion

    //region containsExactly
    @Test fun containsExactly_all_elements_in_same_order_passes() = runTest {
        assertThat(flowOf(1, 2)).containsExactly(1, 2)
    }

    @Test fun containsExactly_all_elements_in_different_order_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf(1, 2)).containsExactly(2, 1)
        }
        assertEquals(
            """expected to contain exactly:<[2, 1]> but was:<[1, 2]>
                | at index:0 expected:<2>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_elements_in_different_order_fails2() = runTest {
        val error = assertFails {
            assertThat(flowOf("1", "2", "3")).containsExactly("2", "3", "1")
        }
        assertEquals(
            """expected to contain exactly:<["2", "3", "1"]> but was:<["1", "2", "3"]>
                | at index:0 unexpected:<"1">
                | at index:2 expected:<"1">
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_same_indexes_are_together() = runTest {
        val error = assertFails {
            assertThat(flowOf(1, 1)).containsExactly(2, 2)
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

    @Test fun containsExactly_missing_element_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf(1, 2)).containsExactly(3)
        }
        assertEquals(
            """expected to contain exactly:<[3]> but was:<[1, 2]>
                | at index:0 expected:<3>
                | at index:0 unexpected:<1>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_extra_element_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf(1, 2)).containsExactly(1, 2, 3)
        }
        assertEquals(
            """expected to contain exactly:<[1, 2, 3]> but was:<[1, 2]>
                | at index:2 expected:<3>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_missing_element_in_middle_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf(1, 3)).containsExactly(1, 2, 3)
        }
        assertEquals(
            """expected to contain exactly:<[1, 2, 3]> but was:<[1, 3]>
                | at index:1 expected:<2>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactly_extra_element_in_middle_fails() = runTest {
        val error = assertFails {
            assertThat(flowOf(1, 2, 3)).containsExactly(1, 3)
        }
        assertEquals(
            """expected to contain exactly:<[1, 3]> but was:<[1, 2, 3]>
                | at index:1 unexpected:<2>
            """.trimMargin(), error.message
        )
    }
    //endregion
}

@ExperimentalCoroutinesApi
private fun <T> nonCompletingFlowOf(vararg elements: T) = callbackFlow {
    for (element in elements) {
        send(element)
    }
    awaitClose { }
}
