@file:JvmName("JVMThrowableSpecKt")
package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import test.assertk.Assertions
import test.assertk.testExceptionPackageName
import kotlin.test.Test

private val rootCause = TestException("rootCause")
private val cause = TestException("cause", rootCause)
private val subject = TestException("test", cause)

class ThrowableSpec_a_Throwable_On_hasCauseInstanceOfClass {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).cause().isNotNull { it.isInstanceOf(TestException::class.java) }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).cause().isNotNull { it.isInstanceOf(DifferentException::class.java) }
        }.hasMessage("expected [cause] to be instance of:<${testExceptionPackageName}DifferentException> but had class:<${testExceptionPackageName}TestException> ($subject)")
    }
}

class ThrowableSpec_a_Throwable_On_hasCauseWithClassClass {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).cause().isNotNull { it.jClass().isEqualTo(TestException::class.java) }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).cause().isNotNull { it.jClass().isEqualTo(Exception::class.java) }
        }.hasMessage("expected [cause.class]:<[java.lang.]Exception> but was:<[${testExceptionPackageName}Test]Exception> ($subject)")
    }
}

class ThrowableSpec_a_Throwable_On_hasRootCauseInstanceOfClass {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).rootCause().isInstanceOf(TestException::class.java)
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).cause().isNotNull { it.isInstanceOf(DifferentException::class.java) }
        }.hasMessage("expected [cause] to be instance of:<${testExceptionPackageName}DifferentException> but had class:<${testExceptionPackageName}TestException> ($subject)")
    }
}

class ThrowableSpec_a_Throwable_On_hasRootCauseWithClassClass {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).rootCause().isNotNull { it.jClass().isEqualTo(TestException::class.java) }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).rootCause().isNotNull { it.jClass().isEqualTo(Exception::class.java) }
        }.hasMessage("expected [rootCause.class]:<[java.lang.]Exception> but was:<[${testExceptionPackageName}Test]Exception> ($subject)")
    }
}

class ThrowableSpec_a_Throwable_On_hasStackTraceContaining {
    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).stackTrace().contains("test.assertk.assertions.JVMThrowableSpecKt.<clinit>(ThrowableSpec.kt:12)")
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).stackTrace().contains("wrong")
        }.hasMessageStartingWith("expected [stack trace] to contain:<\"wrong\"> but was:")
    }
}
