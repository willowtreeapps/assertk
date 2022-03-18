package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import test.assertk.opentestPackageName
import assertk.assertions.support.show
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

$T:$N:$E = ByteArray:byteArray:Byte, IntArray:intArray:Int, ShortArray:shortArray:Short, LongArray:longArray:Long, FloatArray:floatArray:Float, DoubleArray:doubleArray:Double, CharArray:charArray:Char

class $TTest {
    //region isEqualTo
    @Test fun isEqualTo_same_contents_passes() {
        assertThat($NOf(0.to$E())).isEqualTo($NOf(0.to$E()))
    }

//    @Test fun isEqualTo_different_contents_fails() {
//        val error = assertFailsWith<AssertionError> {
//            assertThat($NOf(97.to$E())).isEqualTo($NOf(98.to$E()))
//        }
//        assertEquals("expected:<[[${show(98.to$E(), "")}]]> but was:<[[${show(97.to$E(), "")}]]>", error.message)
//    }
    //endregion

    //region isNotEqualTo
    @Test fun isNotEqualTo_different_contents_passes() {
        assertThat($NOf(0.to$E())).isNotEqualTo($NOf(1.to$E()))
    }

    @Test fun isNotEqualTo_same_contents_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat($NOf(0.to$E())).isNotEqualTo($NOf(0.to$E()))
        }
        assertEquals("expected to not be equal to:<[${show(0.to$E(), "")}]>", error.message)
    }
    //endregion

    //region isEmpty
    @Test fun isEmpty_empty_passes() {
        assertThat($NOf()).isEmpty()
    }

    @Test fun isEmpty_non_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat($NOf(0.to$E())).isEmpty()
        }
        assertEquals("expected to be empty but was:<[${show(0.to$E(), "")}]>", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test fun isNotEmpty_non_empty_passes() {
        assertThat($NOf(0.to$E())).isNotEmpty()
    }

    @Test fun isNotEmpty_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat($NOf()).isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region isNullOrEmpty
    @Test fun isNullOrEmpty_null_passes() {
        assertThat(null as $T?).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_empty_passes() {
        assertThat($NOf()).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_non_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat($NOf(0.to$E())).isNullOrEmpty()
        }
        assertEquals("expected to be null or empty but was:<[${show(0.to$E(), "")}]>", error.message)
    }
    //endregion

    //region hasSize
    @Test fun hasSize_correct_size_passes() {
        assertThat($NOf()).hasSize(0)
    }

    @Test fun hasSize_wrong_size_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat($NOf()).hasSize(1)
        }
        assertEquals("expected [size]:<[1]> but was:<[0]> ([])", error.message)
    }
    //endregion

    //region hasSameSizeAs
    @Test fun hasSameSizeAs_equal_sizes_passes() {
        assertThat($NOf()).hasSameSizeAs($NOf())
    }

    @Test fun hasSameSizeAs_non_equal_sizes_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat($NOf()).hasSameSizeAs($NOf(0.to$E()))
        }
        assertEquals("expected to have same size as:<[${show(0.to$E(), "")}]> (1) but was size:(0)", error.message)
    }
    //endregion

    //region each
    @Test fun each_empty_list_passes() {
        assertThat($NOf()).each { it.isEqualTo(1) }
    }

    @Test fun each_content_passes() {
        assertThat($NOf(1.to$E(), 2.to$E())).each { it.isGreaterThan(0.to$E()) }
    }

    @Test fun each_non_matching_content_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat($NOf(1.to$E(), 2.to$E(), 3.to$E())).each { it.isLessThan(2.to$E()) }
        }
        assertEquals(
            """The following assertions failed (2 failures)
                |	${opentestPackageName}AssertionFailedError: expected [[1]] to be less than:<${show(2.to$E(), "")}> but was:<${show(2.to$E(), "")}> ([${show(1.to$E(), "")}, ${show(2.to$E(), "")}, ${show(3.to$E(), "")}])
                |	${opentestPackageName}AssertionFailedError: expected [[2]] to be less than:<${show(2.to$E(), "")}> but was:<${show(3.to$E(), "")}> ([${show(1.to$E(), "")}, ${show(2.to$E(), "")}, ${show(3.to$E(), "")}])
            """.trimMargin().lines(), error.message!!.lines()
        )
    }
    //endregion

    //region index
    @Test fun index_successful_assertion_passes() {
        assertThat($NOf(1.to$E(), 2.to$E()), name = "subject").index(0).isEqualTo(1.to$E())
    }

    @Test fun index_unsuccessful_assertion_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat($NOf(1.to$E(), 2.to$E()), name = "subject").index(0).isGreaterThan(2.to$E())
        }
        assertEquals(
            "expected [subject[0]] to be greater than:<${show(2.to$E(), "")}> but was:<${show(1.to$E(), "")}> ([${show(1.to$E(), "")}, ${show(2.to$E(), "")}])",
            error.message
        )
    }

    @Test fun index_out_of_range_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat($NOf(1.to$E(), 2.to$E()), name = "subject").index(-1).isEqualTo(listOf(1.to$E()))
        }
        assertEquals("expected [subject] index to be in range:[0-2) but was:<-1>", error.message)
    }
    //endregion
}
