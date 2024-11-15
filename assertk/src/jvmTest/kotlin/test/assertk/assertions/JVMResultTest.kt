package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.isSuccess
import test.assertk.exceptionPackageName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class JVMResultTest {

    @Test
    fun failure_result_fails_with_stacktrace() {
        val error = assertFailsWith<AssertionError> {
            assertThat(Result.failure<String>(Exception("test"))).isSuccess()
        }

        assertNotNull(error.message)
        val errorLines = error.message!!.lines()
        assertTrue(errorLines.size > 1)
        assertEquals("expected success but was failure:<${exceptionPackageName}Exception: test>", errorLines[0])
        assertEquals("${exceptionPackageName}Exception: test", errorLines[1])
    }

}
