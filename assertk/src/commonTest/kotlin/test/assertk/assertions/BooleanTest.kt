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
        assertEquals("expected to be true but was false", error.message)
    }

    @Test
    fun isTrue_null_value_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(null as Boolean?).isTrue()
        }
        assertEquals("expected to be true but was null", error.message)
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
        assertEquals("expected to be false but was true", error.message)
    }

    @Test
    fun isFalse_null_value_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(null as Boolean?).isFalse()
        }
        assertEquals("expected to be false but was null", error.message)
    }
    //endregion
}
