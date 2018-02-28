package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class CharSequenceSpec : Spek({
    given("a CharSequence") {
        on("isEmpty()") {
            it("Given an empty sequence, test should pass") {
                assert("").isEmpty()
            }

            it("Given a non empty sequence, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("test").isEmpty()
                }.hasMessage("expected to be empty but was:<\"test\">")
            }
        }

        on("isNotEmpty()") {
            it("Given an empty sequence, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("").isNotEmpty()
                }.hasMessage("expected to not be empty")
            }

            it("Given a non empty sequence, test should fail") {
                assert("test").isNotEmpty()
            }
        }

        on("isNullOrEmpty()") {
            it("Given an empty sequence or null, test should pass") {
                assert("").isEmpty()
                val test : CharSequence? = null
                assert(test).isNullOrEmpty()
            }

            it("Given a non empty sequence, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("test").isEmpty()
                }.hasMessage("expected to be empty but was:<\"test\">")
            }
        }

        on("hasLength()") {
            it("Given a sequence, test should pass") {
                assert("test").length().isEqualTo(4)
                assert("").length().isEqualTo(0)
            }

            it("Given a sequence with different length, test should fail") {
                Assertions.assertThatThrownBy {
                    assert("test").length().isEqualTo(0)
                }.hasMessage("expected [length]:<[0]> but was:<[4]> (\"test\")")
            }
        }
    }
})