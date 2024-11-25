package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import assertk.assertions.support.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CharSequenceTest {
    //region props
    @Test
    fun extracts_length() {
        assertEquals(4, assertThat("test").havingLength().valueOrFail)
    }
    //endregion

    //region isEmpty
    @Test
    fun isEmpty_empty_passes() {
        assertThat("").isEmpty()
    }

    @Test
    fun isEmpty_non_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").isEmpty()
        }
        assertEquals("expected to be empty but was:<\"test\">", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test
    fun isNotEmpty_non_empty_passes() {
        assertThat("test").isNotEmpty()
    }

    @Test
    fun isNotEmpty_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("").isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region isNullOrEmpty
    @Test
    fun isNullOrEmpty_null_passes() {
        assertThat(null as CharSequence?).isNullOrEmpty()
    }

    @Test
    fun isNullOrEmpty_empty_passes() {
        assertThat("").isNullOrEmpty()
    }

    @Test
    fun isNullOrEmpty_non_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").isNullOrEmpty()
        }
        assertEquals("expected to be null or empty but was:<\"test\">", error.message)
    }
    //endregion

    //region hasLength
    @Test
    fun hasLength_correct_length_passes() {
        assertThat("test").hasLength(4)
    }

    @Test
    fun hasLength_wrong_length_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").hasLength(0)
        }
        assertEquals("expected [length]:<[0]> but was:<[4]> (\"test\")", error.message)
    }
    //endregion

    //region hasSameLengthAs
    @Test
    fun hasSameLengthAs_same_length_passes() {
        assertThat("test").hasSameLengthAs("four")
    }

    @Test
    fun hasSameLengthAs_different_length_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").hasSameLengthAs("")
        }
        assertEquals("expected to have same length as:<\"\"> (0) but was:<\"test\"> (4)", error.message)
    }
    //endregion

    //region contains single
    @Test
    fun contains_value_substring_passes() {
        assertThat("test").contains("est")
    }

    @Test
    fun contains_value_not_substring_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").contains("not")
        }
        assertEquals("expected to contain:<\"not\"> but was:<\"test\">", error.message)
    }

    @Test
    fun contains_value_substring_ignore_case_passes() {
        assertThat("Test").contains("EST", true)
    }

    @Test
    fun contains_value_not_substring_ignore_case_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("Test").contains("EST", false)
        }
        assertEquals("expected to contain:<\"EST\"> but was:<\"Test\">", error.message)
    }
    //endregion

    //region contains multi
    @Test
    fun contains_empty_arg_passes() {
        assertThat("test").contains()
    }

    @Test
    fun contains_value_contains_passes() {
        assertThat("test").contains("te", "st")
    }

    @Test
    fun contains_list_contains_passes() {
        assertThat("test").contains(listOf("te", "st"))
    }

    @Test
    fun contains_contains_unordered_passes() {
        assertThat("test").contains("st", "te")
    }

    @Test
    fun contains_value_not_contains_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").contains("foo", "bar")
        }
        assertEquals("expected to contain:<[\"foo\", \"bar\"]> but was:<\"test\">", error.message)
    }

    @Test
    fun contains_value_contains_ignore_case_passes() {
        assertThat("Test").contains("te", "ST", ignoreCase = true)
    }

    @Test
    fun contains_value_not_contains_ignore_case_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("Test").contains("te", "ST", ignoreCase = false)
        }
        assertEquals("expected to contain:<[\"te\", \"ST\"]> but was:<\"Test\">", error.message)
    }
    //endregion


    //region doesNotContain single
    @Test
    fun doesNotContain_value_not_substring_passes() {
        assertThat("test").doesNotContain("not")
    }

    @Test
    fun doesNotContain_value_substring_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").doesNotContain("est")
        }
        assertEquals("expected to not contain:<\"est\"> but was:<\"test\">", error.message)
    }

    @Test
    fun doesNotContain_value_substring_ignore_case_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("Test").doesNotContain("EST", true)
        }
        assertEquals("expected to not contain:<\"EST\"> but was:<\"Test\">", error.message)
    }

    @Test
    fun doesNotContain_value_not_substring_ignore_case_passes() {
        assertThat("Test").doesNotContain("EST", false)
    }
    //endregion

    //region doesNotContain multi
    @Test
    fun doesNotContain_multivalue_not_substring_passes() {
        assertThat("test").doesNotContain("foo", "bar")
    }

    @Test
    fun doesNotContain_multivalue_substring_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").doesNotContain("te", "st")
        }
        assertEquals("expected to not contain:<[\"te\", \"st\"]> but was:<\"test\">", error.message)
    }

    @Test
    fun doesNotContain_multivalue_substring_ignore_case_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("Test").doesNotContain("TE", "ST", ignoreCase = true)
        }
        assertEquals("expected to not contain:<[\"TE\", \"ST\"]> but was:<\"Test\">", error.message)
    }

    @Test
    fun doesNotContain_multivalue_not_substring_ignore_case_passes() {
        assertThat("Test").doesNotContain("TE", "ST", ignoreCase = false)
    }
    //endregion

    //region doesNotContainMatch
    @Test
    fun doesNotContainMatch_non_matching_value_passes() {
        assertThat("abcd").doesNotContainMatch(Regex("\\d\\d\\d\\d"))
    }

    @Test
    fun doesNotContainMatch_matching_value_fails() {
        val regex = Regex("\\d\\d\\d\\d")
        val error = assertFailsWith<AssertionError> {
            assertThat("1234").doesNotContainMatch(regex)
        }
        assertEquals("expected to not contain match:${show(regex)} but was:<\"1234\">", error.message)
    }
    //endregion

    //region startsWith
    @Test
    fun startsWith_value_prefix_passes() {
        assertThat("test").startsWith("te")
    }

    @Test
    fun startsWith_value_not_prefix_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").startsWith("st")
        }
        assertEquals("expected to start with:<\"st\"> but was:<\"test\">", error.message)
    }

    @Test
    fun startsWith_value_prefix_ignore_case_passes() {
        assertThat("test").startsWith("TE", true)
    }

    @Test
    fun startsWith_value_not_prefix_ignore_case_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").startsWith("TE", false)
        }
        assertEquals("expected to start with:<\"TE\"> but was:<\"test\">", error.message)
    }
    //endregion

    //region endsWith
    @Test
    fun endsWith_value_suffix_passes() {
        assertThat("test").endsWith("st")
    }

    @Test
    fun endsWith_value_not_suffix_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").endsWith("te")
        }
        assertEquals("expected to end with:<\"te\"> but was:<\"test\">", error.message)
    }

    @Test
    fun endsWith_value_suffix_ignore_case_passes() {
        assertThat("test").endsWith("ST", true)
    }

    @Test
    fun endsWith_value_not_suffix_ignore_case_passes() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test").endsWith("ST", false)
        }
        assertEquals("expected to end with:<\"ST\"> but was:<\"test\">", error.message)
    }
    //endregion

    //region hasLineCount
    @Test
    fun hasLineCount_correct_value_passes() {
        assertThat("").hasLineCount(1)
        assertThat("test test").hasLineCount(1)
        assertThat("test test\ntest test").hasLineCount(2)
        assertThat("test test\r\ntest test").hasLineCount(2)
        assertThat("test test\rtest test").hasLineCount(2)
    }

    @Test
    fun hasLineCount_wrong_value_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat("test test").hasLineCount(2)
        }
        assertEquals("expected to have line count:<2> but was:<1>", error.message)
    }
    //endregion

    //region matches
    @Test
    fun matches_matching_value_passes() {
        assertThat("1234").matches(Regex("\\d\\d\\d\\d"))
    }

    @Test
    fun matches_not_matching_value_fails() {
        val regex = Regex("\\d\\d\\d\\d")
        val error = assertFailsWith<AssertionError> {
            assertThat("12345").matches(regex)
        }
        assertEquals("expected to match:${show(regex)} but was:<\"12345\">", error.message)
    }
    //endregion

    //region contains match
    @Test
    fun contains_match_matching_value_passes() {
        assertThat("abc1234xyz").containsMatch(Regex("\\d\\d\\d\\d"))
    }

    @Test
    fun contains_match_not_matching_value_fails() {
        val regex = Regex("\\d\\d\\d\\d")
        val error = assertFailsWith<AssertionError> {
            assertThat("abc123xyz").containsMatch(regex)
        }
        assertEquals("expected to contain match:${show(regex)} but was:<\"abc123xyz\">", error.message)
    }
    //endregion
}
