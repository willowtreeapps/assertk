package test.assertk

import assertk.assert
import assertk.assertions.isEqualTo
import kotlin.test.Test

class BasicObjectString(val str: String) {
    override fun toString(): String = str
}

private val subject = BasicObjectString("yes")

class NamedSpec_an_assert_On_nothing {
    @Test
    fun it_should_fail_with_just_the_default_error_message() {
        Assertions.assertThatThrownBy {
            assert(subject).isEqualTo(BasicObjectString("no"))
        }.hasMessage("expected:<[no]> but was:<[yes]>")
    }
}

class NamedSpec_an_assert_On_named_String {
    @Test
    fun it_should_fail_prefixed_with_the_named_message() {
        Assertions.assertThatThrownBy {
            assert(subject, "test").isEqualTo(BasicObjectString("no"))
        }.hasMessage("expected [test]:<[no]> but was:<[yes]>")
    }
}
