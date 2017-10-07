package test.assertk.assertions

import assertk.assert
import assertk.assertions.hasNotSameContentAs
import assertk.assertions.hasSameContentAs
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.io.ByteArrayInputStream
import java.io.InputStream

class InputStreamSpec : Spek({

    given("an empty stream") {

        it("has the same content as another empty stream") {
            assert(emptyStream()).hasSameContentAs(emptyStream())
        }

        it("has the same content as another empty stream (using hasNotSameContentAs)") {
            assertThatThrownBy {
                assert(emptyStream()).hasNotSameContentAs(emptyStream())
            }.hasMessage("expected streams not to be equal, but they were equal")
        }

        it("has not the same content as any other non empty stream") {
            assertThatThrownBy {
                assert(emptyStream()).hasSameContentAs(streamA())
            }.hasMessage("expected to have the same size, but actual stream size (0) differs from other stream size (10)")
        }

        it("has not the same content as any other non emnpty stream (using hasNotSameContentAs)") {
            assert(emptyStream()).hasNotSameContentAs(streamA())
        }
    }

    given("a non empty stream") {

        it("has the same content as another stream using a copy of the byte array") {
            assert(streamA()).hasSameContentAs(streamA())
        }

        it("has the same content as another stream using a copy of the byte array (using hasNotSameContentAs)") {
            assertThatThrownBy {
                assert(streamA()).hasNotSameContentAs(streamA())
            }.hasMessage("expected streams not to be equal, but they were equal")
        }

        it("has not the same content as another stream using another byte array") {
            assertThatThrownBy {
                assert(streamA()).hasSameContentAs(streamB())
            }.hasMessage("expected to have the same content, but actual stream differs at pos 0. Actual stream: value=0x00, size=10. Other stream: value=0x64, size=10")
        }

        it("has not the same content as another stream using another byte array (using hasNotSameContentAs)") {

            assert(streamA()).hasNotSameContentAs(streamB())
        }
    }

    given("a non empty stream which is the prefix of the second stream") {

        it("is not the same as the second stream") {
            assertThatThrownBy {
                assert(prefixOfStreamA()).hasSameContentAs(streamA())
            }.hasMessage("expected to have the same size, but actual size (5) differs from other size (10)")
        }

        it("is not the same as the second stream (using hasNotSameContentAs)") {
            assert(prefixOfStreamA()).hasNotSameContentAs(streamA())
        }

    }

    given("a non empty stream which has an prefix stream") {

        it("is not the same as its prefix stream") {

            assertThatThrownBy {
                assert(streamA()).hasSameContentAs(prefixOfStreamA())
            }.hasMessage("expected to have the same size, but actual size (10) differs from other size (5)")
        }

        it("is not the same as its prefix stream (using hasNotSameContentAs)") {
            assert(streamA()).hasNotSameContentAs(prefixOfStreamA())
        }

    }

    given("another non empty stream which differs from the other somewhere in the middle") {

        it("streamB has not the same content as streamC") {

            assertThatThrownBy {
                assert(streamB()).hasSameContentAs(streamC())
            }.hasMessage("expected to have the same content, but actual stream differs at pos 0. Actual stream: value=0x78, size=10. Other stream: value=0x77, size=10")

        }

        it("streamB has not the same content as streamC (using hasNotSameContentAs)") {
            assert(streamB()).hasNotSameContentAs(streamC())
        }
    }
})


/** Create a new empty stream. */
internal fun emptyStream(): InputStream {
    return ByteArrayInputStream(ByteArray(0))
}

/** Every call creates a new stream, with the same content. */
internal fun streamA(): InputStream {
    return ByteArrayInputStream(arrayOf(0.toByte(), 10.toByte(), 20.toByte(), 30.toByte(), 40.toByte(), 50.toByte(), 60.toByte(), 70.toByte(), 80.toByte(), 90.toByte()).toByteArray())
}

/** Every call creates a new stream, with the same content. */
internal fun prefixOfStreamA(): InputStream {
    return ByteArrayInputStream(arrayOf(0.toByte(), 10.toByte(), 20.toByte(), 30.toByte(), 40.toByte()).toByteArray())
}

/** Every call creates a new stream, with the same content. */
internal fun streamB(): InputStream {
    return ByteArrayInputStream(
            arrayOf(
                    100.toByte(), 110.toByte(), 120.toByte(), 130.toByte(), 140.toByte(), 150.toByte(), 160.toByte(), 170.toByte(), 180.toByte(), 190.toByte()
            ).toByteArray()
    )
}

/** Every call creates a new stream, with the same content. The stream only differs in one place from [streamB] */
internal fun streamC(): InputStream {
    return ByteArrayInputStream(
            arrayOf(
                    100.toByte(), 110.toByte(), 119.toByte(), 130.toByte(), 140.toByte(), 150.toByte(), 160.toByte(), 170.toByte(), 180.toByte(), 190.toByte()
            ).toByteArray()
    )
}