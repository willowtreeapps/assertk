package test.assertk

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.tableOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TableTest {
    @Test fun no_failures_runs_for_each_row_and_passes() {
        var invokeCount = 0
        tableOf("a", "b")
            .row(1, 1)
            .row(2, 2)
            .forAll { a, b ->
                assert(a).isEqualTo(b)
                invokeCount += 1
            }

        assertEquals(2, invokeCount)
    }

    @Test fun single_failure_fails_row() {
        var invokeCount = 0
        val error = assertFails {
            tableOf("a", "b")
                .row(1, 1)
                .row(2, 3)
                .forAll { a, b ->
                    invokeCount += 1
                    assert(a).isEqualTo(b)
                }
        }

        assertEquals(2, invokeCount)
        assertEquals(
            """The following assertion failed
              |${"\t"}on row:(a=<2>,b=<3>)
              |${"\t"}expected:<[3]> but was:<[2]>
            """.trimMargin(),
            error.message
        )
    }

    @Test fun multiple_failures_fails_with_all() {
        var invokeCount = 0
        val error = assertFails {
            tableOf("a", "b")
                .row(1, 2)
                .row(2, 3)
                .forAll { a, b ->
                    invokeCount += 1
                    assert(a).isEqualTo(b)
                }
        }

        assertEquals(2, invokeCount)
        assertEquals(
            """The following assertions failed (2 failures)
              |${"\t"}on row:(a=<1>,b=<2>)
              |${"\t"}expected:<[2]> but was:<[1]>
              |
              |${"\t"}on row:(a=<2>,b=<3>)
              |${"\t"}expected:<[3]> but was:<[2]>
            """.trimMargin(),
            error.message
        )
    }
}