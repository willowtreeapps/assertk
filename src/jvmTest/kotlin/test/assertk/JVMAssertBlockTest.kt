package test.assertk

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isPositive
import assertk.catch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class JVMAssertBlockTest {

    private val errorSubject = assertThat { throw Exception("test") }

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
        assertEquals(
            "expected to not throw an exception but threw:<${exceptionPackageName}Exception: test>",
            errorLines[0]
        )
        assertEquals("${exceptionPackageName}Exception: test", errorLines[1])
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    @Test fun returnedValue_works_in_coroutine_test() {
        runBlockingTest {
            assertThat {
                asyncReturnValue()
            }.returnedValue { isEqualTo(1) }
        }
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    @Test fun returnedValue_exception_works_in_coroutine_test() {
        runBlockingTest {
            assertThat {
                asyncThrows()
            }.thrownError { hasMessage("test") }
        }
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    @Test fun catch_works_in_coroutine_test() {
        runBlockingTest {
            val error = catch {
                asyncThrows()
            }
            assertThat(error).isNotNull().hasMessage("test")
        }
    }

    private suspend fun asyncReturnValue(): Int {
        delay(10000)
        return 1
    }

    private suspend fun asyncThrows() {
        delay(10000)
        throw  Exception("test")
    }
}