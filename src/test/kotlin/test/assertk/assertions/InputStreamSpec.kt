package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
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
            }.hasMessage("expected to have the same content, but actual stream differs at pos 0. Actual stream: value=0x66, size=10. Other stream: value=0x77, size=10")

        }

        it("streamB has not the same content as streamC (using hasNotSameContentAs)") {
            assert(streamB()).hasNotSameContentAs(streamC())
        }
    }

    given("a non empty stream where we compute the checksum") {

        it("can compute CRC32 checksum") {
            assert(streamB()).hasCRC32(4012917027L)
        }

        it("can compute MD5 checksum") {
            assert(streamB()).hasMD5("7F:03:21:9A:CF:8D:68:49:03:2A:1B:B6:E4:F1:4C:3C")
        }

        it("can compute SHA-1 checksum") {
            assert(streamB()).hasSHA1("5B:F8:75:11:92:91:EC:68:95:7E:D5:88:AD:ED:62:77:0E:44:AB:AF")
        }

        it("can compute SHA-256 checksum") {
            assert(streamB()).hasSHA256("4B:7B:EC:25:AE:64:52:9C:76:0B:C3:E8:82:9F:03:79:D9:2A:91:6B:22:A9:3B:82:CA:B9:18:54:55:1A:AD:C0")
        }

    }

    given("checksum of an empty stream") {

        it("has CRC32 checksum") {
            assert(emptyStream()).hasCRC32(0)
        }

        it("has MD5 checksum") {
            assert(emptyStream()).hasMD5("D4:1D:8C:D9:8F:00:B2:04:E9:80:09:98:EC:F8:42:7E")
        }

        it("has SHA-1 checksum") {
            assert(emptyStream()).hasSHA1("DA:39:A3:EE:5E:6B:4B:0D:32:55:BF:EF:95:60:18:90:AF:D8:07:09")
        }

        it("has SHA-256 checksum") {
            assert(emptyStream()).hasSHA256("E3:B0:C4:42:98:FC:1C:14:9A:FB:F4:C8:99:6F:B9:24:27:AE:41:E4:64:9B:93:4C:A4:95:99:1B:78:52:B8:55")
        }
    }

    given("a non empty stream where we compute the checksum wrongly") {

        it("can compute CRC32 checksum") {
            assertThatThrownBy {
                assert(streamB()).hasCRC32(-1)
            }.hasMessage("expected checksum to be -1, but it was 4012917027")
        }

        it("can compute MD5 checksum") {
            assertThatThrownBy {
                assert(streamB()).hasMD5("WRONG_CHECKSUM")
            }.hasMessage("expected checksum to be WRONG_CHECKSUM, but was 7F:03:21:9A:CF:8D:68:49:03:2A:1B:B6:E4:F1:4C:3C")
        }

        it("can compute SHA-1 checksum") {
            assertThatThrownBy {
                assert(streamB()).hasSHA1("WRONG_CHECKSUM")
            }.hasMessage("expected checksum to be WRONG_CHECKSUM, but was 5B:F8:75:11:92:91:EC:68:95:7E:D5:88:AD:ED:62:77:0E:44:AB:AF")
        }

        it("can compute SHA-256 checksum") {
            assertThatThrownBy {
                assert(streamB()).hasSHA256("WRONG_CHECKSUM")
            }.hasMessage("expected checksum to be WRONG_CHECKSUM, but was 4B:7B:EC:25:AE:64:52:9C:76:0B:C3:E8:82:9F:03:79:D9:2A:91:6B:22:A9:3B:82:CA:B9:18:54:55:1A:AD:C0")
        }

    }
})


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