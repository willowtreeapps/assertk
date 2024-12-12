package test.assertk.assertions

import assertk.all
import assertk.assertThat
import assertk.assertions.any
import assertk.assertions.atLeast
import assertk.assertions.atMost
import assertk.assertions.contains
import assertk.assertions.containsAtLeast
import assertk.assertions.containsExactly
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.containsNone
import assertk.assertions.containsOnly
import assertk.assertions.doesNotContain
import assertk.assertions.each
import assertk.assertions.exactly
import assertk.assertions.extracting
import assertk.assertions.first
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isGreaterThanOrEqualTo
import assertk.assertions.isLessThan
import assertk.assertions.isNotEmpty
import assertk.assertions.none
import assertk.assertions.single
import test.assertk.opentestPackageName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IterableTest {
    //region contains
    @Test
    fun contains_element_present_passes() {
        assertThat(iterableOf(1, 2)).contains(2)
    }

    @Test
    fun contains_element_missing_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyIterable<Any?>()).contains(1)
        }
        assertEquals("expected to contain:<1> but was:<[]>", error.message)
    }
    //endregion

    //region doesNotContain
    @Test
    fun doesNotContain_element_missing_passes() {
        assertThat(emptyIterable<Any?>()).doesNotContain(1)
    }

    @Test
    fun doesNotContain_element_present_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2)).doesNotContain(2)
        }
        assertEquals("expected to not contain:<2> but was:<[1, 2]>", error.message)
    }
    //endregion

    //region containsNone
    @Test
    fun containsNone_missing_elements_passes() {
        assertThat(emptyIterable<Any?>()).containsNone(1)
    }

    @Test
    fun containsNone_present_element_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2)).containsNone(2, 3)
        }
        assertEquals(
            """expected to contain none of:<[2, 3]> but was:<[1, 2]>
                | elements not expected:<[2]>
            """.trimMargin(), error.message
        )
    }
    //region

    //region containsAtLeast
    @Test
    fun containsAtLeast_all_elements_passes() {
        assertThat(iterableOf(1, 2)).containsAtLeast(2, 1)
    }

    @Test
    fun containsAtLeast_some_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1)).containsAtLeast(1, 2)
        }
        assertEquals(
            """expected to contain all:<[1, 2]> but was:<[1]>
                | elements not found:<[2]>
            """.trimMargin(), error.message
        )
    }
    //endregion

    //region containsOnly
    @Test
    fun containsOnly_only_elements_passes() {
        assertThat(iterableOf(1, 2)).containsOnly(2, 1)
    }

    @Test
    fun containsOnly_duplicate_elements_passes() {
        assertThat(iterableOf(1, 2, 2)).containsOnly(2, 1)
    }

    @Test
    fun containsOnly_duplicate_elements_passes2() {
        assertThat(iterableOf(1, 2)).containsOnly(2, 2, 1)
    }

    @Test
    fun containsOnly_more_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 3)).containsOnly(2, 1)
        }
        assertEquals(
            """expected to contain only:<[2, 1]> but was:<[1, 2, 3]>
                | extra elements found:<[3]>
            """.trimMargin(), error.message
        )
    }

    @Test
    fun containsOnly_less_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 3)).containsOnly(2, 1, 3, 4)
        }
        assertEquals(
            """expected to contain only:<[2, 1, 3, 4]> but was:<[1, 2, 3]>
                | elements not found:<[4]>
            """.trimMargin(),
            error.message
        )
    }

    @Test
    fun containsOnly_different_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1)).containsOnly(2)
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

    //region containsExactlyInAnyOrder
    @Test
    fun containsExactlyInAnyOrder_only_elements_passes() {
        assertThat(iterableOf(1, 2)).containsExactlyInAnyOrder(2, 1)
    }

    @Test
    fun containsExactlyInAnyOrder_only_elements_passes2() {
        assertThat(iterableOf(1, 2, 1)).containsExactlyInAnyOrder(2, 1, 1)
    }

    @Test
    fun containsExactlyInAnyOrder_duplicate_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 2)).containsExactlyInAnyOrder(2, 1)
        }
        assertEquals(
            """expected to contain exactly in any order:<[2, 1]> but was:<[1, 2, 2]>
                | extra elements found:<[2]>
            """.trimMargin(), error.message
        )
    }

    @Test
    fun containsExactlyInAnyOrder_duplicate_elements_fails2() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2)).containsExactlyInAnyOrder(2, 2, 1)
        }
        assertEquals(
            """expected to contain exactly in any order:<[2, 2, 1]> but was:<[1, 2]>
                | elements not found:<[2]>
            """.trimMargin(), error.message
        )
    }

    @Test
    fun containsExactlyInAnyOrder_more_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 3)).containsExactlyInAnyOrder(2, 1)
        }
        assertEquals(
            """expected to contain exactly in any order:<[2, 1]> but was:<[1, 2, 3]>
                | extra elements found:<[3]>
            """.trimMargin(), error.message
        )
    }

    @Test
    fun containsExactlyInAnyOrder_less_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 3)).containsExactlyInAnyOrder(2, 1, 3, 4)
        }
        assertEquals(
            """expected to contain exactly in any order:<[2, 1, 3, 4]> but was:<[1, 2, 3]>
                | elements not found:<[4]>
            """.trimMargin(),
            error.message
        )
    }

    @Test
    fun containsExactlyInAnyOrder_different_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1)).containsExactlyInAnyOrder(2)
        }
        assertEquals(
            """expected to contain exactly in any order:<[2]> but was:<[1]>
                | elements not found:<[2]>
                | extra elements found:<[1]>
            """.trimMargin(),
            error.message
        )
    }

    @Test fun containsExactlyInAnyOrder_only_elements_of_another_iterable_passes() {
        assertThat(iterableOf(1, 2)).containsExactlyInAnyOrder(iterableOf(2, 1))
    }

    @Test fun containsExactlyInAnyOrder_only_elements_of_another_iterable_passes2() {
        assertThat(iterableOf(1, 2, 1)).containsExactlyInAnyOrder(iterableOf(2, 1, 1))
    }

    @Test fun containsExactlyInAnyOrder_duplicate_elements_of_another_iterable_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 2)).containsExactlyInAnyOrder(iterableOf(2, 1))
        }
        assertEquals(
            """expected to contain exactly in any order:<[2, 1]> but was:<[1, 2, 2]>
                | extra elements found:<[2]>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactlyInAnyOrder_of_another_iterable_duplicate_elements_fails2() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2)).containsExactlyInAnyOrder(iterableOf(2, 2, 1))
        }
        assertEquals(
            """expected to contain exactly in any order:<[2, 2, 1]> but was:<[1, 2]>
                | elements not found:<[2]>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactlyInAnyOrder__of_another_iterable_more_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 3)).containsExactlyInAnyOrder(iterableOf(2, 1))
        }
        assertEquals(
            """expected to contain exactly in any order:<[2, 1]> but was:<[1, 2, 3]>
                | extra elements found:<[3]>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsExactlyInAnyOrder_of_another_iterable_less_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 3)).containsExactlyInAnyOrder(iterableOf(2, 1, 3, 4))
        }
        assertEquals(
            """expected to contain exactly in any order:<[2, 1, 3, 4]> but was:<[1, 2, 3]>
                | elements not found:<[4]>
            """.trimMargin(),
            error.message
        )
    }

    @Test fun containsExactlyInAnyOrder_of_another_iterable_different_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1)).containsExactlyInAnyOrder(iterableOf(2))
        }
        assertEquals(
            """expected to contain exactly in any order:<[2]> but was:<[1]>
                | elements not found:<[2]>
                | extra elements found:<[1]>
            """.trimMargin(),
            error.message
        )
    }
    //endregion

    //region each
    @Test
    fun each_empty_list_passes() {
        assertThat(emptyIterable<Int>()).each { it.isEqualTo(1) }
    }

    @Test
    fun each_content_passes() {
        assertThat(iterableOf(1, 2)).each { it.isGreaterThan(0) }
    }

    @Test
    fun each_non_matching_content_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 3)).each { it.isLessThan(2) }
        }
        assertEquals(
            """The following assertions failed (2 failures)
              |${"\t"}${opentestPackageName}AssertionFailedError: expected [[1]] to be less than:<2> but was:<2> ([1, 2, 3])
              |${"\t"}${opentestPackageName}AssertionFailedError: expected [[2]] to be less than:<2> but was:<3> ([1, 2, 3])
            """.trimMargin().lines(), error.message!!.lines()
        )
    }
    //endregion

    //region none
    @Test
    fun none_empty_list_passes() {
        assertThat(emptyIterable<Int>()).none { it.isEqualTo(1) }
    }

    @Test
    fun none_matching_content_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2)).none { it.isGreaterThan(0) }
        }
        assertEquals(
            """expected none to pass
            | at index:0 passed:<1>
            | at index:1 passed:<2>
            """.trimMargin().lines(), error.message!!.lines()
        )
    }

    @Test
    fun none_matching_some_content_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(listOf(1, 2, 3) as Iterable<Int>).none { it.isGreaterThanOrEqualTo(3) }
        }
        assertEquals(
            """expected none to pass
            | at index:2 passed:<3>
            """.trimMargin().lines(), error.message!!.lines()
        )
    }

    @Test
    fun none_all_non_matching_content_passes() {
        assertThat(listOf(1, 2, 3) as Iterable<Int>).none { it.isLessThan(0) }
    }

    @Test
    fun none_multiple_failures_passes() {
        assertThat(iterableOf(1, 2, 3)).none {
            it.isLessThan(2)
            it.isGreaterThan(2)
        }
    }
    //endregion

    //region atLeast
    @Test
    fun atLeast_too_many_failures_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 3)).atLeast(2) { it.isGreaterThan(2) }
        }
        assertEquals(
            """expected to pass at least 2 times (2 failures)
            |${"\t"}${opentestPackageName}AssertionFailedError: expected [[0]] to be greater than:<2> but was:<1> ([1, 2, 3])
            |${"\t"}${opentestPackageName}AssertionFailedError: expected [[1]] to be greater than:<2> but was:<2> ([1, 2, 3])
        """.trimMargin().lines(), error.message!!.lines()
        )
    }

    @Test
    fun atLeast_no_failures_passes() {
        assertThat(iterableOf(1, 2, 3)).atLeast(2) { it.isGreaterThan(0) }
    }

    @Test
    fun atLeast_less_than_times_failures_passes() {
        assertThat(iterableOf(1, 2, 3)).atLeast(2) { it.isGreaterThan(1) }
    }

    @Test
    fun atLeast_works_in_a_soft_assert_context() {
        assertThat(iterableOf(1, 2, 3)).all { atLeast(2) { it.isGreaterThan(1) } }
    }

    @Test
    fun atLeast_multiple_failures_passes() {
        assertThat(listOf(1, 2, 3) as Iterable<Int>).atLeast(2) {
            it.isGreaterThan(1)
            it.isGreaterThan(1)
        }
    }
    //endregion

    //region atMost
    @Test
    fun atMost_more_than_times_passed_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 3)).atMost(2) { it.isGreaterThan(0) }
        }
        assertEquals(
            """expected to pass at most 2 times""".trimMargin(), error.message
        )
    }

    @Test
    fun atMost_exactly_times_passed_passes() {
        assertThat(iterableOf(1, 2, 3)).atMost(2) { it.isGreaterThan(1) }
    }


    @Test
    fun atMost_less_than_times_passed_passes() {
        assertThat(iterableOf(1, 2)).atMost(2) { it.isGreaterThan(1) }
    }
    //endregion


    //region exactly
    @Test
    fun exactly_too_few_passes_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 3)).exactly(2) { it.isGreaterThan(2) }
        }
        assertEquals(
            """expected to pass exactly 2 times (2 failures)
            |${"\t"}${opentestPackageName}AssertionFailedError: expected [[0]] to be greater than:<2> but was:<1> ([1, 2, 3])
            |${"\t"}${opentestPackageName}AssertionFailedError: expected [[1]] to be greater than:<2> but was:<2> ([1, 2, 3])
            """.trimMargin().lines(), error.message!!.lines()
        )
    }

    @Test
    fun exactly_too_many_passes_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(5, 4, 3)).exactly(2) { it.isGreaterThan(2) }
        }
        assertEquals(
            """expected to pass exactly 2 times""".trimMargin(), error.message
        )
    }

    @Test
    fun exactly_too_few_inside_all_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(5, 4, 3)).all {
                exactly(2) { it.isGreaterThan(2) }
            }
        }
        assertEquals(
            """expected to pass exactly 2 times""".trimMargin(), error.message
        )
    }

    @Test
    fun exactly_times_passed_passes() {
        assertThat(iterableOf(0, 1, 2)).exactly(2) { it.isGreaterThan(0) }
    }

    @Test
    fun exactly_times_passed_passes_multiple_assertions() {
        assertThat(listOf(0, 1, 2) as Iterable<Int>).exactly(2) {
            it.isGreaterThan(0)
            it.isGreaterThan(0)
        }
    }
    //endregion

    //region any
    @Test
    fun any_passes_if_one_item_passes() {
        assertThat(iterableOf(1, 2)).any { it.isGreaterThan(1) }
    }

    @Test
    fun any_fails_if_all_fail() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2)).any { it.isGreaterThan(3) }
        }
        assertEquals(
            """expected any item to pass (2 failures)
	        |${"\t"}${opentestPackageName}AssertionFailedError: expected [[0]] to be greater than:<3> but was:<1> ([1, 2])
	        |${"\t"}${opentestPackageName}AssertionFailedError: expected [[1]] to be greater than:<3> but was:<2> ([1, 2])
            """.trimMargin().lines(), error.message!!.lines()
        )
    }

    @Test
    fun any_multiple_assertions_fail() {
        assertFailsWith<AssertionError> {
            assertThat(iterableOf("one")).any {
                it.isEqualTo("two")
                it.isEqualTo("two")
            }
        }
    }

    @Test
    fun any_multiple_items_fail() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(1, 2, 3)).any {
                it.isEqualTo(4)
            }
        }

        assertEquals(
            """expected any item to pass (3 failures)
	        |${"\t"}${opentestPackageName}AssertionFailedError: expected [[0]]:<[4]> but was:<[1]> ([1, 2, 3])
	        |${"\t"}${opentestPackageName}AssertionFailedError: expected [[1]]:<[4]> but was:<[2]> ([1, 2, 3])
	        |${"\t"}${opentestPackageName}AssertionFailedError: expected [[2]]:<[4]> but was:<[3]> ([1, 2, 3])
            """.trimMargin().lines(), error.message!!.lines()
        )
    }

    @Test
    fun any_with_exception_still_passes() {
        var count = 0
        assertThat(iterableOf("one", "two")).any {
            if (count == 1) {
                throw Exception()
            }
            count++
        }
    }
    //endregion

    //region isEmpty
    @Test
    fun empty_iterable_passes_is_empty() {
        val empty = emptyIterable<Int>()
        assertThat(empty).isEmpty()
    }

    @Test
    fun non_empty_iterable_fails_is_empty() {
        val nonEmpty = iterableOf(1)
        val error = assertFailsWith<AssertionError> {
            assertThat(nonEmpty).isEmpty()
        }
        assertEquals("expected to be empty but was:<[1]>", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test
    fun non_empty_iterable_passes_is_not_empty() {
        val nonEmpty = iterableOf(1)
        assertThat(nonEmpty).isNotEmpty()
    }

    @Test
    fun empty_iterable_fails_is_not_empty() {
        val empty = emptyIterable<Int>()
        val error = assertFailsWith<AssertionError> {
            assertThat(empty).isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region extracting
    @Test
    fun single_extracting_function_passes() {
        assertThat(iterableOf("one", "two")).extracting { it.length }.containsExactly(3, 3)
    }

    @Test
    fun single_extracting_function_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf("one", "two")).extracting { it.length }.containsExactly(2, 2)
        }
        assertEquals(
            """expected to contain exactly:<[2, 2]> but was:<[3, 3]>
            | at index:0 expected:<2>
            | at index:0 unexpected:<3>
            | at index:1 expected:<2>
            | at index:1 unexpected:<3> (["one", "two"])""".trimMargin(), error.message
        )
    }

    @Test
    fun pair_extracting_function_passes() {
        assertThat(iterableOf(Thing("one", 1, '1'), Thing("two", 2, '2')))
            .extracting(Thing::one, Thing::two)
            .containsExactly("one" to 1, "two" to 2)
    }

    @Test
    fun pair_extracting_function_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(Thing("one", 1, '1'), Thing("two", 2, '2')))
                .extracting(Thing::one, Thing::two)
                .containsExactly("one" to 2, "two" to 1)
        }
        assertEquals(
            """expected to contain exactly:<[("one", 2), ("two", 1)]> but was:<[("one", 1), ("two", 2)]>
            | at index:0 expected:<("one", 2)>
            | at index:0 unexpected:<("one", 1)>
            | at index:1 expected:<("two", 1)>
            | at index:1 unexpected:<("two", 2)> ([Thing(one=one, two=1, three=1), Thing(one=two, two=2, three=2)])""".trimMargin(),
            error.message
        )
    }

    @Test
    fun triple_extracting_function_passes() {
        assertThat(iterableOf(Thing("one", 1, '1'), Thing("two", 2, '2')))
            .extracting(Thing::one, Thing::two, Thing::three)
            .containsExactly(Triple("one", 1, '1'), Triple("two", 2, '2'))
    }

    @Test
    fun triple_extracting_function_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(iterableOf(Thing("one", 1, '1'), Thing("two", 2, '2')))
                .extracting(Thing::one, Thing::two, Thing::three)
                .containsExactly(Triple("one", 1, '2'), Triple("two", 2, '3'))
        }
        assertEquals(
            """expected to contain exactly:<[("one", 1, '2'), ("two", 2, '3')]> but was:<[("one", 1, '1'), ("two", 2, '2')]>
            | at index:0 expected:<("one", 1, '2')>
            | at index:0 unexpected:<("one", 1, '1')>
            | at index:1 expected:<("two", 2, '3')>
            | at index:1 unexpected:<("two", 2, '2')> ([Thing(one=one, two=1, three=1), Thing(one=two, two=2, three=2)])""".trimMargin(),
            error.message
        )
    }
    //region extracting

    //region first
    @Test
    fun first_element_present_match_passes() {
        assertThat(listOf(1, 2)).first().isEqualTo(1)
    }

    @Test
    fun first_element_present_mismatch_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(listOf(1, 2)).first().isEqualTo(2)
        }
        assertEquals("expected [first]:<[2]> but was:<[1]> ([1, 2])", error.message)
    }

    @Test
    fun first_element_missing_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyList<Any?>()).first().isEqualTo(1)
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region first
    @Test
    fun single_single_element_match_passes() {
        assertThat(listOf(1)).single().isEqualTo(1)
    }

    @Test
    fun single_single_element_mismatch_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(listOf(1)).single().isEqualTo(2)
        }
        assertEquals("expected [single]:<[2]> but was:<[1]> ([1])", error.message)
    }

    @Test
    fun single_no_element_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyList<Any?>()).single().isEqualTo(1)
        }
        assertEquals("expected to have single element but was empty", error.message)
    }

    @Test
    fun single_multiple_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(listOf(1, 2)).single().isEqualTo(1)
        }
        assertEquals("expected to have single element but has 2: <[1, 2]>", error.message)
    }
    //endregion

    data class Thing(val one: String, val two: Int, val three: Char)
}

private fun <T> emptyIterable(): Iterable<T> = emptyList()
private fun <T> iterableOf(vararg elements: T): Iterable<T> = elements.toList()
