package test.assertk

import assertk.catch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CatchTest {
    @Test fun catchs_and_returns_an_exception() {
        val error = catch { throw TestException() }
        assertEquals("test", error?.message)
    }

    @Test fun returns_null_when_no_exception_is_thrown() {
        val error = catch { }
        assertNull(error)
    }

    private class TestException : Exception("test")
}
