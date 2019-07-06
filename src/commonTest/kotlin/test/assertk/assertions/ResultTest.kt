@file:Suppress("DEPRECATION")

package test.assertk.assertions

import assertk.Result
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import assertk.returnedValue
import test.assertk.exceptionPackageName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ResultTest {
    // region success
    @Test fun success_passes() {
        assertThat(Result.success(1)).returnedValue {  }
    }

    @Test fun success_fails() {
        val error = assertFails {
            assertThat(Result.failure<String>(Exception("error"))).isSuccess()
        }
        assertEquals(
            "expected success but was failure:<${exceptionPackageName}Exception: error>",
            error.message!!.lineSequence().first()
        )
    }

    @Test fun chained_success_passes() {
        assertThat(Result.success(1)).isSuccess().isEqualTo(1)
    }

    @Test fun chained_success_fails() {
        val error = assertFails {
            assertThat(Result.success(1)).isSuccess().isEqualTo(2)
        }
        assertEquals("expected:<[2]> but was:<[1]> (Success(1))", error.message)
    }
    //endregion

    //region failure
    @Test fun failure_passes() {
        assertThat(Result.failure<String>(Exception("error"))).isFailure()
    }

    @Test fun failure_fails() {
        val error = assertFails {
            assertThat(Result.success(1)).isFailure()
        }
        assertEquals("expected failure but was success:<1>", error.message)
    }

    @Test fun chained_failure_passes() {
        assertThat(Result.failure<String>(Exception("error"))).isFailure().hasMessage("error")
    }

    @Test fun chained_failure_fails() {
        val error = assertFails {
            assertThat(Result.failure<String>(Exception("error"))).isFailure().hasMessage("wrong")
        }
        assertEquals(
            "expected [message]:<\"[wrong]\"> but was:<\"[error]\"> (Failure(${exceptionPackageName}Exception: error))",
            error.message
        )
    }
    //endregion
}