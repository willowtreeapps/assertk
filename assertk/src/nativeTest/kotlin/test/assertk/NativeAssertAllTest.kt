package test.assertk

import assertk.all
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.Future
import kotlin.native.concurrent.TransferMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse

class NativeAssertAllTest {

    @Test fun assert_all_is_thread_safe() {
        aBunchOfWokers { w ->
            w.execute(TransferMode.SAFE, { }, {
                assertAll {
                    assertThat("one").isEqualTo("one")
                    assertThat("two").isEqualTo("two")
                }
            })
        }
    }

    @Test fun all_is_thread_safe() {
        aBunchOfWokers { w ->
            w.execute(TransferMode.SAFE, { }, {
                assertThat("one").all {
                    contains("o")
                    doesNotContain("f")
                }
            })
        }
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

    fun aBunchOfWokers(f: (Worker) -> Future<Unit>) {
        val workers = Array(20, { Worker.start() })
        val futures = mutableSetOf<Future<Unit>>()
        for (i in 0..5) {
            for (w in workers) {
                futures.add(f(w))
            }
        }
        for (future in futures) { future.result }

        for (w in workers) {
            w.requestTermination().result
        }
    }
}

