package test.assertk

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isPositive
import assertk.tableOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails


class TableSpec_a_table_On_one_column_with_one_row {
    @Test
    fun it_should_run_once_and_pass_a_successful_test() {
        var invokeCount = 0
        tableOf("a")
            .row(1)
            .forAll { a ->
                assert(a).isEqualTo(1)
                invokeCount += 1
            }

        assertEquals(1, invokeCount)
    }

    @Test
    fun it_should_fail_showing_the_failing_assertion() {
        val error = assertFails {
            tableOf("a")
                .row(1)
                .forAll { a ->
                    assert(a).isEqualTo(2)
                }
        }
        assertEquals("The following assertion failed:\non row:(a=<1>)\n- expected:<[2]> but was:<[1]>", error.message)
    }
}

class TableSpec_a_table_On_one_column_with_two_rows {
    @Test
    fun it_should_run_twice_and_pass_a_successful_test() {
        var invokeCount = 0
        tableOf("a")
            .row(1)
            .row(2)
            .forAll { a ->
                assert(a).isPositive()
                invokeCount += 1
            }

        assertEquals(2, invokeCount)
    }

    @Test
    fun it_should_fail_showing_the_failing_assertions() {
        val error = assertFails {
            tableOf("a")
                .row(1)
                .row(2)
                .forAll { a ->
                    assert(a).isEqualTo(3)
                }
        }
        assertEquals(
            "The following 2 assertions failed:\non row:(a=<1>)\n- expected:<[3]> but was:<[1]>\n\non row:(a=<2>)\n- expected:<[3]> but was:<[2]>",
            error.message
        )
    }
}

class TableSpec_a_table_On_two_columns_with_one_row {
    @Test
    fun it_should_run_once_and_pass_a_successful_test() {
        var invokeCount = 0
        tableOf("a", "b")
            .row(1, 1)
            .forAll { a, b ->
                assert(a).isEqualTo(b)
                invokeCount += 1
            }

        assertEquals(1, invokeCount)
    }

    @Test
    fun it_should_fail_showing_the_failing_assertion() {
        val error = assertFails {
            tableOf("a", "b")
                .row(1, 2)
                .forAll { a, b ->
                    assert(a + b).isEqualTo(2)
                }
        }
        assertEquals(
            "The following assertion failed:\non row:(a=<1>,b=<2>)\n- expected:<[2]> but was:<[3]>",
            error.message
        )
    }
}


