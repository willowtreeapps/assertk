package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import test.assertk.exceptionPackageName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ThrowableTest {
    val rootCause = Exception("rootCause")
    val cause = Exception("cause", rootCause)
    val subject = Exception("test", cause)

    @Test
    fun extracts_message() {
        assertEquals(subject.message, assertThat(subject).havingMessage().valueOrFail)
    }

    @Test
    fun extracts_cause() {
        assertEquals(cause, assertThat(subject).havingCause().valueOrFail)
    }

    @Test
    fun extracts_root_cause() {
        assertEquals(rootCause, assertThat(subject).havingRootCause().valueOrFail)
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
}
