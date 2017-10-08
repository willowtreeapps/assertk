package test.assertk.assertions

import assertk.assert
import assertk.assertions.hasCRC32
import assertk.assertions.hasMD5
import assertk.assertions.hasSHA1
import assertk.assertions.hasSHA256
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.io.ByteArrayInputStream
import java.io.InputStream

class ChecksumSpec: Spek({

    given("the checksum of a non empty stream") {

        it("using CRC32") {
            assert(nonEmptyStream()).hasCRC32(4012917027L)
        }

        it("using MD5") {
            assert(nonEmptyStream()).hasMD5("7F:03:21:9A:CF:8D:68:49:03:2A:1B:B6:E4:F1:4C:3C")
        }

        it("using SHA1") {
            assert(nonEmptyStream()).hasSHA1("5B:F8:75:11:92:91:EC:68:95:7E:D5:88:AD:ED:62:77:0E:44:AB:AF")
        }

        it("using SHA256") {
            assert(nonEmptyStream()).hasSHA256("4B:7B:EC:25:AE:64:52:9C:76:0B:C3:E8:82:9F:03:79:D9:2A:91:6B:22:A9:3B:82:CA:B9:18:54:55:1A:AD:C0")
        }

    }

    given("the checksum of an empty stream") {

        it("using CRC32") {
            assert(emptyStream()).hasCRC32(0)
        }

        it("using MD5") {
            assert(emptyStream()).hasMD5("D4:1D:8C:D9:8F:00:B2:04:E9:80:09:98:EC:F8:42:7E")
        }

        it("using SHA1") {
            assert(emptyStream()).hasSHA1("DA:39:A3:EE:5E:6B:4B:0D:32:55:BF:EF:95:60:18:90:AF:D8:07:09")
        }

        it("using SHA256") {
            assert(emptyStream()).hasSHA256("E3:B0:C4:42:98:FC:1C:14:9A:FB:F4:C8:99:6F:B9:24:27:AE:41:E4:64:9B:93:4C:A4:95:99:1B:78:52:B8:55")
        }
    }

    given("the checksum of a non empty stream") {

        it("using CRC32 and giving the wrong result") {
            assertThatThrownBy {
                assert(nonEmptyStream()).hasCRC32(-1)
            }.hasMessage("expected checksum to be -1, but it was 4012917027")
        }

        it("using MD5 and giving the wrong result") {
            assertThatThrownBy {
                assert(nonEmptyStream()).hasMD5("WRONG_CHECKSUM")
            }.hasMessage("expected checksum to be WRONG_CHECKSUM, but was 7F:03:21:9A:CF:8D:68:49:03:2A:1B:B6:E4:F1:4C:3C")
        }

        it("using SHA-1 and giving the wrong result") {
            assertThatThrownBy {
                assert(nonEmptyStream()).hasSHA1("WRONG_CHECKSUM")
            }.hasMessage("expected checksum to be WRONG_CHECKSUM, but was 5B:F8:75:11:92:91:EC:68:95:7E:D5:88:AD:ED:62:77:0E:44:AB:AF")
        }

        it("using SHA-256 and giving the wrong result") {
            assertThatThrownBy {
                assert(nonEmptyStream()).hasSHA256("WRONG_CHECKSUM")
            }.hasMessage("expected checksum to be WRONG_CHECKSUM, but was 4B:7B:EC:25:AE:64:52:9C:76:0B:C3:E8:82:9F:03:79:D9:2A:91:6B:22:A9:3B:82:CA:B9:18:54:55:1A:AD:C0")
        }

    }

})


/** Create a new empty stream. */
internal fun emptyStream(): InputStream {
    return ByteArrayInputStream(ByteArray(0))
}

internal fun nonEmptyStream(): InputStream {
    return ByteArrayInputStream(ByteArray(10, { (100+it).toByte() }))
}
