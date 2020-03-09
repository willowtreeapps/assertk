package test.assertk

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.fail
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse

class AssertTest {
    //region transform
    @Test fun transform_that_throws_always_fails_assertion() {
        val error = assertFails {
            assertAll {
                assertThat(0).transform { fail("error") }.isEqualTo(0)
            }
        }

        assertEquals("error", error.message)
    }

    @Test fun transform_does_not_run_after_throws() {
        var run = false
        val error = assertFails {
            assertAll {
                assertThat(0).transform { fail("error") }.transform { run = true }.isEqualTo(0)
            }
        }

        assertEquals("error", error.message)
        assertFalse(run)
    }

    @Test fun transform_rethrows_thrown_exception() {
        val error = assertFails {
            assertAll {
                assertThat(0).transform { throw MyException("error") }.isEqualTo(0)
            }
        }

        assertEquals(MyException::class, error::class)
        assertEquals("error", error.message)
    }
    //endregion

    //region given
    @Test fun given_rethrows_thrown_exception() {
        val error = assertFails {
            assertAll {
                assertThat(0).given { throw MyException("error") }
            }
        }

        assertEquals(MyException::class, error::class)
        assertEquals("error", error.message)
    }
    //endregion

    //region assertThat
    @Test fun assertThat_inherits_name_of_parent_assertion_by_default() {
        val error = assertFails {
            assertThat(0, name = "test").assertThat(1).isEqualTo(2)
        }

        assertEquals("expected [test]:<[2]> but was:<[1]> (0)", error.message)
    }

    @Test fun assertThat_on_failing_assert_is_ignored() {
        val error = assertFails {
            assertAll {
                assertThat(0, name = "test").transform { fail("error") }.assertThat(1, name = "ignored").isEqualTo(2)
            }
        }

        assertEquals("error", error.message)
    }
    //endregion

    private class MyException(message: String) : Exception(message)
}