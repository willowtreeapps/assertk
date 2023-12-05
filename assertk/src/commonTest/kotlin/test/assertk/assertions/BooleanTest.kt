package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BooleanTest {
    //region isTrue
    @Test
    fun isTrue_true_value_passes() {
        assertThat(true).isTrue()
    }

    @Test
    fun isTrue_false_value_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(false).isTrue()
        }
        assertEquals("expected to be true", error.message)
    }
    //endregion

    //region isFalse
    @Test
    fun isFalse_false_value_passes() {
        assertThat(false).isFalse()
    }

    @Test
    fun isFalse_true_value_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(true).isFalse()
        }
        assertEquals("expected to be false", error.message)
    }
    //endregion
}
