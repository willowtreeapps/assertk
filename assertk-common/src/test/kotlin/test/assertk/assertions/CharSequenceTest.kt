package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CharSequenceTest {
    //region props
    @Test fun extracts_length() {
        assertEquals(4, assert("test").length().actual)
    }
    //endregion

    //region isEmpty
    @Test fun isEmpty_empty_passes() {
        assert("").isEmpty()
    }

    @Test fun isEmpty_non_empty_fails() {
        val error = assertFails {
            assert("test").isEmpty()
        }
        assertEquals("expected to be empty but was:<\"test\">", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test fun isNotEmpty_non_empty_passes() {
        assert("test").isNotEmpty()
    }

    @Test fun isNotEmpty_empty_fails() {
        val error = assertFails {
            assert("").isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region isNullOrEmpty
    @Test fun isNullOrEmpty_null_passes() {
        assert(null as CharSequence?).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_empty_passes() {
        assert("").isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_non_empty_fails() {
        val error = assertFails {
            assert("test").isNullOrEmpty()
        }
        assertEquals("expected to be null or empty but was:<\"test\">", error.message)
    }
    //endregion

    //region hasLength
    @Test fun hasLength_correct_length_passes() {
        assert("test").hasLength(4)
    }

    @Test fun hasLength_wrong_length_fails() {
        val error = assertFails {
            assert("test").hasLength(0)
        }
        assertEquals("expected [length]:<[0]> but was:<[4]> (\"test\")", error.message)
    }
    //endregion

    //region hasSameLengthAs
    @Test fun hasSameLengthAs_same_length_passes() {
        assert("test").hasSameLengthAs("four")
    }

    @Test fun hasSameLengthAs_different_length_fails() {
        val error = assertFails {
            assert("test").hasSameLengthAs("")
        }
        assertEquals("expected to have same length as:<\"\"> (0) but was:<\"test\"> (4)", error.message)
    }
    //endregion
}