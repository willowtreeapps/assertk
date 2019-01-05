package test.assertk

import assertk.assertThat
import assertk.assertions.isPositive
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class JVMAssertBlockTest {

    private val errorSubject = assertThat { throw Exception("test") }

    //region thrown error
    @Test fun returnedValue_exception_fails_with_stacktrace() {
        val error = assertFails {
            errorSubject.returnedValue {
                isPositive()
            }
        }

        assertNotNull(error.message)
        val errorLines = error.message!!.split("\n")
        assertTrue(errorLines.size > 1)
        assertEquals("expected value but threw:<${exceptionPackageName}Exception: test>", errorLines[0])
        assertEquals("${exceptionPackageName}Exception: test", errorLines[1])
    }


    @Test fun doesNotThrowAnyException_exception_fails_with_stacktrace() {
        val error = assertFails {
            errorSubject.doesNotThrowAnyException()
        }

        assertNotNull(error.message)
        val errorLines = error.message!!.split("\n")
        assertTrue(errorLines.size > 1)
        assertEquals("expected to not throw an exception but threw:<${exceptionPackageName}Exception: test>", errorLines[0])
        assertEquals("${exceptionPackageName}Exception: test", errorLines[1])
    }
    //endregion
}