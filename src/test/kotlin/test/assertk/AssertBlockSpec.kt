package test.assertk

import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class AssertBlockSpec : Spek({

    on("a returns value assert block value") {
        val subject = assert {
            1 + 1
        }

        it("should pass a successful returns value assertion") {
            subject.returnedValue {
                isEqualTo(2)
            }
        }

        it("should fail an unsuccessful return value assertion") {
            Assertions.assertThatThrownBy {
                subject.returnedValue {
                    isNegative()
                }
            }.hasMessage("expected to be negative but was:<2>")
        }

        it("should fail a throws error assertion") {
            Assertions.assertThatThrownBy {
                subject.thrownError {
                    message().isEqualTo("error")
                }
            }.hasMessage("expected exception but was:<2>")
        }
    }

    on("a throws error assert block") {
        val subject = assert {
            throw Exception("test")
        }

        it("should pass a successful throws error assertion") {
            subject.thrownError {
                message().isEqualTo("test")
            }
        }

        it("should fail a unsuccessful throws error assertion") {
            Assertions.assertThatThrownBy {
                subject.thrownError {
                    message().isEqualTo("wrong")
                }
            }.hasMessage("expected [message]:<\"[wrong]\"> but was:<\"[test]\"> (java.lang.Exception: test)")
        }

        it("should fail a returns value assertion") {
            Assertions.assertThatThrownBy {
                subject.returnedValue {
                    isPositive()
                }
            }.hasMessage("expected value but threw:<java.lang.Exception: test>")
        }
    }
})