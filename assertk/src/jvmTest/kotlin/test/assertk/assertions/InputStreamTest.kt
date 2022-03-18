package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.hasNotSameContentAs
import assertk.assertions.hasSameContentAs
import java.io.ByteArrayInputStream
import java.io.InputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class InputStreamTest {

    //region hasSameContentAs
    @Test fun hasSameContentAs_on_empty_streams_passes() {
        assertThat(emptyStream()).hasSameContentAs(emptyStream())
    }

    @Test fun hasSameContentAs_on_different_streams_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyStream()).hasSameContentAs(streamA())
        }
        assertEquals(
            "expected to have the same size, but actual stream size (0) differs from other stream size (10)",
            error.message
        )
    }

    @Test fun hasSameContentAs_on_different_non_empty_streams_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(streamA()).hasSameContentAs(streamB())
        }
        assertEquals(
            "expected to have the same content, but actual stream differs at pos 0. Actual stream: value=0x00, size=10. Other stream: value=0x64, size=10",
            error.message
        )
    }

    @Test fun hasSameContentAs_with_same_streams_passes() {
        assertThat(streamA()).hasSameContentAs(streamA())
    }

    @Test fun hasSameContent_on_different_sized_streams_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(prefixOfStreamA()).hasSameContentAs(streamA())
        }
        assertEquals("expected to have the same size, but actual size (5) differs from other size (10)", error.message)

        val error2 = assertFailsWith<AssertionError> {
            assertThat(streamA()).hasSameContentAs(prefixOfStreamA())
        }
        assertEquals("expected to have the same size, but actual size (10) differs from other size (5)", error2.message)
    }

    @Test fun hasSameContentAs_streams_different_in_single_value_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(streamB()).hasSameContentAs(streamC())
        }
        assertEquals(
            "expected to have the same content, but actual stream differs at pos 0. Actual stream: value=0x66, size=10. Other stream: value=0x77, size=10",
            error.message
        )

    }
    //endregion

    //region hasNotSameContentAs
    @Test fun hasNotSameContentAs_on_empty_streams_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyStream()).hasNotSameContentAs(emptyStream())
        }
        assertEquals("expected streams not to be equal, but they were equal", error.message)
    }

    @Test fun hasNotSameContentAs_on_different_streams_passes() {
        assertThat(emptyStream()).hasNotSameContentAs(streamA())
    }

    @Test fun hasNotSameContentAs_on_same_streams_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(streamA()).hasNotSameContentAs(streamA())
        }
        assertEquals("expected streams not to be equal, but they were equal", error.message)
    }

    @Test fun hasNotSameContentAs_on_different_non_empty_streams_passes() {
        assertThat(streamA()).hasNotSameContentAs(streamB())
    }

    @Test fun hasNotSameContentAs_on_different_sized_streams_passes() {
        assertThat(prefixOfStreamA()).hasNotSameContentAs(streamA())
        assertThat(streamA()).hasNotSameContentAs(prefixOfStreamA())
        assertThat(streamB()).hasNotSameContentAs(streamC())
    }
    //endregion
}

/** Create a new empty stream. */
internal fun emptyStream(): InputStream {
    return ByteArrayInputStream(ByteArray(0))
}

/** Every call creates a new stream, with the same content. */
internal fun streamA(): InputStream {
    return ByteArrayInputStream(byteArrayOf(0, 10, 20, 30, 40, 50, 60, 70, 80, 90))
}

/** Every call creates a new stream, with the same content. */
internal fun prefixOfStreamA(): InputStream {
    return ByteArrayInputStream(byteArrayOf(0, 10, 20, 30, 40))
}

/** Every call creates a new stream, with the same content. */
internal fun streamB(): InputStream {
    return ByteArrayInputStream(byteArrayOf(100, 101, 102, 103, 104, 105, 106, 107, 108, 109))
}

/** Every call creates a new stream, with the same content. The stream only differs in one place from [streamB] */
internal fun streamC(): InputStream {
    return ByteArrayInputStream(byteArrayOf(100, 101, 119, 103, 104, 105, 106, 107, 108, 109))
}
