package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class StringSpec : Spek({
    given("a String") {
        on("isEqualTo()") {
            it("Given same strings, test should pass") {
                assert("test").isEqualTo("test")
            }

            it("Given different strings, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("").isEqualTo("test")
                }.hasMessage("expected:<\"[test]\"> but was:<\"[]\">")
            }

            it("Given same strings with different casing, test should pass") {
                assert("Test").isEqualTo("tesT", true)
            }

            it("Given same strings with different casing, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("Test").isEqualTo("tesT", false)
                }.hasMessage("expected:<\"[tesT]\"> but was:<\"[Test]\">")
            }

            it("Given a java nullable string, picks the objects isEqualTo over the string one") {
                assert(JavaNullableString.string()).isEqualTo(JavaNullableString.string())
            }
        }

        on("isNotEqualTo()") {
            it("Given same strings, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("test").isNotEqualTo("test")
                }.hasMessage("expected to not be equal to:<\"test\">")
            }

            it("Given different strings, test should pass") {
                assert("").isNotEqualTo("test")
            }

            it("Given same strings with different casing and ignoring case, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("Test").isNotEqualTo("tesT", true)
                }.hasMessage("expected:<\"tesT\"> not to be equal to (ignoring case):<\"Test\">")
            }

            it("Given different strings with different casing and not ignoring case, test should pass") {
                assert("Test").isNotEqualTo("tesT", false)
            }

            it("Given a java nullable string, picks the objects isNotEqualTo over the string one") {
                assert(JavaNullableString.string()).isNotEqualTo("wrong")
            }
        }

        on("contains()") {
            it("Given a string that contains a substring, test should pass") {
                assert("test").contains("est")
            }

            it("Given a string that doesn't contain a substring, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("test").contains("not")
                }.hasMessage("expected to contain:<\"not\"> but was:<\"test\">")
            }

            it("Given a string that contains a substring, ignoring case, test should pass") {
                assert("Test").contains("EST", true)
            }

            it("Given a string that doesn't contain a substring not ignoring case, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("Test").contains("EST", false)
                }.hasMessage("expected to contain:<\"EST\"> but was:<\"Test\">")
            }
        }

        on("startsWith()") {
            it("Given a string that starts with a substring, test should pass") {
                assert("test").startsWith("te")
            }

            it("Given a string that doesn't start with a substring, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("test").startsWith("st")
                }.hasMessage("expected to start with:<\"st\"> but was:<\"test\">")
            }

            it("Given a string that starts with a substring, ignoring case, test should pass") {
                assert("test").startsWith("TE", true)
            }

            it("Given a string that starts with a substring, not ignoring case, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("test").startsWith("TE", false)
                }.hasMessage("expected to start with:<\"TE\"> but was:<\"test\">")
            }
        }

        on("endsWith()") {
            it("Given a string that ends with a substring, test should pass") {
                assert("test").endsWith("st")
            }

            it("Given a string that doesn't end with a substring, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("test").endsWith("te")
                }.hasMessage("expected to end with:<\"te\"> but was:<\"test\">")
            }

            it("Given a string that ends with a substring, ignoring case, test should pass") {
                assert("test").endsWith("ST", true)
            }

            it("Given a string that ends with a substring, not ignoring case, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("test").endsWith("ST", false)
                }.hasMessage("expected to end with:<\"ST\"> but was:<\"test\">")
            }
        }

        on("hasLineCount()") {
            it("Given a string, test should pass") {
                assert("").hasLineCount(1)
                assert("test test").hasLineCount(1)
                assert("test test\ntest test").hasLineCount(2)
                assert("test test\r\ntest test").hasLineCount(2)
                assert("test test\rtest test").hasLineCount(2)
            }

            it("Given a string without a line break, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("test test").hasLineCount(2)
                }.hasMessage("expected to have line count:<2> but was:<1>")
            }
        }

        on("matches()") {
            it("Given a string that matches, test should pass") {
                assert("1234").matches(Regex("\\d\\d\\d\\d"))
            }

            it("Given a string that doesn't matche, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("12345").matches(Regex("\\d\\d\\d\\d"))
                }.hasMessage("expected to match:</\\d\\d\\d\\d/> but was:<\"12345\">")
            }
        }

    }
})
