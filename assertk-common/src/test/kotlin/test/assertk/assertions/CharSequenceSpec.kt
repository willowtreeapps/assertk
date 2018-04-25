package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import test.assertk.Assertions
import kotlin.test.Test

class CharSequenceSpec_a_CharSequence_On_isEmpty() {
    @Test
    fun it_Given_an_empty_sequence_test_should_pass() {
        assert("").isEmpty()
    }

    @Test
    fun it_Given_a_non_empty_sequence_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("test").isEmpty()
        }.hasMessage("expected to be empty but was:<\"test\">")
    }
}

class CharSequenceSpec_a_CharSequence_On_isNotEmpty() {
    @Test
    fun it_Given_an_empty_sequence_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("").isNotEmpty()
        }.hasMessage("expected to not be empty")
    }

    @Test
    fun it_Given_a_non_empty_sequence_test_should_fail() {
        assert("test").isNotEmpty()
    }
}

class CharSequenceSpec_a_CharSequence_On_isNullOrEmpty() {
    @Test
    fun it_Given_an_empty_sequence_or_null_test_should_pass() {
        assert("").isNullOrEmpty()
        val test: CharSequence? = null
        assert(test).isNullOrEmpty()
    }

    @Test
    fun it_Given_a_non_empty_sequence_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("test").isNullOrEmpty()
        }.hasMessage("expected to be null or empty but was:<\"test\">")
    }
}

class CharSequenceSpec_a_CharSequence_On_hasLength() {
    @Test
    fun it_Given_a_sequence_test_should_pass() {
        assert("test").length().isEqualTo(4)
        assert("").length().isEqualTo(0)
    }

    @Test
    fun it_Given_a_sequence_with_different_length_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("test").length().isEqualTo(0)
        }.hasMessage("expected [length]:<[0]> but was:<[4]> (\"test\")")
    }
}
