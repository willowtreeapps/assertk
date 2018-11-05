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
        fun divisibleBy5(value: Int) : Boolean {
           return value % 5 == 0
        }
        val error = assertFails {
            assert(listOf(1, 5, 4)).areAtLeast(2) { divisibleBy5(it) }
        }
        assertEquals("expected atleast 2 occurences in <[1, 5, 4]>", error.message)

    }

    @Test fun least_expectancy_met_passes() {
        fun divisibleBy5(value: Int) : Boolean {
           return value % 5 == 0
        }
        assert(listOf(5, 2, 10) as Iterable<Int>).areAtLeast(2) { divisibleBy5(it) }
    }

    @Test fun least_expectancy_met_inline_condition_passes() {
        assert(listOf(1, 2, 3) as Iterable<Int>).areAtLeast(2) { listOf(1, 2, 3).contains(it) }
    }
}
