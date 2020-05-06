package test.assertk.coroutines.assertions

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.coroutines.assertions.suspendCall
import test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class AnyTest {
    val subject = BasicObject("test")

    @Test fun suspendCall_passes() = runTest {
        assertThat(subject).suspendCall("str") { it.str() }.isEqualTo("test")
    }

    @Test fun suspendCall_includes_name_in_failure_message() = runTest {
        val error = assertFails {
            assertThat(subject).suspendCall("str") { it.str() }.isEmpty()
        }
        assertEquals("expected [str] to be empty but was:<\"test\"> (test)", error.message)
    }

    @Test fun nested_suspendCall_include_names_in_failure_message() = runTest {
        val error = assertFails {
            assertThat(subject).suspendCall("other") { it.other() }.suspendCall("str") { it?.str() }.isNotNull()
        }
        assertEquals("expected [other.str] to not be null (test)", error.message)
    }

    companion object {

        class BasicObject(
            private val str: String,
            private val other: BasicObject? = null
        ) {
            suspend fun str() = str

            suspend fun other() = other

            override fun toString(): String = str

            override fun equals(other: Any?): Boolean =
                (other is BasicObject) && (str == other.str && this.other == other.other)

            override fun hashCode(): Int = 42
        }
    }
}