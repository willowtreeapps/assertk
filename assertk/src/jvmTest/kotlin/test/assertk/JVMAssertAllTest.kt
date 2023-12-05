package test.assertk

import assertk.all
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.*
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class JVMAssertAllTest {
    @Test fun assertAll_is_thread_safe() {
        runOnMultipleThreads {
            assertAll {
                assertThat("one").isEqualTo("one")
                assertThat("two").isEqualTo("two")
            }
        }
    }

    @Test fun all_is_thread_safe() {
        runOnMultipleThreads {
            assertThat("one").all {
                contains("o")
                doesNotContain("f")
            }
        }
    }

    @Test fun assertAll_includes_exceptions_as_suppressed() {
        val error = assertFailsWith<AssertionError> {
            assertAll {
                assertThat(1).isEqualTo(2)
                assertThat(2).isEqualTo(1)
            }
        }
        assertEquals(2, error.suppressed.size)
        assertEquals("expected:<[2]> but was:<[1]>", error.suppressed[0].message)
        assertEquals("expected:<[1]> but was:<[2]>", error.suppressed[1].message)
    }

    @Test fun assertAll_does_not_catch_out_of_memory_errors() {
        assertFailsWith<OutOfMemoryError> {
            assertAll {
                throw OutOfMemoryError()
            }
        }
    }

    @Test fun assertAll_does_not_catch_out_of_memory_errors_in_nested_assert() {
        var runs = false
        assertFailsWith<OutOfMemoryError> {
            assertAll {
                assertThat(1).given { throw OutOfMemoryError() }
                runs = true
            }
        }
        assertFalse(runs)
    }
}

private fun runOnMultipleThreads(f: () -> Unit) {
    val executor = Executors.newFixedThreadPool(20)
    for (i in 0..100) {
        executor.submit {
            f()
        }
    }
    executor.shutdown()
    executor.awaitTermination(1, TimeUnit.SECONDS)
}
