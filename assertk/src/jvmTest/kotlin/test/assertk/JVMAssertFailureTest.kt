package test.assertk

import assertk.assertFailure
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.message
import com.willowtreeapps.opentest4k.AssertionFailedError
import kotlinx.coroutines.runBlocking
import test.assertk.assertions.valueOrFail
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class JVMAssertFailureTest {
    @Test
    fun can_test_suspending_functions(): Unit = runBlocking {
        val t = assertFailsWith<AssertionFailedError> {
            assertFailure {
                suspendCoroutine { it.resume(Unit) }
            }
        }
        assertEquals("expected failure but lambda completed successfully", t.message)
    }
}
