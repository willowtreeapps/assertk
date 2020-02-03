package test.assertk

import assertk.assertThat
import assertk.assertions.*
import assertk.catch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.test.Test

class JSAssertLambdaTest {

    @Test fun returnedValue_works_in_coroutine_test() = GlobalScope.promise {
        assertThat {
            asyncReturnValue()
        }.isSuccess().isEqualTo(1)
    }

    @Test fun returnedValue_exception_works_in_coroutine_test() = GlobalScope.promise {
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