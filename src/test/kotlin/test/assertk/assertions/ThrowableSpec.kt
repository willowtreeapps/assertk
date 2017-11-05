package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class ThrowableSpec : Spek({
    val THROWABLE_SPEC = ThrowableSpec::class.qualifiedName

    given("a Throwable") {

        val cause = TestException("cause")
        val subject = TestException("test", cause)

        on("hasMessage()") {
            it("should pass a successful test") {
                assert(subject).hasMessage("test")
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).hasMessage("not test")
                }.hasMessage("expected [message]:<\"[not ]test\"> but was:<\"[]test\">")
            }
        }

        on("hasCause()") {
            it("should pass a successful test") {
                assert(subject).hasCause(cause)
            }

            it("should fail an unsuccessful test on no cause") {
                val causeless = TestException("test")
                Assertions.assertThatThrownBy {
                    assert(causeless).hasCause(cause)
                }.hasMessage("expected [cause] to not be null")
            }

            it("should fail an unsuccessful test on wrong cause class") {
                val wrongCause = Exception("cause")
                Assertions.assertThatThrownBy {
                    assert(subject).hasCause(wrongCause)
                }.hasMessage("expected [cause]:<[java.lang.]Exception: cause> but was:<[$THROWABLE_SPEC\$Test]Exception: cause>")
            }

            it("should fail an unsuccessful test on wrong message") {
                val wrongCause = TestException("wrong cause")
                Assertions.assertThatThrownBy {
                    assert(subject).hasCause(wrongCause)
                }.hasMessage("expected [cause]:<...Spec\$TestException: [wrong ]cause> but was:<...Spec\$TestException: []cause>")
            }

            it("should fail an unsuccessful test on wrong cause class and message") {
                val wrongCause = Exception("wrong cause")
                Assertions.assertThatThrownBy {
                    assert(subject).hasCause(wrongCause)
                }.hasMessage("expected [cause]:<[java.lang.Exception: wrong] cause> but was:<[$THROWABLE_SPEC\$TestException:] cause>")
            }
        }

        on("hasNoCause()") {
            it("should pass a successful test") {
                val causeless = TestException("test")
                assert(causeless).hasNoCause()
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).hasNoCause()
                }.hasMessage("expected [cause] to not exist but was:<$THROWABLE_SPEC\$TestException: cause>")
            }
        }

        on("hasMessageStartingWith") {
            it("should pass a successful test") {
                assert(subject).hasMessageStartingWith("t")
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).hasMessageStartingWith("f")
                }.hasMessage("expected [message] to start with:<\"f\"> but was:<\"test\">")
            }
        }

        on("hasMessageContaining") {
            it("should pass a successful test") {
                assert(subject).hasMessageContaining("es")
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).hasMessageContaining("f")
                }.hasMessage("expected [message] to contain:<\"f\"> but was:<\"test\">")
            }
        }

        on("hasMessageMatching") {
            it("should pass a successful test") {
                assert(subject).hasMessageMatching("t.*t".toRegex())
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).hasMessageMatching("f.*f".toRegex())
                }.hasMessage("expected [message] to match:</f.*f/> but was:<\"test\">")
            }
        }

        on("hasMessageEndingWidth") {
            it("should pass a successful test") {
                assert(subject).hasMessageEndingWith("t")
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).hasMessageEndingWith("f")
                }.hasMessage("expected [message] to end with:<\"f\"> but was:<\"test\">")
            }
        }

        on("hasCauseInstanceOf(KClass)") {
            it("should pass a successful test") {
                assert(subject).hasCauseInstanceOf(TestException::class)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as Exception).hasCauseInstanceOf(DifferentException::class)
                }.hasMessage("expected [cause] to be instance of:<$THROWABLE_SPEC\$DifferentException> but had class:<$THROWABLE_SPEC\$TestException>")
            }
        }

        on("hasCauseInstanceOf(Class)") {
            it("should pass a successful test") {
                assert(subject).hasCauseInstanceOf(TestException::class.java)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as Exception).hasCauseInstanceOf(DifferentException::class.java)
                }.hasMessage("expected [cause] to be instance of:<$THROWABLE_SPEC\$DifferentException> but had class:<$THROWABLE_SPEC\$TestException>")
            }
        }
    }
}) {
    class TestException(message: String = "test", cause: Throwable? = null) : Exception(message, cause)

    class DifferentException : Exception()
}