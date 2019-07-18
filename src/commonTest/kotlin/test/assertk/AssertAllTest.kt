package test.assertk

import assertk.*
import assertk.assertions.*
import assertk.assertions.support.show
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class AssertAllTest {
    //region all
    @Test fun all_multiple_successful_passes() {
        assertThat("test").all {
            startsWith("t")
            endsWith("t")
        }
    }

    @Test fun all_one_failure_fails() {
        val error = assertFails {
            assertThat("test", name = "test").all {
                startsWith("t")
                endsWith("g")
            }
        }
        assertEquals(
            "expected [test] to end with:<\"g\"> but was:<\"test\">",
            error.message
        )
    }

    @Test fun all_both_failures_fails_with_both() {
        val error = assertFails {
            assertThat("test", name = "test").all {
                startsWith("w")
                endsWith("g")
            }
        }
        assertEquals(
            """The following assertions failed (2 failures)
              |${"\t"}expected [test] to start with:<"w"> but was:<"test">
              |${"\t"}expected [test] to end with:<"g"> but was:<"test">
            """.trimMargin(),
            error.message
        )
    }
    //endregion

    //region assertAll
    @Test fun assertAll_multiple_successful_passes() {
        assertAll {
            assertThat("test1", name = "test1").isEqualTo("test1")
            assertThat("test2", name = "test2").isEqualTo("test2")
        }
    }

    @Test fun assertAll_one_failure_fails() {
        val error = assertFails {
            assertAll {
                assertThat("test1", name = "test1").isEqualTo("wrong1")
                assertThat("test2", name = "test2").isEqualTo("test2")
            }
        }
        assertEquals(
            "expected [test1]:<\"[wrong]1\"> but was:<\"[test]1\">",
            error.message
        )
    }

    @Test fun assertAll_both_failures_fails_with_both() {
        val error = assertFails {
            assertAll {
                assertThat("test1", name = "test1").isEqualTo("wrong1")
                assertThat("test2", name = "test2").isEqualTo("wrong2")
            }
        }
        assertEquals(
            """The following assertions failed (2 failures)
              |${"\t"}expected [test1]:<"[wrong]1"> but was:<"[test]1">
              |${"\t"}expected [test2]:<"[wrong]2"> but was:<"[test]2">
            """.trimMargin(),
            error.message
        )
    }

    @Test fun leaves_soft_assert_scope_properly_on_exception() {
        val error = assertFails {
            try {
                assertThat("This").all {
                    throw AssertionError()
                }
            } catch (e: Throwable) {
            }
            fail(AssertionError("Fail"))
        }
        assertEquals("Fail", error.message)
    }

    @Test fun assertAll_fails_multiple_block_thrownError_assertions() {
        val error = assertFails {
            assertAll {
                assertThat { 1 + 1 }.isFailure()
                assertThat { 2 + 3 }.isFailure()
            }
        }
        assertEquals(
            """The following assertions failed (2 failures)
              |${"\t"}expected failure but was success:<2>
              |${"\t"}expected failure but was success:<5>
            """.trimMargin(),
            error.message
        )
    }

    @Test fun assertAll_fails_multiple_block_returnedValue_assertions() {
        val error = assertFails {
            assertAll {
                assertThat { throw Exception("error1") }.isSuccess()
                assertThat { throw Exception("error2") }.isSuccess()
            }
        }
        assertEquals(
            "The following assertions failed (2 failures)",
            error.message!!.lineSequence().first()
        )
        assertTrue(error.message!!.contains("\texpected success but was failure:${show(Exception("error1"))}"))
        assertTrue(error.message!!.contains("\texpected success but was failure:${show(Exception("error2"))}"))
    }

    @Test fun assertAll_fails_multiple_block_doesNotThrowAnyException_assertions() {
        val error = assertFails {
            assertAll {
                assertThat { throw Exception("error1") }.isSuccess()
                assertThat { throw Exception("error2") }.isSuccess()
            }
        }
        assertEquals(
            "The following assertions failed (2 failures)".trimMargin(),
            error.message!!.lineSequence().first()
        )
        assertTrue(error.message!!.contains("\texpected success but was failure:${show(Exception("error1"))}"))
        assertTrue(error.message!!.contains("\texpected success but was failure:${show(Exception("error2"))}"))
    }
    //endregion

}
