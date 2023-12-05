package test.assertk

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.fail
import kotlin.test.*

class AssertTest {
    //region transform
    @Test
    fun transform_that_throws_always_fails_assertion() {
        val error = assertFailsWith<AssertionError> {
            assertAll {
                assertThat(0).transform { fail("error") }.isEqualTo(0)
            }
        }

        assertEquals("error", error.message)
    }

    @Test
    fun transform_does_not_run_after_throws() {
        var run = false
        val error = assertFailsWith<AssertionError> {
            assertAll {
                assertThat(0).transform { fail("error") }.transform { run = true }.isEqualTo(0)
            }
        }

        assertEquals("error", error.message)
        assertFalse(run)
    }

    @Test
    fun transform_rethrows_thrown_exception() {
        val error = assertFailsWith<MyException> {
            assertAll {
                assertThat(0).transform { throw MyException("error") }.isEqualTo(0)
            }
        }

        assertEquals("error", error.message)
    }
    //endregion

    //region given
    @Test
    fun given_rethrows_thrown_exception() {
        val error = assertFailsWith<MyException> {
            assertAll {
                assertThat(0).given { throw MyException("error") }
            }
        }

        assertEquals("error", error.message)
    }
    //endregion

    //region assertThat
    @Test
    fun assertThat_inherits_name_of_parent_assertion_by_default() {
        val error = assertFailsWith<AssertionError> {
            assertThat(0, name = "test").assertThat(1).isEqualTo(2)
        }

        assertEquals("expected [test]:<[2]> but was:<[1]> (0)", error.message)
    }

    @Test
    fun assertThat_failing_transformed_assert_shows_original_by_displayActual_lambda() {
        val error = assertFailsWith<AssertionError> {
            assertThat(0, name = "test", displayActual = { "Number:${it}" })
                .assertThat(1).isEqualTo(2)
        }

        assertEquals("expected [test]:<[2]> but was:<[1]> (Number:0)", error.message)
    }

    @Test
    fun assertThat_on_failing_assert_is_ignored() {
        val error = assertFailsWith<AssertionError> {
            assertAll {
                assertThat(0, name = "test").transform { fail("error") }.assertThat(1, name = "ignored").isEqualTo(2)
            }
        }

        assertEquals("error", error.message)
    }
    //endregion

    private class MyException(message: String) : Exception(message)
}
