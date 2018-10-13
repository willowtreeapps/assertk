package test.assertk

import assertk.assertAll
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class JavaAssertAllTest {
    @Test fun assertAll_propagates_out_of_memory_error() {
        val error = assertFails {
            assertAll(listOf({
                throw OutOfMemoryError()
            }))
        }

        assertEquals<Class<*>>(OutOfMemoryError::class.java, error.javaClass)
    }
}