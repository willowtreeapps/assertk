package test.assertk

import assertk.all
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.*
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse

class JVMAssertAllTest {
    @Test fun assert_all_is_thread_safe() {
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

    @Test fun assert_all_includes_exceptions_as_suppressed() {
        val error = assertFails {
            assertAll {
                assertThat(1).isEqualTo(2)
                assertThat(2).isEqualTo(1)
            }
        }
        assertEquals(2, error.suppressed.size)
        assertEquals("expected:<[2]> but was:<[1]>", error.suppressed[0].message)
        assertEquals("expected:<[1]> but was:<[2]>", error.suppressed[1].message)
    }

    @Test fun assert_all_does_not_catch_out_of_memory_errors() {
        var runs = false
        val error = assertFails {
            assertAll {
                assertThat(1).given { throw OutOfMemoryError() }
                runs = true
            }
        }
        assertEquals(OutOfMemoryError::class, error::class)
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
