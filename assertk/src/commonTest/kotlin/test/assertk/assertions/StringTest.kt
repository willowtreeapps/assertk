package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StringTest {

    //region isEqualTo
    @Test
    fun isEqualTo_same_value_passes() {
        assertThat("test").isEqualTo("test")
    }

    @Test
    fun isEqualTo_different_value_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("").isEqualTo("test")
        }
        assertEquals("expected:<\"[test]\"> but was:<\"[]\">", error.message)
    }

    @Test
    fun isEqualTo_different_null_value_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("null").isEqualTo(null)
        }
        assertEquals("expected:<null> but was:<\"null\">", error.message)
    }

    @Test
    fun isEqualTo_same_value_ignore_case_passes() {
        assertThat("Test").isEqualTo("tesT", true)
    }

    @Test
    fun isEqualTo_different_value_ignore_case_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("Test").isEqualTo("tesT", false)
        }
        assertEquals("expected:<\"[tesT]\"> but was:<\"[Test]\">", error.message)
    }

    @Test
    fun isEqualTo_renders_different_line_endings() {
        val error = assertFailsWith<AssertionError> {
            assertThat("Test\n").isEqualTo("Test\r\n")
        }
        assertEquals("expected:<\"Test[\\r]\n\"> but was:<\"Test[]\n\">", error.message)
    }

    @Test
    fun isEqual_renders_tabs_vs_spaces() {
        val error = assertFailsWith<AssertionError> {
            assertThat("\tTest").isEqualTo("    Test")
        }
        assertEquals("expected:<\"[    ]Test\"> but was:<\"[\\t]Test\">", error.message)
    }

    @Test
    fun isEqual_renders_newline_vs_not() {
        val error = assertFailsWith<AssertionError> {
            assertThat("Test\n").isEqualTo("Test")
        }
        assertEquals("expected:<\"Test[]\"> but was:<\"Test[\\n]\">", error.message)
    }
    //endregion

    //region isNotEqualTo
    @Test
    fun isNotEqualTo_same_value_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").isNotEqualTo("test")
        }
        assertEquals("expected to not be equal to:<\"test\">", error.message)
    }

    @Test
    fun isNotEqualTo_different_value_passes() {
        assertThat("").isNotEqualTo("test")
    }

    @Test
    fun isNotEqualTo_different_null_value_passes() {
        assertThat("null").isNotEqualTo(null)
    }

    @Test
    fun isNotEqualTo_same_value_ignore_case_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("Test").isNotEqualTo("tesT", true)
        }
        assertEquals("expected:<\"tesT\"> not to be equal to (ignoring case):<\"Test\">", error.message)
    }

    @Test
    fun isNotEqualTo_different_value_ignore_case_passes() {
        assertThat("Test").isNotEqualTo("tesT", false)
    }
    //endregion

}
