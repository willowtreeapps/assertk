package test.me.tatarka.assertk

import me.tatarka.assertk.assert
import me.tatarka.assertk.assertions.hasMessage
import me.tatarka.assertk.assertions.isEqualTo
import me.tatarka.assertk.assertions.isNegative
import me.tatarka.assertk.assertions.isPositive
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
            subject.returnsValue {
                it.isEqualTo(2)
            }
        }

        it("should fail an unsuccessful return value assertion") {
            Assertions.assertThatThrownBy {
                subject.returnsValue {
                    it.isNegative()
                }
            }.hasMessage("expected to be negative but was:<2>")
        }

        it("should fail a throws error assertion") {
            Assertions.assertThatThrownBy {
                subject.throwsError {
                    it.hasMessage("error")
                }
            }.hasMessage("expected exception but was:<2>")
        }
    }

    on("a throws error assert block") {
        val subject = assert {
            throw Exception("test")
        }

        it("should pass a successful throws error assertion") {
            subject.throwsError {
                it.hasMessage("test")
            }
        }

        it("should fail a unsuccessful throws error assertion") {
            Assertions.assertThatThrownBy {
                subject.throwsError {
                    it.hasMessage("wrong")
                }
            }.hasMessage("expected [message]:<\"[wrong]\"> but was:<\"[test]\">")
        }

        it("should fail a returns value assertion") {
            Assertions.assertThatThrownBy {
                subject.returnsValue {
                    it.isPositive()
                }
            }.hasMessage("expected value but threw:<java.lang.Exception: test>")
        }
    }
})