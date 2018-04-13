package test.assertk

import kotlin.test.Test
import kotlin.test.assertTrue


class CatchSpec_an_exception_On_catch {
    private val subject = TestException()
    @Test
    fun it_should_catch_and_return_a_exception() {
        Assertions.assertThatThrownBy { throw subject }
                .isInstanceOf(TestException::class)
                .hasMessage("test")
    }

    @Test
    fun it_should_throw_exception_when_no_exception_is_thrown() {
        var exceptionThrown = false
        try {
            Assertions.assertThatThrownBy { }
        } catch (e: AssertionError) {
            exceptionThrown = true
        }
        assertTrue(exceptionThrown,"Assertion error should be thrown")
    }
}

private class TestException : Exception("test")

