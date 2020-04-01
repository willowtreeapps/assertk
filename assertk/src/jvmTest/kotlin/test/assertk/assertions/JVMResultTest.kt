@file:Suppress("DEPRECATION")

package test.assertk.assertions

import assertk.Result
import assertk.assertThat
import assertk.assertions.isSuccess
import org.junit.Test
import test.assertk.exceptionPackageName
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class JVMResultTest {

    @Test fun failure_result_fails_with_stacktrace() {
        val error = assertFails {
            assertThat(Result.failure<String>(Exception("test"))).isSuccess()
        }

        assertNotNull(error.message)
        val errorLines = error.message!!.lines()
        assertTrue(errorLines.size > 1)
        assertEquals("expected success but was failure:<${exceptionPackageName}Exception: test>", errorLines[0])
        assertEquals("${exceptionPackageName}Exception: test", errorLines[1])
    }

}