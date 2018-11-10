package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class PredicateTest {

    @Test fun assert_predicate_passes() {

        val divisibleBy5 : (Int) -> Boolean = { it % 5 == 0 }

        assert(10).assertPredicate(divisibleBy5)

    }

    @Test fun assert_predicate_fails() {
        val divisibleBy5 : (Int) -> Boolean = { it % 5 == 0 }

        val error = assertFails { assert(6).assertPredicate(divisibleBy5) }

        assertEquals("expected 6 to satisfy the predicate", error.message)
    }

    @Test fun least_expectancy_not_met_fails() {
        val divisibleBy5 : (Int) -> Boolean = { it % 5 == 0 }

        val error = assertFails {
            assert(listOf(1, 5, 4)).atLeast(2) { it -> it.assertPredicate(divisibleBy5) }
        }
        assertEquals("expected atleast 2 occurences in <[1, 5, 4]> but [0],[2] did not match", error.message)

    }

    @Test fun least_expectancy_met_passes() {
        val divisibleBy5 : (Int) -> Boolean = { it % 5 == 0 }

        assert(listOf(5, 2, 10) as Iterable<Int>).atLeast(2) { it -> it.assertPredicate(divisibleBy5) }
    }

    @Test fun least_expectancy_met_inline_condition_passes() {
        val contains: (Int) -> Boolean = { listOf(1, 2, 3).contains(it) }

        assert(listOf(1, 2, 3) as Iterable<Int>).atLeast(2) { it -> it.assertPredicate(contains) }
    }

}
