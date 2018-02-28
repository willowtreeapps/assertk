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

        val rootCause = TestException("rootCause")
        val cause = TestException("cause", rootCause)
        val subject = TestException("test", cause)

        on("hasMessage()") {
            it("should pass a successful test") {
                assert(subject).message().isEqualTo("test")
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).message().isEqualTo("not test")
                }.hasMessage("expected [message]:<\"[not ]test\"> but was:<\"[]test\"> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasCause()") {
            it("should pass a successful test") {
                assert(subject).cause().isEqualTo(cause)
            }

            it("should fail an unsuccessful test on no cause") {
                val causeless = TestException("test")
                Assertions.assertThatThrownBy {
                    assert(causeless).cause().isEqualTo(cause)
                }.hasMessage("expected [cause]:<$THROWABLE_SPEC\$TestException: cause> but was:<null> ($THROWABLE_SPEC\$TestException: test)")
            }

            it("should fail an unsuccessful test on wrong cause class") {
                val wrongCause = Exception("cause")
                Assertions.assertThatThrownBy {
                    assert(subject).cause().isEqualTo(wrongCause)
                }.hasMessage("expected [cause]:<[java.lang.]Exception: cause> but was:<[$THROWABLE_SPEC\$Test]Exception: cause> ($THROWABLE_SPEC\$TestException: test)")
            }

            it("should fail an unsuccessful test on wrong message") {
                val wrongCause = TestException("wrong cause")
                Assertions.assertThatThrownBy {
                    assert(subject).cause().isEqualTo(wrongCause)
                }.hasMessage("expected [cause]:<...Spec\$TestException: [wrong ]cause> but was:<...Spec\$TestException: []cause> ($THROWABLE_SPEC\$TestException: test)")
            }

            it("should fail an unsuccessful test on wrong cause class and message") {
                val wrongCause = Exception("wrong cause")
                Assertions.assertThatThrownBy {
                    assert(subject).cause().isEqualTo(wrongCause)
                }.hasMessage("expected [cause]:<[java.lang.Exception: wrong] cause> but was:<[$THROWABLE_SPEC\$TestException:] cause> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasNoCause()") {
            it("should pass a successful test") {
                val causeless = TestException("test")
                assert(causeless).cause().isNull()
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).cause().isNull()
                }.hasMessage("expected [cause] to be null but was:<$THROWABLE_SPEC\$TestException: cause> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasMessageStartingWith") {
            it("should pass a successful test") {
                assert(subject).message().isNotNull { it.startsWith("t") }
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).message().isNotNull { it.startsWith("f") }
                }.hasMessage("expected [message] to start with:<\"f\"> but was:<\"test\"> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasMessageContaining") {
            it("should pass a successful test") {
                assert(subject).message().isNotNull { it.contains("es") }
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).message().isNotNull { it.contains("f") }
                }.hasMessage("expected [message] to contain:<\"f\"> but was:<\"test\"> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasMessageMatching") {
            it("should pass a successful test") {
                assert(subject).message().isNotNull { it.matches("t.*t".toRegex()) }
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).message().isNotNull { it.matches("f.*f".toRegex()) }
                }.hasMessage("expected [message] to match:</f.*f/> but was:<\"test\"> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasMessageEndingWidth") {
            it("should pass a successful test") {
                assert(subject).message().isNotNull { it.endsWith("t") }
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).message().isNotNull { it.endsWith("f") }
                }.hasMessage("expected [message] to end with:<\"f\"> but was:<\"test\"> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasCauseInstanceOf(KClass)") {
            it("should pass a successful test") {
                assert(subject).cause().isNotNull { it.isInstanceOf(TestException::class) }
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).cause().isNotNull { it.isInstanceOf(DifferentException::class) }
                }.hasMessage("expected [cause] to be instance of:<$THROWABLE_SPEC\$DifferentException> but had class:<$THROWABLE_SPEC\$TestException> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasCauseInstanceOf(Class)") {
            it("should pass a successful test") {
                assert(subject).cause().isNotNull { it.isInstanceOf(TestException::class.java) }
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).cause().isNotNull { it.isInstanceOf(DifferentException::class.java) }
                }.hasMessage("expected [cause] to be instance of:<$THROWABLE_SPEC\$DifferentException> but had class:<$THROWABLE_SPEC\$TestException> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasCauseWithClass(KClass)") {
            it("should pass a successful test") {
                assert(subject).cause().isNotNull { it.kClass().isEqualTo(TestException::class) }
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).cause().isNotNull { it.kClass().isEqualTo(Exception::class) }
                }.hasMessage("expected [cause.class]:<class [java.lang.]Exception> but was:<class [$THROWABLE_SPEC\$Test]Exception> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasCauseWithClass(Class)") {
            it("should pass a successful test") {
                assert(subject).cause().isNotNull { it.jClass().isEqualTo(TestException::class.java) }
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).cause().isNotNull { it.jClass().isEqualTo(Exception::class.java) }
                }.hasMessage("expected [cause.class]:<[java.lang.]Exception> but was:<[$THROWABLE_SPEC\$Test]Exception> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasRootCause()") {
            it("should pass a successful test") {
                assert(subject).rootCause().isEqualTo(rootCause)
            }

            it("should fail an unsuccessful test on no cause") {
                val causeless = TestException("test")
                Assertions.assertThatThrownBy {
                    assert(causeless).rootCause().isEqualTo(rootCause)
                }.hasMessage("expected [rootCause]:<...Spec\$TestException: [rootCause]> but was:<...Spec\$TestException: [test]>")
            }

            it("should fail an unsuccessful test on wrong cause class") {
                val wrongCause = Exception("cause")
                Assertions.assertThatThrownBy {
                    assert(subject).rootCause().isEqualTo(wrongCause)
                }.hasMessage("expected [rootCause]:<[java.lang.Exception: c]ause> but was:<[$THROWABLE_SPEC\$TestException: rootC]ause> ($THROWABLE_SPEC\$TestException: test)")
            }

            it("should fail an unsuccessful test on wrong message") {
                val wrongCause = TestException("wrong cause")
                Assertions.assertThatThrownBy {
                    assert(subject).rootCause().isEqualTo(wrongCause)
                }.hasMessage("expected [rootCause]:<...Spec\$TestException: [wrong c]ause> but was:<...Spec\$TestException: [rootC]ause> ($THROWABLE_SPEC\$TestException: test)")
            }

            it("should fail an unsuccessful test on wrong cause class and message") {
                val wrongCause = Exception("wrong cause")
                Assertions.assertThatThrownBy {
                    assert(subject).rootCause().isEqualTo(wrongCause)
                }.hasMessage("expected [rootCause]:<[java.lang.Exception: wrong c]ause> but was:<[$THROWABLE_SPEC\$TestException: rootC]ause> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasRootCauseInstanceOf(KClass)") {
            it("should pass a successful test") {
                assert(subject).rootCause().isInstanceOf(TestException::class)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).rootCause().isInstanceOf(DifferentException::class)
                }.hasMessage("expected [rootCause] to be instance of:<$THROWABLE_SPEC\$DifferentException> but had class:<$THROWABLE_SPEC\$TestException> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasRootCauseInstanceOf(Class)") {
            it("should pass a successful test") {
                assert(subject).rootCause().isInstanceOf(TestException::class.java)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).cause().isNotNull { it.isInstanceOf(DifferentException::class.java) }
                }.hasMessage("expected [cause] to be instance of:<$THROWABLE_SPEC\$DifferentException> but had class:<$THROWABLE_SPEC\$TestException> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasRootCauseWithClass(KClass)") {
            it("should pass a successful test") {
                assert(subject).rootCause().kClass().isEqualTo(TestException::class)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).rootCause().kClass().isEqualTo(Exception::class)
                }.hasMessage("expected [rootCause.class]:<class [java.lang.]Exception> but was:<class [$THROWABLE_SPEC\$Test]Exception> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasRootCauseWithClass(Class)") {
            it("should pass a successful test") {
                assert(subject).rootCause().jClass().isEqualTo(TestException::class.java)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).rootCause().jClass().isEqualTo(Exception::class.java)
                }.hasMessage("expected [rootCause.class]:<[java.lang.]Exception> but was:<[$THROWABLE_SPEC\$Test]Exception> ($THROWABLE_SPEC\$TestException: test)")
            }
        }

        on("hasStackTraceContaining") {
            it("should pass a successful test") {
                assert(subject).stackTrace().contains("$THROWABLE_SPEC\$1\$1.invoke(ThrowableSpec.kt:18)")
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).stackTrace().contains("wrong")
                }.hasMessageStartingWith("expected [stack trace] to contain:<\"wrong\"> but was:")
            }
        }
    }
}) {
    class TestException(message: String = "test", cause: Throwable? = null) : Exception(message, cause)

    class DifferentException : Exception()
}