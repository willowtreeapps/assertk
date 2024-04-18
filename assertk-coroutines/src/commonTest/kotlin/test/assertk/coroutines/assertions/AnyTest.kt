package test.assertk.coroutines.assertions

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.coroutines.assertions.having
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest

class AnyTest {
    val subject = BasicObject("test")

    @Test
    fun having_passes() = runTest {
        assertThat(subject).having("str") { it.str() }.isEqualTo("test")
    }

    @Test
    fun having_includes_name_in_failure_message() = runTest {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).having("str") { it.str() }.isEmpty()
        }
        assertEquals("expected [str] to be empty but was:<\"test\"> (test)", error.message)
    }

    @Test
    fun nested_having_include_names_in_failure_message() = runTest {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).having("other") { it.other() }.having("str") { it?.str() }.isNotNull()
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
