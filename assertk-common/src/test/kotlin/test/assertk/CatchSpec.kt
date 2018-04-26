package test.assertk

import assertk.catch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CatchSpec_an_exception_On_catch {
    private val subject = TestException()

    @Test
    fun it_should_catch_and_return_a_exception() {
        val error = catch { throw subject }

        assertEquals("test", error?.message)
    }

    @Test
    fun it_should_return_null_when_no_exception_is_thrown() {
        val error = catch { }
        assertNull(error)
    }

    private class TestException : Exception("test")
}
