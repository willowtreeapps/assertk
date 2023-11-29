package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assume
import com.willowtreeapps.opentest4k.TestAbortedException
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
}