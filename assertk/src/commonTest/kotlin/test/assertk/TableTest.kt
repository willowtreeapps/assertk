package test.assertk

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import assertk.tableOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TableTest {
    @Test
    fun no_failures_runs_for_each_row_and_passes() {
        var invokeCount = 0
        tableOf("a", "b")
            .row(1, 1)
            .row(2, 2)
            .forAll { a, b ->
                assertThat(a).isEqualTo(b)
                invokeCount += 1
            }

        assertEquals(2, invokeCount)
    }

    @Test
    fun single_failure_fails_row() {
        var invokeCount = 0
        val error = assertFailsWith<AssertionError> {
            tableOf("a", "b")
                .row(1, 1)
                .row(2, 3)
                .forAll { a, b ->
                    assertThat(a).isEqualTo(b)
                    invokeCount += 1
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

    @Test
    fun multiple_failures_fails_with_all() {
        var invokeCount = 0
        val error = assertFailsWith<AssertionError> {
            tableOf("a", "b")
                .row(1, 2)
                .row(2, 3)
                .forAll { a, b ->
                    assertThat(a).isEqualTo(b)
                    invokeCount += 1
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

    @Test
    fun table_with_one_value_fails_row() {
        var invokeCount = 0
        val error = assertFailsWith<AssertionError> {
            tableOf("a")
                .row(false)
                .forAll { a ->
                    assertThat(a).isTrue()
                    invokeCount += 1
                }
        }

        assertEquals(1, invokeCount)
        assertEquals(
            """The following assertion failed
	          |${"\t"}on row:(a=<false>)
	          |${"\t"}expected to be true
            """.trimMargin(),
            error.message
        )
    }

    @Test
    fun table_with_three_values_fails_row() {
        var invokeCount = 0
        val error = assertFailsWith<AssertionError> {
            tableOf("a", "b", "c")
                .row(1, 2, 4)
                .forAll { a, b, c ->
                    assertThat(a + b).isEqualTo(c)
                    invokeCount += 1
                }
        }

        assertEquals(1, invokeCount)
        assertEquals(
            """The following assertion failed
              |${"\t"}on row:(a=<1>,b=<2>,c=<4>)
              |${"\t"}expected:<[4]> but was:<[3]>
            """.trimMargin(),
            error.message
        )
    }

    @Test
    fun table_with_four_values_fails_row() {
        var invokeCount = 0
        val error = assertFailsWith<AssertionError> {
            tableOf("a", "b", "c", "d")
                .row(1, 2, 3, 4)
                .forAll { a, b, c, d ->
                    assertThat(a + b + c).isEqualTo(d)
                    invokeCount += 1
                }
        }

        assertEquals(1, invokeCount)
        assertEquals(
            """The following assertion failed
              |${"\t"}on row:(a=<1>,b=<2>,c=<3>,d=<4>)
              |${"\t"}expected:<[4]> but was:<[6]>
            """.trimMargin(),
            error.message
        )
    }
}
