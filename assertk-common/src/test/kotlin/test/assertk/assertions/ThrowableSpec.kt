package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import test.assertk.Assertions
import test.assertk.exceptionPackageName
import test.assertk.shortTestExceptionPackageName
import test.assertk.testExceptionPackageName
import kotlin.test.Test


val TEST_EXCEPTION = TestException::class
val DIFFERENT_EXCEPTION = DifferentException::class

private val rootCause = TestException("rootCause")
private val cause = TestException("cause", rootCause)
private val subject = TestException("test", cause)

class ThrowableSpec_a_Throwable_On_hasMessage() {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).message().isEqualTo("test")
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).message().isEqualTo("not test")
        }.hasMessage("expected [message]:<\"[not ]test\"> but was:<\"[]test\"> (${subject})")
    }
}

class ThrowableSpec_a_Throwable_On_hasCause() {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).cause().isEqualTo(cause)
    }

    @Test
    fun it_should_fail_an_unsuccessful_test_on_no_cause() {
        val causeless = TestException("test")
        Assertions.assertThatThrownBy {
            assert(causeless).cause().isEqualTo(cause)
        }.hasMessage("expected [cause]:<${testExceptionPackageName}TestException: cause> but was:<null> ($causeless)")
    }

    @Test
    fun it_should_fail_an_unsuccessful_test_on_wrong_cause_class() {
        val wrongCause = Exception("cause")
        Assertions.assertThatThrownBy {
            assert(subject).cause().isEqualTo(wrongCause)
        }.hasMessage("expected [cause]:<[${exceptionPackageName}]Exception: cause> but was:<[${testExceptionPackageName}Test]Exception: cause> (${subject})")
    }

    @Test
    fun it_should_fail_an_unsuccessful_test_on_wrong_message() {
        val wrongCause = TestException("wrong cause")
        Assertions.assertThatThrownBy {
            assert(subject).cause().isEqualTo(wrongCause)
        }.hasMessage("expected [cause]:<${shortTestExceptionPackageName}TestException: [wrong ]cause> but was:<${shortTestExceptionPackageName}TestException: []cause> (${subject})")
    }

    @Test
    fun it_should_fail_an_unsuccessful_test_on_wrong_cause_class_and_message() {
        val wrongCause = Exception("wrong cause")
        Assertions.assertThatThrownBy {
            assert(subject).cause().isEqualTo(wrongCause)
        }.hasMessage("expected [cause]:<[${exceptionPackageName}Exception: wrong] cause> but was:<[${testExceptionPackageName}TestException:] cause> (${subject})")
    }
}

class ThrowableSpec_a_Throwable_On_hasNoCause() {
    @Test
    fun it_should_pass_a_successful_test() {
        val causeless = TestException("test")
        assert(causeless).cause().isNull()
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).cause().isNull()
        }.hasMessage("expected [cause] to be null but was:<${cause}> (${subject})")
    }
}

class ThrowableSpec_a_Throwable_On_hasMessageStartingWith {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).message().isNotNull { it.startsWith("t") }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).message().isNotNull { it.startsWith("f") }
        }.hasMessage("expected [message] to start with:<\"f\"> but was:<\"test\"> (${subject})")
    }
}

class ThrowableSpec_a_Throwable_On_hasMessageContaining {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).message().isNotNull { it.contains("es") }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).message().isNotNull { it.contains("f") }
        }.hasMessage("expected [message] to contain:<\"f\"> but was:<\"test\"> (${subject})")
    }
}

class ThrowableSpec_a_Throwable_On_hasMessageMatching {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).message().isNotNull { it.matches("t.*t".toRegex()) }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        val regex = "f.*f".toRegex()
        Assertions.assertThatThrownBy {
            assert(subject).message().isNotNull { it.matches(regex) }
        }.hasMessage("expected [message] to match:</$regex/> but was:<\"test\"> (${subject})")
    }
}

