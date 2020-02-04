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

