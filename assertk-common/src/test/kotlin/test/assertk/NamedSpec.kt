package test.assertk

import assertk.assert
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class BasicObjectString(val str: String) {
    override fun toString(): String = str
}

private val subject = BasicObjectString("yes")

class NamedSpec_an_assert_On_nothing {
    @Test
    fun it_should_fail_with_just_the_default_error_message() {
        val error = assertFails {
            assert(subject).isEqualTo(BasicObjectString("no"))
        }
        assertEquals("expected:<[no]> but was:<[yes]>", error.message)
    }
}

class NamedSpec_an_assert_On_named_String {
    @Test
    fun it_should_fail_prefixed_with_the_named_message() {
        val error = assertFails {
            assert(subject, "test").isEqualTo(BasicObjectString("no"))
        }
        assertEquals("expected [test]:<[no]> but was:<[yes]>", error.message)
    }
}
