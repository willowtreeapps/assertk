package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import assertk.assertions.support.show
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class StringTest {

    //region isEqualTo
    @Test fun isEqualTo_same_value_passes() {
        assertThat("test").isEqualTo("test")
    }

    @Test fun isEqualTo_different_value_fails() {
        val error = assertFails {
            assertThat("").isEqualTo("test")
        }
        assertEquals("expected:<\"[test]\"> but was:<\"[]\">", error.message)
    }

    @Test fun isEqualTo_same_value_ignore_case_passes() {
        assertThat("Test").isEqualTo("tesT", true)
    }

    @Test fun isEqualTo_different_value_ignore_case_fails() {
        val error = assertFails {
            assertThat("Test").isEqualTo("tesT", false)
        }
        assertEquals("expected:<\"[tesT]\"> but was:<\"[Test]\">", error.message)
    }

    @Test fun isEqualTo_renders_different_line_endings() {
        val error = assertFails {
            assertThat("Test\n").isEqualTo("Test\r\n")
        }
        assertEquals("expected:<\"Test[\\r]\n\"> but was:<\"Test[]\n\">", error.message)
    }

    @Test fun isEqual_renders_tabs_vs_spaces() {
        val error = assertFails {
            assertThat("\tTest").isEqualTo("    Test")
        }
        assertEquals("expected:<\"[    ]Test\"> but was:<\"[\\t]Test\">", error.message)
    }

    @Test fun isEqual_renders_newline_vs_not() {
        val error = assertFails {
            assertThat("Test\n").isEqualTo("Test")
        }
        assertEquals("expected:<\"Test[]\"> but was:<\"Test[\\n]\">", error.message)
    }
    //endregion

    //region isNotEqualTo
    @Test fun isNotEqualTo_same_value_fails() {
        val error = assertFails {
            assertThat("test").isNotEqualTo("test")
        }
        assertEquals("expected to not be equal to:<\"test\">", error.message)
    }

    @Test fun isNotEqualTo_different_value_passes() {
        assertThat("").isNotEqualTo("test")
    }

    @Test fun isNotEqualTo_same_value_ignore_case_fails() {
        val error = assertFails {
            assertThat("Test").isNotEqualTo("tesT", true)
        }
        assertEquals("expected:<\"tesT\"> not to be equal to (ignoring case):<\"Test\">", error.message)
    }

    @Test fun isNotEqualTo_different_value_ignore_case_passes() {
        assertThat("Test").isNotEqualTo("tesT", false)
    }
    //endregion

    //region contains
    @Test fun contains_value_substring_passes() {
        assertThat("test").contains("est")
    }

    @Test fun contains_value_not_substring_fails() {
        val error = assertFails {
            assertThat("test").contains("not")
        }
        assertEquals("expected to contain:<\"not\"> but was:<\"test\">", error.message)
    }

    @Test fun contains_value_substring_ignore_case_passes() {
        assertThat("Test").contains("EST", true)
    }

    @Test fun contains_value_not_substring_ignore_case_fails() {
        val error = assertFails {
            assertThat("Test").contains("EST", false)
        }
        assertEquals("expected to contain:<\"EST\"> but was:<\"Test\">", error.message)
    }
    //endregion

    //region containsSubstrings
    @Test fun containsSubstrings_value_contains_passes() {
        assertThat("test").containsSubstrings("es", "st")
    }

    @Test fun containsSubstrings_list_contains_passes() {
        assertThat("test").containsSubstrings(listOf("es", "st"))
    }

    @Test fun containsSubstrings_value_not_contains_fails() {
        val error = assertFails {
            assertThat("test").containsSubstrings("not")
        }
        assertEquals("expected to contain:<\"not\"> but was:<\"test\">", error.message)
    }

    @Test fun containsSubstrings_value_contains_ignore_case_passes() {
        assertThat("Test").containsSubstrings("EST", ignoreCase = true)
    }

    @Test fun containsSubstrings_value_not_contains_ignore_case_fails() {
        val error = assertFails {
            assertThat("Test").containsSubstrings("EST", ignoreCase = false)
        }
        assertEquals("expected to contain:<\"EST\"> but was:<\"Test\">", error.message)
    }
    //endregion


    //region doesNotContain
    @Test fun doesNotContain_value_not_substring_passes() {
        assertThat("test").doesNotContain("not")
    }

    @Test fun doesNotContain_value_substring_fails() {
        val error = assertFails {
            assertThat("test").doesNotContain("est")
        }
        assertEquals("expected to not contain:<\"est\">", error.message)
    }

    @Test fun doesNotContain_value_substring_ignore_case_fails() {
        val error = assertFails {
            assertThat("Test").doesNotContain("EST", true)
        }
        assertEquals("expected to not contain:<\"EST\">", error.message)
    }

    @Test fun doesNotContain_value_not_substring_ignore_case_passes() {
        assertThat("Test").doesNotContain("EST", false)
    }
    //endregion

    //region startsWith
    @Test fun startsWith_value_prefix_passes() {
        assertThat("test").startsWith("te")
    }

    @Test fun startsWith_value_not_prefix_fails() {
        val error = assertFails {
            assertThat("test").startsWith("st")
        }
        assertEquals("expected to start with:<\"st\"> but was:<\"test\">", error.message)
    }

    @Test fun startsWith_value_prefix_ignore_case_passes() {
        assertThat("test").startsWith("TE", true)
    }

    @Test fun startsWith_value_not_prefix_ignore_case_fails() {
        val error = assertFails {
            assertThat("test").startsWith("TE", false)
        }
        assertEquals("expected to start with:<\"TE\"> but was:<\"test\">", error.message)
    }
    //endregion

    //region endsWith
    @Test fun endsWith_value_suffix_passes() {
        assertThat("test").endsWith("st")
    }

    @Test fun endsWith_value_not_suffix_fails() {
        val error = assertFails {
            assertThat("test").endsWith("te")
        }
        assertEquals("expected to end with:<\"te\"> but was:<\"test\">", error.message)
    }

    @Test fun endsWith_value_suffix_ignore_case_passes() {
        assertThat("test").endsWith("ST", true)
    }

    @Test fun endsWith_value_not_suffix_ignore_case_passes() {
        val error = assertFails {
            assertThat("test").endsWith("ST", false)
        }
        assertEquals("expected to end with:<\"ST\"> but was:<\"test\">", error.message)
    }
    //endregion

    //region hasLineCount
    @Test fun hasLineCount_correct_value_passes() {
        assertThat("").hasLineCount(1)
        assertThat("test test").hasLineCount(1)
        assertThat("test test\ntest test").hasLineCount(2)
        assertThat("test test\r\ntest test").hasLineCount(2)
        assertThat("test test\rtest test").hasLineCount(2)
    }

    @Test fun hasLineCount_wrong_value_fails() {
        val error = assertFails {
            assertThat("test test").hasLineCount(2)
        }
        assertEquals("expected to have line count:<2> but was:<1>", error.message)
    }
    //endregion

    //region matches
    @Test fun matches_matching_value_passes() {
        assertThat("1234").matches(Regex("\\d\\d\\d\\d"))
    }

    @Test fun matches_not_matching_value_fails() {
        val regex = Regex("\\d\\d\\d\\d")
        val error = assertFails {
            assertThat("12345").matches(regex)
        }
        assertEquals("expected to match:${show(regex)} but was:<\"12345\">", error.message)
    }
    //endregion
}
