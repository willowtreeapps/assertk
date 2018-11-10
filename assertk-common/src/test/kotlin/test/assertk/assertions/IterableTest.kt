package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class IterableTest {
    //region contains
    @Test fun contains_element_present_passes() {
        assert(listOf(1, 2) as Iterable<Int>).contains(2)
    }

    @Test fun contains_element_missing_fails() {
        val error = assertFails {
            assert(emptyList<Any?>() as Iterable<Any?>).contains(1)
        }
        assertEquals("expected to contain:<1> but was:<[]>", error.message)
    }
    //endregion

    //region doesNotContain
    @Test fun doesNotContain_element_missing_passes() {
        assert(emptyList<Any?>() as Iterable<Any?>).doesNotContain(1)
    }

    @Test fun doesNotContain_element_present_fails() {
        val error = assertFails {
            assert(listOf(1, 2) as Iterable<Int>).doesNotContain(2)
        }
        assertEquals("expected to not contain:<2> but was:<[1, 2]>", error.message)
    }
    //endregion

    //region each
    @Test fun each_empty_list_passes() {
        assert(emptyList<Int>() as Iterable<Int>).each { it.isEqualTo(1) }
    }

    @Test fun each_content_passes() {
        assert(listOf(1, 2) as Iterable<Int>).each { it.isGreaterThan(0) }
    }

    @Test fun each_non_matching_content_fails() {
        val error = assertFails {
            assert(listOf(1, 2, 3) as Iterable<Int>).each { it.isLessThan(2) }
        }
        assertEquals(
            """The following assertions failed (2 failures)
              |${"\t"}expected [[1]] to be less than:<2> but was:<2> ([1, 2, 3])
              |${"\t"}expected [[2]] to be less than:<2> but was:<3> ([1, 2, 3])
            """.trimMargin(), error.message
        )
    }
    //endregion

    //region atLeast
    @Test fun atLeast_to_many_failures_fails() {
        val error = assertFails {
            assert(listOf(1, 2, 3)).atLeast(2) { it -> it.isGreaterThan(2) }
        }
        assertEquals(
            """expected to pass at least 2 times (2 failures)
            |${"\t"}expected [[0]] to be greater than:<2> but was:<1> ([1, 2, 3])
            |${"\t"}expected [[1]] to be greater than:<2> but was:<2> ([1, 2, 3])
        """.trimMargin(), error.message
        )
    }

    @Test fun atLest_no_failures_passes() {
        assert(listOf(1, 2, 3) as Iterable<Int>).atLeast(2) { it -> it.isGreaterThan(0) }
    }

    @Test fun atLeast_less_than_times_failures_passes() {
        assert(listOf(1, 2, 3) as Iterable<Int>).atLeast(2) { it -> it.isGreaterThan(1) }
    }
    //endregion
}
