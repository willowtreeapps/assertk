package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CharSequenceTest {
    //region props
    @Test fun extracts_length() {
        assertEquals(4, assertThat("test").length().valueOrFail)
    }
    //endregion

    //region isEmpty
    @Test fun isEmpty_empty_passes() {
        assertThat("").isEmpty()
    }

    @Test fun isEmpty_non_empty_fails() {
        val error = assertFails {
            assertThat("test").isEmpty()
        }
        assertEquals("expected to be empty but was:<\"test\">", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test fun isNotEmpty_non_empty_passes() {
        assertThat("test").isNotEmpty()
    }

    @Test fun isNotEmpty_empty_fails() {
        val error = assertFails {
            assertThat("").isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region isNullOrEmpty
    @Test fun isNullOrEmpty_null_passes() {
        assertThat(null as CharSequence?).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_empty_passes() {
        assertThat("").isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_non_empty_fails() {
        val error = assertFails {
            assertThat("test").isNullOrEmpty()
        }
        assertEquals("expected to be null or empty but was:<\"test\">", error.message)
    }
    //endregion

    //region hasLength
    @Test fun hasLength_correct_length_passes() {
        assertThat("test").hasLength(4)
    }

    @Test fun hasLength_wrong_length_fails() {
        val error = assertFails {
            assertThat("test").hasLength(0)
        }
        assertEquals("expected [length]:<[0]> but was:<[4]> (\"test\")", error.message)
    }
    //endregion

    //region hasSameLengthAs
    @Test fun hasSameLengthAs_same_length_passes() {
        assertThat("test").hasSameLengthAs("four")
    }

    @Test fun hasSameLengthAs_different_length_fails() {
        val error = assertFails {
            assertThat("test").hasSameLengthAs("")
        }
        assertEquals("expected to have same length as:<\"\"> (0) but was:<\"test\"> (4)", error.message)
    }
    //endregion
}