package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import test.assertk.Assertions
import kotlin.test.Test

class StringSpec_a_String_On_isEqualTo() {
    @Test
    fun it_Given_same_strings_test_should_pass() {
        assert("test").isEqualTo("test")
    }

    @Test
    fun it_Given_different_strings_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("").isEqualTo("test")
        }.hasMessage("expected:<\"[test]\"> but was:<\"[]\">")
    }

    @Test
    fun it_Given_same_strings_with_different_casing_test_should_pass() {
        assert("Test").isEqualTo("tesT", true)
    }

    @Test
    fun it_Given_same_strings_with_different_casing_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("Test").isEqualTo("tesT", false)
        }.hasMessage("expected:<\"[tesT]\"> but was:<\"[Test]\">")
    }

}

class StringSpec_a_String_On_isNotEqualTo() {
    @Test
    fun it_Given_same_strings_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("test").isNotEqualTo("test")
        }.hasMessage("expected to not be equal to:<\"test\">")
    }

    @Test
    fun it_Given_different_strings_test_should_pass() {
        assert("").isNotEqualTo("test")
    }

    @Test
    fun it_Given_same_strings_with_different_casing_and_ignoring_case_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("Test").isNotEqualTo("tesT", true)
        }.hasMessage("expected:<\"tesT\"> not to be equal to (ignoring case):<\"Test\">")
    }

    @Test
    fun it_Given_different_strings_with_different_casing_and_not_ignoring_case_test_should_pass() {
        assert("Test").isNotEqualTo("tesT", false)
    }

}

class StringSpec_a_String_On_contains() {
    @Test
    fun it_Given_a_string_that_contains_a_substring_test_should_pass() {
        assert("test").contains("est")
    }

    @Test
    fun it_Given_a_string_that_doesnt_contain_a_substring_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("test").contains("not")
        }.hasMessage("expected to contain:<\"not\"> but was:<\"test\">")
    }

    @Test
    fun it_Given_a_string_that_contains_a_substring_ignoring_case_test_should_pass() {
        assert("Test").contains("EST", true)
    }

    @Test
    fun it_Given_a_string_that_doesnt_contain_a_substring_not_ignoring_case_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("Test").contains("EST", false)
        }.hasMessage("expected to contain:<\"EST\"> but was:<\"Test\">")
    }
}

class StringSpec_a_String_On_startsWith() {
    @Test
    fun it_Given_a_string_that_starts_with_a_substring_test_should_pass() {
        assert("test").startsWith("te")
    }

    @Test
    fun it_Given_a_string_that_doesnt_start_with_a_substring_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("test").startsWith("st")
        }.hasMessage("expected to start with:<\"st\"> but was:<\"test\">")
    }

    @Test
    fun it_Given_a_string_that_starts_with_a_substring_ignoring_case_test_should_pass() {
        assert("test").startsWith("TE", true)
    }

    @Test
    fun it_Given_a_string_that_starts_with_a_substring_not_ignoring_case_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("test").startsWith("TE", false)
        }.hasMessage("expected to start with:<\"TE\"> but was:<\"test\">")
    }
}

class StringSpec_a_String_On_endsWith() {
    @Test
    fun it_Given_a_string_that_ends_with_a_substring_test_should_pass() {
        assert("test").endsWith("st")
    }

    @Test
    fun it_Given_a_string_that_doesnt_end_with_a_substring_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("test").endsWith("te")
        }.hasMessage("expected to end with:<\"te\"> but was:<\"test\">")
    }

    @Test
    fun it_Given_a_string_that_ends_with_a_substring_ignoring_case_test_should_pass() {
        assert("test").endsWith("ST", true)
    }

    @Test
    fun it_Given_a_string_that_ends_with_a_substring_not_ignoring_case_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("test").endsWith("ST", false)
        }.hasMessage("expected to end with:<\"ST\"> but was:<\"test\">")
    }
}

class StringSpec_a_String_On_hasLineCount() {
    @Test
    fun it_Given_a_string_test_should_pass() {
        assert("").hasLineCount(1)
        assert("test test").hasLineCount(1)
        assert("test test\ntest test").hasLineCount(2)
        assert("test test\r\ntest test").hasLineCount(2)
        assert("test test\rtest test").hasLineCount(2)
    }

    @Test
    fun it_Given_a_string_without_a_line_break_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert("test test").hasLineCount(2)
        }.hasMessage("expected to have line count:<2> but was:<1>")
    }
}

class StringSpec_a_String_On_matches() {
    @Test
    fun it_Given_a_string_that_matches_test_should_pass() {
        assert("1234").matches(Regex("\\d\\d\\d\\d"))
    }

    @Test
    fun it_Given_a_string_that_doesnt_matche_test_should_fail() {
        val regex = Regex("\\d\\d\\d\\d")
        Assertions.assertThatThrownBy {
            assert("12345").matches(regex)
        }.hasMessage("expected to match:</$regex/> but was:<\"12345\">")
    }
}
