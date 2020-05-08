package test.assertk.coroutines

import assertk.assertThat
import assertk.assertions.isNegative
import assertk.assertions.isPositive
import assertk.coroutines.all
import assertk.coroutines.assertions.index
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import test.runTest
import kotlin.test.*


@OptIn(ExperimentalCoroutinesApi::class)
class AssertTest {
    @Test fun all()= runTest {
        val error = assertFails {
            assertThat(flowOf(4,2)).all {
                index(0).isPositive()
                index(1).isNegative()
            }
        }
        println(error.message)
        assertTrue(error.message!!.startsWith("expected [[1]] to be negative but was:<2>"))
    }
}