package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import assertk.assume
import com.willowtreeapps.opentest4k.TestAbortedException
import test.assertk.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AssumeTest {
    @Test
    fun assume_throws_TestAbortedException() {
        val error = assertFailsWith<TestAbortedException> {
            assume {
                assertThat(true).isFalse()
            }
        }

        assertEquals("Assumption failed: expected to be false", error.message)
    }

    @Test
    fun assume_aborts_instead_of_fails_test() {
        // this test should be skipped instead of failing
        assume { assertThat(true).isFalse() }
    }

    @Test
    fun assume_does_not_capture_unexpected_exceptions() {
        assertFailsWith<NullPointerException> {
            assume { throw NullPointerException() }
        }
    }

    @Test
    fun assume_aborts_when_suspend() = runTest {
        assume { assertThat(suspend { true }()).isFalse()  }
    }
}