package test.assertk

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isPositive
import assertk.assertions.support.show
import assertk.catch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class JSAssertBlockTest {

    private val errorSubject = assertThat { throw Exception("test") }

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

    @Test fun returnedValue_works_in_coroutine_test() = GlobalScope.promise {
        assertThat {
            asyncReturnValue()
        }.returnedValue { isEqualTo(1) }
    }

    @Test fun returnedValue_exception_works_in_coroutine_test() = GlobalScope.promise {
        assertThat {
            asyncThrows()
        }.thrownError { hasMessage("test") }
    }

    @Test fun catch_works_in_corountine_test() = GlobalScope.promise {
        val error = catch {
            asyncThrows()
        }
        assertThat(error).isNotNull().hasMessage("test")
    }

    @Suppress("RedundantSuspendModifier")
    private suspend fun asyncReturnValue(): Int {
        return 1
    }

    @Suppress("RedundantSuspendModifier")
    private suspend fun asyncThrows() {
        throw  Exception("test")
    }
}