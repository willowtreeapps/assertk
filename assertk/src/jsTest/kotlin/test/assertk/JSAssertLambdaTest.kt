package test.assertk

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.promise
import kotlin.test.Test

class JSAssertLambdaTest {

    private val scope = CoroutineScope(Job())

    @Test fun returnedValue_works_in_coroutine_test() = scope.promise {
        assertThat {
            asyncReturnValue()
        }.isSuccess().isEqualTo(1)
    }

    @Test fun returnedValue_exception_works_in_coroutine_test() = scope.promise {
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