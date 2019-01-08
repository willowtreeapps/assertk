package test.assertk

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNegative
import assertk.assertions.message
import assertk.assertions.support.show
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class AssertBlockTest {
    val returnSubject = assertThat { 1 + 1 }
    val errorSubject = assertThat { throw Exception("test") }

    //region returnedValue
    @Test fun returnedValue_successful_assertion_passes() {
        returnSubject.returnedValue { isEqualTo(2) }
    }

    @Test fun returnedValue_unsuccessful_assertion_fails() {
        val error = assertFails {
            returnSubject.returnedValue {
                isNegative()
            }
        }
        assertEquals("expected to be negative but was:<2>", error.message)
    }
    //endregion

    //region thrownError
    @Test fun thrownError_successful_assertion_passes() {
        errorSubject.thrownError {
            message().isEqualTo("test")
        }
    }

    @Test fun thrownError_unsuccessful_assertion_fails() {
        val error = assertFails {
            errorSubject.thrownError {
                message().isEqualTo("wrong")
            }
        }
        assertEquals(
            "expected [message]:<\"[wrong]\"> but was:<\"[test]\"> ${show(Exception("test"), "()")}",
            error.message
        )
    }

    @Test fun thrownError_no_exception_in_block_fails() {
        val error = assertFails {
            returnSubject.thrownError {
                message().isEqualTo("error")
            }
        }
        assertEquals("expected exception but was:<2>", error.message)
    }
    //endregion

    //region doesNotThrowAnyException
    @Test fun doesNotThrowAnyException_no_exception_passes() {
        returnSubject.doesNotThrowAnyException()
    }
    //endregion
}