package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class NumberSpec : Spek({
    given("a Number") {
        on("isZero()") {
            it("Given a zero, test should pass") {
                assert(0).isZero()
            }

            it("Given a not zero, test should fail") {
                Assertions.assertThatThrownBy {
                    assert(1).isZero()
                }.hasMessage("expected to be 0 but was:<1>")
            }
        }

        on("isNotZero()") {
            it("Given a zero, test should fail") {
                Assertions.assertThatThrownBy {
                    assert(0).isNotZero()
                }.hasMessage("expected to not be 0")
            }

            it("Given a not zero, test should pass") {
                    assert(1).isNotZero()
            }
        }

        on("isPositive()") {
            it("Given a positive number, test should pass") {
                assert(1).isPositive()
            }

            it("Given a zero or negative number, test should fail") {
                Assertions.assertThatThrownBy {
                    assert(0).isPositive()
                }.hasMessage("expected to be positive but was:<0>")

                Assertions.assertThatThrownBy {
                    assert(-1).isPositive()
                }.hasMessage("expected to be positive but was:<-1>")
            }
        }

        on("isNegative()") {
            it("Given a zero or negative number, test should fail") {
                Assertions.assertThatThrownBy {
                    assert(0).isNegative()
                }.hasMessage("expected to be negative but was:<0>")

                Assertions.assertThatThrownBy {
                    assert(1).isNegative()
                }.hasMessage("expected to be negative but was:<1>")
            }

            it("Given a negative number, test should pass") {
                assert(-1).isNegative()
            }
        }
    }
})