class ThrowableSpec_a_Throwable_On_hasMessageEndingWidth {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).message().isNotNull { it.endsWith("t") }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).message().isNotNull { it.endsWith("f") }
        }.hasMessage("expected [message] to end with:<\"f\"> but was:<\"test\"> (${subject})")
    }
}

class ThrowableSpec_a_Throwable_On_hasCauseInstanceOfKClass {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).cause().isNotNull { it.isInstanceOf(TestException::class) }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).cause().isNotNull { it.isInstanceOf(DifferentException::class) }
        }.hasMessage("expected [cause] to be instance of:<${DIFFERENT_EXCEPTION}> but had class:<${TEST_EXCEPTION}> (${subject})")
    }
}

class ThrowableSpec_a_Throwable_On_hasCauseWithClassKClass {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).cause().isNotNull { it.kClass().isEqualTo(TestException::class) }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).cause().isNotNull { it.kClass().isEqualTo(Exception::class) }
        }.hasMessage("expected [cause.class]:<class [${exceptionPackageName}]Exception> but was:<class [${testExceptionPackageName}Test]Exception> (${subject})")
    }
}

class ThrowableSpec_a_Throwable_On_hasRootCause() {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).rootCause().isEqualTo(rootCause)
    }

    @Test
    fun it_should_fail_an_unsuccessful_test_on_no_cause() {
        val causeless = TestException("test")
        Assertions.assertThatThrownBy {
            assert(causeless).rootCause().isEqualTo(rootCause)
        }.hasMessage("expected [rootCause]:<${shortTestExceptionPackageName}TestException: [rootCause]> but was:<${shortTestExceptionPackageName}TestException: [test]>")
    }

    @Test
    fun it_should_fail_an_unsuccessful_test_on_wrong_cause_class() {
        val wrongCause = Exception("cause")
        Assertions.assertThatThrownBy {
            assert(subject).rootCause().isEqualTo(wrongCause)
        }.hasMessage("expected [rootCause]:<[${exceptionPackageName}Exception: c]ause> but was:<[${testExceptionPackageName}TestException: rootC]ause> (${subject})")
    }

    @Test
    fun it_should_fail_an_unsuccessful_test_on_wrong_message() {
        val wrongCause = TestException("wrong cause")
        Assertions.assertThatThrownBy {
            assert(subject).rootCause().isEqualTo(wrongCause)
        }.hasMessage("expected [rootCause]:<${shortTestExceptionPackageName}TestException: [wrong c]ause> but was:<${shortTestExceptionPackageName}TestException: [rootC]ause> (${subject})")
    }

    @Test
    fun it_should_fail_an_unsuccessful_test_on_wrong_cause_class_and_message() {
        val wrongCause = Exception("wrong cause")
        Assertions.assertThatThrownBy {
            assert(subject).rootCause().isEqualTo(wrongCause)
        }.hasMessage("expected [rootCause]:<[${exceptionPackageName}Exception: wrong c]ause> but was:<[${testExceptionPackageName}TestException: rootC]ause> (${subject})")
    }
}

class ThrowableSpec_a_Throwable_On_hasRootCauseInstanceOfKClass {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).rootCause().isInstanceOf(TestException::class)
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).rootCause().isInstanceOf(DifferentException::class)
        }.hasMessage("expected [rootCause] to be instance of:<${DIFFERENT_EXCEPTION}> but had class:<${TEST_EXCEPTION}> (${subject})")
    }
}

class ThrowableSpec_a_Throwable_On_hasRootCauseWithClassKClass {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).rootCause().isNotNull { it.kClass().isEqualTo(TestException::class) }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).rootCause().isNotNull { it.kClass().isEqualTo(Exception::class) }
        }.hasMessage("expected [rootCause.class]:<class [${exceptionPackageName}]Exception> but was:<class [${testExceptionPackageName}Test]Exception> (${subject})")
    }
}

class TestException(message: String = "test", cause: Throwable? = null) : Exception(message, cause)

class DifferentException : Exception()
