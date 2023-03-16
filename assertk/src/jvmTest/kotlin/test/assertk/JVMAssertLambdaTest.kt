package test.assertk

import assertk.*
import assertk.assertions.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

class JVMAssertLambdaTest {

    @Test fun returnedValue_works_in_coroutine_test() {
        runBlocking {
            assertThat {
                asyncReturnValue()
            }.isSuccess().isEqualTo(1)
        }
    }

    @Test fun returnedValue_exception_works_in_coroutine_test() {
        runBlocking {
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