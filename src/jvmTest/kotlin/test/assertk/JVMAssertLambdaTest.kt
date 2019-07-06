package test.assertk

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.catch
import assertk.returnedValue
import assertk.thrownError
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
            }.returnedValue { isEqualTo(1) }
        }
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    @Test fun returnedValue_exception_works_in_coroutine_test() {
        runBlockingTest {
            assertThat {
                asyncThrows()
            }.thrownError { hasMessage("test") }
        }
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    @Test fun catch_works_in_coroutine_test() {
        runBlockingTest {
            val error = catch {
                asyncThrows()
            }
            assertThat(error).isNotNull().hasMessage("test")
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