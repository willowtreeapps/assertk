package test.assertk

import assertk.*
import assertk.assertions.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class JVMAssertLambdaTest {

    @UseExperimental(ExperimentalCoroutinesApi::class)
    @Test fun returnedValue_works_in_coroutine_test() {
        runBlockingTest {
            assertThat {
                asyncReturnValue()
            }.isSuccess().isEqualTo(1)
        }
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    @Test fun returnedValue_exception_works_in_coroutine_test() {
        runBlockingTest {
            assertThat {
                asyncThrows()
            }.isFailure().hasMessage("test")
        }
    }

    private suspend fun asyncReturnValue(): Int {
        delay(10000)
        return 1
    }

    private suspend fun asyncThrows() {
        delay(10000)
        throw  Exception("test")
    }
}