@file:Suppress("DEPRECATION")

package test.assertk

import assertk.Result
import assertk.assertThat
import test.assertk.assertions.valueOrFail
import kotlin.test.Test
import kotlin.test.assertEquals

class AssertLambdaTest {

    @Test fun successful_assert_lambda_returns_success() {
        assertEquals(Result.success(2), assertThat { 1 + 1 }.valueOrFail)
    }

    @Test fun failing_assert_lambda_returns_failure() {
        val e = Exception("error")
        assertEquals(Result.failure<String>(e), assertThat { throw e }.valueOrFail)
    }
}