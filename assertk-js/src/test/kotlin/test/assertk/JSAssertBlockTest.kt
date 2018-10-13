package test.assertk

import assertk.assert
import assertk.assertions.isPositive
import assertk.assertions.support.show
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class JSAssertBlockTest {

    private val errorSubject = assert { throw Exception("test") }

    //region thrown error
    @Test fun returnedValue_exception_in_block_fails() {
        val error = assertFails {
            errorSubject.returnedValue {
                isPositive()
            }
        }
        assertEquals("expected value but threw:${show(Exception("test"))}", error.message)
    }

    @Test fun doesNotThrowAnyException_exception_fails() {
        val error = assertFails {
            errorSubject.doesNotThrowAnyException()
        }

        assertEquals(
            "expected to not throw an exception but threw:<${exceptionPackageName}Exception: test>",
            error.message
        )
    }
    //endregion
}