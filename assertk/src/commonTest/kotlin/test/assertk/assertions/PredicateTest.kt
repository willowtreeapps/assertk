package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.matchesPredicate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PredicateTest {

    @Test
    fun matchesPredicate_true_predicate_passes() {
        val divisibleBy5: (Int) -> Boolean = { it % 5 == 0 }

        assertThat(10).matchesPredicate(divisibleBy5)
    }

    @Test
    fun matchesPredicate_false_predicate_fails() {
        val divisibleBy5: (Int) -> Boolean = { it % 5 == 0 }
        val error = assertFailsWith<AssertionError> { assertThat(6).matchesPredicate(divisibleBy5) }

        assertEquals("expected 6 to satisfy the predicate", error.message)
    }
}
