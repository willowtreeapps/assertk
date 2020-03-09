package test.assertk

import assertk.all
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.*
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

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
