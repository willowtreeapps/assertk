package test.assertk

import assertk.assertThat
import assertk.asAssert
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NameTest {
    @Test
    fun with_no_name_fails_with_default_error_message() {
        val error1 = assertFailsWith<AssertionError> {
            assertThat("yes").isEqualTo("no")
        }
        val error2 = assertFailsWith<AssertionError> {
            "yes".asAssert().isEqualTo("no")
        }
        val expected = "expected:<\"[no]\"> but was:<\"[yes]\">"
        assertEquals(expected, error1.message)
        assertEquals(expected, error2.message)
    }

    @Test
    fun with_name_fails_with_name_prefixing_message() {
        val error1 = assertFailsWith<AssertionError> {
            assertThat("yes", name = "test").isEqualTo("no")
        }
        val error2 = assertFailsWith<AssertionError> {
            "yes".asAssert(name = "test").isEqualTo("no")
        }
        val expected = "expected [test]:<\"[no]\"> but was:<\"[yes]\">"
        assertEquals(expected, error1.message)
        assertEquals(expected, error2.message)
    }

    @Test
    fun property_based_assertion_fails_with_getter_name_prefixing_message() {
        data class Person(val name: String)

        val p = Person("Yoda")

        val error1 = assertFailsWith<AssertionError> {
            assertThat(p::name).isEqualTo("Darth")
        }
        val error2 = assertFailsWith<AssertionError> {
            p::name.asAssert().isEqualTo("Darth")
        }
        val expected = "expected [name]:<\"[Darth]\"> but was:<\"[Yoda]\">"
        assertEquals(expected, error1.message)
        assertEquals(expected, error2.message)
    }

    @Test
    fun property_based_assertion_fails_with_given_name_prefixing_message() {
        data class Person(val name: String)

        val p = Person("Yoda")

        val error1 = assertFailsWith<AssertionError> {
            assertThat(p::name, "test").isEqualTo("Darth")
        }
        val error2 = assertFailsWith<AssertionError> {
            p::name.asAssert("test").isEqualTo("Darth")
        }
        val expected = "expected [test]:<\"[Darth]\"> but was:<\"[Yoda]\">"
        assertEquals(expected, error1.message)
        assertEquals(expected, error2.message)
    }
}
