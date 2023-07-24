package test.assertk

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import test.assertk.assertions.valueOrFail
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("DEPRECATION_ERROR")
class AssertLambdaTest {

    @Test fun successful_assert_lambda_returns_success() {
        assertEquals(Result.success(2), assertThat { 1 + 1 }.valueOrFail)
    }

    @Test fun successful_assert_lambda_returning_null_returns_success() {
        assertEquals(Result.success(null), assertThat { null }.valueOrFail)
    }

    @Test fun failing_assert_lambda_returns_failure() {
        val e = Exception("error")
        assertEquals(Result.failure<String>(e), assertThat { throw e }.valueOrFail)
    }
    @Test fun returnedValue_works_in_coroutine_test() = runTest {
        assertThat {
            asyncReturnValue()
        }.isSuccess().isEqualTo(1)
    }

    @Test fun returnedValue_exception_works_in_coroutine_test() = runTest {
        assertThat {
            asyncThrows()
        }.isFailure().hasMessage("test")
    }

    @Suppress("RedundantSuspendModifier")
    private suspend fun asyncReturnValue(): Int {
        return 1
    }

    @Suppress("RedundantSuspendModifier")
    private suspend fun asyncThrows() {
        throw  Exception("test")
    }
}