package test.assertk.coroutines

import assertk.assertions.isEqualTo
import assertk.assertions.isNegative
import assertk.assertions.message
import assertk.assertions.support.show
import assertk.coroutines.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@UseExperimental(ExperimentalCoroutinesApi::class)
class AssertCoroutineTest {

    val returnSubject = assertThat {
        suspendAndReturn(background = coroutineContext, main = coroutineContext)
    }
    val errorSubject = assertThat { suspendAndThrow() }

    suspend fun suspendAndReturn(
        background: CoroutineContext = Dispatchers.Default,
        main: CoroutineContext = Dispatchers.Main
    ): Int {
        withContext(background) {
            delay(10000)
        }
        return withContext(main) {
            1 + 1
        }
    }

    suspend fun suspendAndThrow(): Int {
        delay(10000)
        throw Exception("test")
    }

    //region returnedValue
    @Test fun returnedValue_successful_assertion_passes() {
        returnSubject.returnedValue { isEqualTo(2) }
    }

    @Test fun returnedValue_unsuccessful_assertion_fails() {
        val error = assertFails {
            returnSubject.returnedValue { isNegative() }
        }
        assertEquals("expected to be negative but was:<2>", error.message)
    }

    @Test fun returnedValue_exception_in_block_fails() {
        val error = assertFails {
            errorSubject.returnedValue { }
        }
        assertEquals("expected value but threw:${show(Exception("test"))}", error.message!!.lineSequence().first())
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

    @Test fun doesNotThrowAnyException_exception_fails() {
        val error = assertFails {
            errorSubject.doesNotThrowAnyException()
        }

        assertEquals(
            "expected to not throw an exception but threw:${show(Exception("test"))}",
            error.message!!.lineSequence().first()
        )
    }
    //endregion
}