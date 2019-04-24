package test.assertk

import assertk.fail
import com.willowtreeapps.opentest4k.*
import kotlin.test.*


class FailureTest {

    @Test fun fail_throws_assertion_failed_error_with_actual_and_expected_present_and_defined() {
        val error = assertFails {
            fail("message", "expected", "actual")
        }

        assertEquals(AssertionFailedError::class, error::class)
        error as AssertionFailedError
        assertEquals("expected" as Any?, error.expected?.value)
        assertEquals("actual" as Any?, error.actual?.value)
        assertTrue(error.isExpectedDefined)
        assertTrue(error.isActualDefined)
    }

    @Test fun fail_throws_assertion_failed_error_with_actual_and_expected_not_defined() {
        val error = assertFails {
            fail("message")
        }

        assertEquals(AssertionFailedError::class, error::class)
        error as AssertionFailedError
        assertNull(error.expected)
        assertNull(error.actual)
        assertFalse(error.isExpectedDefined)
        assertFalse(error.isActualDefined)
    }
}