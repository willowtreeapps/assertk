package test.assertk

import assertk.assertFailure
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.message
import com.willowtreeapps.opentest4k.AssertionFailedError
import kotlinx.coroutines.test.runTest
import test.assertk.assertions.valueOrFail
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class AssertFailureTest {

    @Test fun failure_is_success() {
        val expected = RuntimeException()
        assertSame(expected, assertFailure { throw expected }.valueOrFail)
    }

    @Test fun failure_originating_subject_not_wrapped_in_result() {
        val t = assertFailsWith<AssertionFailedError> {
            assertFailure { throw RuntimeException("foo") }
                .message()
                .isEqualTo("bar")
        }
        assertTrue("RuntimeException" in t.message!!)
        assertFalse("Failure(" in t.message!!)
    }

    @Test fun success_is_failure() {
        val t = assertFailsWith<AssertionFailedError> {
            assertFailure { }
        }
        assertEquals("expected failure but lambda completed successfully", t.message)
    }

    @Test
    fun suspending_functions_can_succeed() = runTest {
        val t = assertFailsWith<AssertionFailedError> {
            assertFailure {
                suspendCoroutine { it.resume(Unit) }
            }
        }
        assertEquals("expected failure but lambda completed successfully", t.message)
    }

    @Test
    fun suspending_functions_fail() = runTest {
        val t = assertFailure {
            suspendCoroutine { it.resumeWithException(IllegalArgumentException()) }
        }
        t.isInstanceOf<IllegalArgumentException>()
    }
}
