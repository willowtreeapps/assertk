package test.assertk

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NameTest {
    @Test fun with_no_name_fails_with_default_error_message() {
        val error = assertFailsWith<AssertionError> {
            assertThat("yes").isEqualTo("no")
        }
        assertEquals("expected:<\"[no]\"> but was:<\"[yes]\">", error.message)
    }

    @Test fun with_name_fails_with_name_prefixing_message() {
        val error = assertFailsWith<AssertionError> {
            assertThat("yes", name = "test").isEqualTo("no")
        }
        assertEquals("expected [test]:<\"[no]\"> but was:<\"[yes]\">", error.message)
    }

    @Test fun property_based_assertion_fails_with_getter_name_prefixing_message() {
        data class Person(val name: String)
        val p = Person("Yoda")

        val error = assertFailsWith<AssertionError> {
            assertThat(p::name).isEqualTo("Darth")
        }
        assertEquals("expected [name]:<\"[Darth]\"> but was:<\"[Yoda]\">", error.message)
    }

    @Test fun property_based_assertion_fails_with_given_name_prefixing_message() {
        data class Person(val name: String)
        val p = Person("Yoda")

        val error = assertFailsWith<AssertionError> {
            assertThat(p::name, "test").isEqualTo("Darth")
        }
        assertEquals("expected [test]:<\"[Darth]\"> but was:<\"[Yoda]\">", error.message)
    }
}
