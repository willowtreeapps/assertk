package test.assertk.assertions

import assertk.assertFailureWith
import assertk.assertThat
import assertk.assertions.*
import test.assertk.exceptionPackageName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.test.fail

class ThrowableTest {
    private val rootCause = Exception("rootCause")
    private val cause = Exception("cause", rootCause)
    private val subject = Exception("test", cause)

    @Test
    fun extracts_message() {
        assertEquals(subject.message, assertThat(subject).message().valueOrFail)
    }

    @Test
    fun extracts_cause() {
        assertEquals(cause, assertThat(subject).cause().valueOrFail)
    }

    @Test
    fun extracts_root_cause() {
        assertEquals(rootCause, assertThat(subject).rootCause().valueOrFail)
    }

    //region hasMessage
    @Test
    fun hasMessage_same_message_passes() {
        assertThat(subject).hasMessage("test")
    }

    @Test
    fun hasMessage_different_message_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).hasMessage("not test")
        }
        assertEquals("expected [message]:<\"[not ]test\"> but was:<\"[]test\"> ($subject)", error.message)
    }
    //endregion

    //region messageContains
    @Test
    fun messageContains_similar_message_passes() {
        assertThat(subject).messageContains("es")
    }

    @Test
    fun messageContains_different_message_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).messageContains("not")
        }
        assertEquals("expected [message] to contain:<\"not\"> but was:<\"test\"> ($subject)", error.message)

    }
    //endregion

    //region hasCause
    @Test
    fun hasCause_same_type_and_message_passes() {
        assertThat(subject).hasCause(Exception("cause"))
    }

    @Test
    fun hasCause_no_cause_fails() {
        val causeless = Exception("test")
        val error = assertFailsWith<AssertionError> {
            assertThat(causeless).hasCause(cause)
        }
        assertEquals(
            "expected [cause] to not be null ($subject)",
            error.message
        )
    }

    @Test
    fun hasCause_different_message_fails() {
        val wrongCause = Exception("wrong")
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).hasCause(wrongCause)
        }
        assertEquals(
            "expected [cause.message]:<\"[wrong]\"> but was:<\"[cause]\"> ($subject)",
            error.message
        )
    }

    @Test
    fun hasCause_different_type_fails() {
        val wrongCause = IllegalArgumentException("cause")
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).hasCause(wrongCause)
        }
        assertEquals(
            "expected [cause.class]:<class $exceptionPackageName[IllegalArgument]Exception> but was:<class $exceptionPackageName[]Exception> ($subject)",
            error.message
        )
    }
    //endregion

    //region hasNoCause
    @Test
    fun hasNoCause_no_cause_passes() {
        val causeless = Exception("test")
        assertThat(causeless).hasNoCause()
    }

    @Test
    fun hasNoCause_cause_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).hasNoCause()
        }
        assertEquals("expected [cause] to be null but was:<$cause> ($subject)", error.message)
    }
    //endregion

    //region hasRootCause
    @Test
    fun hasRootCause_same_root_cause_type_and_message_passes() {
        assertThat(subject).hasRootCause(Exception("rootCause"))
    }

    @Test
    fun hasRootCause_wrong_cause_type_fails() {
        val wrongCause = IllegalArgumentException("rootCause")
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).hasRootCause(wrongCause)
        }
        assertEquals(
            "expected [rootCause.class]:<class $exceptionPackageName[IllegalArgument]Exception> but was:<class $exceptionPackageName[]Exception> ($subject)",
            error.message
        )
    }

    @Test
    fun hasRootCause_wrong_cause_message_fails() {
        val wrongCause = Exception("wrong")
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).hasRootCause(wrongCause)
        }
        assertEquals(
            "expected [rootCause.message]:<\"[wrong]\"> but was:<\"[rootCause]\"> ($subject)",
            error.message
        )
    }
    //endregion

    //region hasProperties
    @Test
    fun hasProperties_single_fail() {
        val exception = DummyException(1116, 12.5)
        val error = assertFailsWith<AssertionError> {
            assertFailureWith<DummyException> { throw exception }.hasProperties(
                DummyException::index to 1118,
                DummyException::rate to 12.5
            )
        }
        assertEquals(
            "expected [index]:<111[8]> but was:<111[6]> ($exception)",
            error.message
        )
    }

    @Test
    fun hasProperties_multiple_fails() {
        val exception = DummyException(1116, 12.5)
        val error = assertFailsWith<AssertionError> {
            assertFailureWith<DummyException> { throw exception }.hasProperties(
                DummyException::index to 1118,
                DummyException::rate to 15.3
            )
        }
        val message = error.message ?: fail("should have a message")
        assertTrue("The following assertions failed (2 failures)" in message)
        assertTrue("expected [index]:<111[8]> but was:<111[6]> ($exception)" in message)
        assertTrue("expected [rate]:<1[5.3]> but was:<1[2.5]> ($exception)" in message)
    }

    class DummyException(val index: Int, val rate: Double): Exception("bad value: $index -> $rate")
    //endregion
}
