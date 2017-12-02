package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class AssertSpecComparable : Spek({

    given("a comparable") {
        val lowInt: Int = 0
        val highInt: Int = 2

        on("isGreaterThan()") {

            it("Checking if highInt is greater than lowInt should pass") {
                assert(highInt).isGreaterThan(lowInt)
            }

            it("Checking the same Type should fail") {
                Assertions.assertThatThrownBy {
                    assert(lowInt).isGreaterThan(lowInt)
                }.hasMessage("expected to be greater than:<0> but was:<0>")
            }

            it("Checking if lowInt is greater than highInt should fail") {
                Assertions.assertThatThrownBy {
                    assert(lowInt).isGreaterThan(highInt)
                }.hasMessage("expected to be greater than:<2> but was:<0>")
            }
        }

        on("isLessThan()") {

            it("Checking if lowInt is less than highInt should pass") {
                assert(lowInt).isLessThan(highInt)
            }

            it("Checking the same Type should fail") {
                Assertions.assertThatThrownBy {
                    assert(lowInt).isLessThan(lowInt)
                }.hasMessage("expected to be less than:<0> but was:<0>")
            }

            it("Checking if highInt is less than lowInt should fail") {
                Assertions.assertThatThrownBy {
                    assert(highInt).isLessThan(lowInt)
                }.hasMessage("expected to be less than:<0> but was:<2>")
            }
        }

        on("isGreaterThanOrEqualTo()") {

            it("Checking if highInt is greater or equal to lowInt should pass") {
                assert(highInt).isGreaterThanOrEqualTo(lowInt)
            }

            it("Checking the same Type should pass") {
                assert(lowInt).isGreaterThanOrEqualTo(lowInt)
            }

            it("Checking if lowInt is greater than or equal to highInt should fail") {
                Assertions.assertThatThrownBy {
                    assert(lowInt).isGreaterThanOrEqualTo(highInt)
                }.hasMessage("expected to be greater than or equal to:<2> but was:<0>")
            }
        }

        on("isLessThanOrEqualTo()") {

            it("Checking if lowInt is less than or equal to highInt should pass") {
                assert(lowInt).isLessThanOrEqualTo(highInt)
            }

            it("Checking the same Type should pass") {
                assert(lowInt).isLessThanOrEqualTo(lowInt)
            }

            it("Checking if highInt is less than or equal to lowInt should fail") {
                Assertions.assertThatThrownBy {
                    assert(highInt).isLessThanOrEqualTo(lowInt)
                }.hasMessage("expected to be less than or equal to:<0> but was:<2>")
            }
        }

        on("isBetween()") {
            it("Checking inside the range should pass") {
                assert(lowInt + 1).isBetween(lowInt, highInt)
            }

            it("Checking the lower bound should pass") {
                assert(lowInt).isBetween(lowInt, highInt)
            }

            it("Checking the upper bound should pass") {
                assert(highInt).isBetween(lowInt, highInt)
            }

            it("Checking below the lower bound should fail") {
                Assertions.assertThatThrownBy {
                    assert(lowInt - 1).isBetween(lowInt, highInt)
                }.hasMessage("expected to be between:<0> and <2> but was:<-1>")
            }

            it("Checking above the upper bound should fail") {
                Assertions.assertThatThrownBy {
                    assert(highInt + 1).isBetween(lowInt, highInt)
                }.hasMessage("expected to be between:<0> and <2> but was:<3>")
            }
        }

        on("isStrictlyBetween()") {
            it("Checking inside the range should pass") {
                assert(lowInt + 1).isStrictlyBetween(lowInt, highInt)
            }

            it("Checking the lower bound should fail") {
                Assertions.assertThatThrownBy {
                    assert(lowInt).isStrictlyBetween(lowInt, highInt)
                }.hasMessage("expected to be strictly between:<0> and <2> but was:<0>")
            }

            it("Checking the upper bound should fail") {
                Assertions.assertThatThrownBy {
                    assert(highInt).isStrictlyBetween(lowInt, highInt)
                }.hasMessage("expected to be strictly between:<0> and <2> but was:<2>")
            }

            it("Checking below the lower bound should fail") {
                Assertions.assertThatThrownBy {
                    assert(lowInt - 1).isStrictlyBetween(lowInt, highInt)
                }.hasMessage("expected to be strictly between:<0> and <2> but was:<-1>")
            }

            it("Checking above the upper bound should fail") {
                Assertions.assertThatThrownBy {
                    assert(highInt + 1).isStrictlyBetween(lowInt, highInt)
                }.hasMessage("expected to be strictly between:<0> and <2> but was:<3>")
            }
        }
    }
})
