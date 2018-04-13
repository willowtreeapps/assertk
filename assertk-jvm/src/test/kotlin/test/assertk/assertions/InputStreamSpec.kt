package test.assertk.assertions

import assertk.assert
import assertk.assertions.hasNotSameContentAs
import assertk.assertions.hasSameContentAs
import test.assertk.Assertions.Companion.assertThatThrownBy
import java.io.ByteArrayInputStream
import java.io.InputStream
import kotlin.test.Test


class InputStreamSpec_an_empty_stream {

    @Test
    fun it_has_the_same_content_as_another_empty_stream() {
        assert(emptyStream()).hasSameContentAs(emptyStream())
    }

    @Test
    fun it_has_the_same_content_as_another_empty_stream_using_hasNotSameContentAs() {
        assertThatThrownBy {
            assert(emptyStream()).hasNotSameContentAs(emptyStream())
        }.hasMessage("expected streams not to be equal, but they were equal")
    }

    @Test
    fun it_has_not_the_same_content_as_any_other_non_empty_stream() {
        assertThatThrownBy {
            assert(emptyStream()).hasSameContentAs(streamA())
        }.hasMessage("expected to have the same size, but actual stream size (0) differs from other stream size (10)")
    }

    @Test
    fun it_has_not_the_same_content_as_any_other_non_emnpty_stream_using_hasNotSameContentAs() {
        assert(emptyStream()).hasNotSameContentAs(streamA())
    }
}

class InputStreamSpec_an_empty_stream_a_non_empty_stream {

    @Test
    fun it_has_the_same_content_as_another_stream_using_a_copy_of_the_byte_array() {
        assert(streamA()).hasSameContentAs(streamA())
    }

    @Test
    fun it_has_the_same_content_as_another_stream_using_a_copy_of_the_byte_array_using_hasNotSameContentAs() {
        assertThatThrownBy {
            assert(streamA()).hasNotSameContentAs(streamA())
        }.hasMessage("expected streams not to be equal, but they were equal")
    }

    @Test
    fun it_has_not_the_same_content_as_another_stream_using_another_byte_array() {
        assertThatThrownBy {
            assert(streamA()).hasSameContentAs(streamB())
        }.hasMessage("expected to have the same content, but actual stream differs at pos 0. Actual stream: value=0x00, size=10. Other stream: value=0x64, size=10")
    }

    @Test
    fun it_has_not_the_same_content_as_another_stream_using_another_byte_array_using_hasNotSameContentAs() {

        assert(streamA()).hasNotSameContentAs(streamB())
    }
}

class InputStreamSpec_an_empty_stream_a_non_empty_stream_which_is_the_prefix_of_the_second_stream {

    @Test
    fun it_is_not_the_same_as_the_second_stream() {
        assertThatThrownBy {
            assert(prefixOfStreamA()).hasSameContentAs(streamA())
        }.hasMessage("expected to have the same size, but actual size (5) differs from other size (10)")
    }

    @Test
    fun it_is_not_the_same_as_the_second_stream_using_hasNotSameContentAs() {
        assert(prefixOfStreamA()).hasNotSameContentAs(streamA())
    }

}

class InputStreamSpec_an_empty_stream_a_non_empty_stream_which_has_an_prefix_stream {

    @Test
    fun it_is_not_the_same_as_its_prefix_stream() {

        assertThatThrownBy {
            assert(streamA()).hasSameContentAs(prefixOfStreamA())
        }.hasMessage("expected to have the same size, but actual size (10) differs from other size (5)")
    }

    @Test
    fun it_is_not_the_same_as_its_prefix_stream_using_hasNotSameContentAs() {
        assert(streamA()).hasNotSameContentAs(prefixOfStreamA())
    }

}

class InputStreamSpec_an_empty_stream_another_non_empty_stream_which_differs_from_the_other_somewhere_in_the_middle {

    @Test
    fun it_streamB_has_not_the_same_content_as_streamC() {

        assertThatThrownBy {
            assert(streamB()).hasSameContentAs(streamC())
        }.hasMessage("expected to have the same content, but actual stream differs at pos 0. Actual stream: value=0x66, size=10. Other stream: value=0x77, size=10")

    }

    @Test
    fun it_streamB_has_not_the_same_content_as_streamC_using_hasNotSameContentAs() {
        assert(streamB()).hasNotSameContentAs(streamC())
    }
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
