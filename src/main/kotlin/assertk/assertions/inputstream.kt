package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import java.io.InputStream
import java.security.MessageDigest
import java.util.zip.CRC32

/** Asserts that the actual stream has the same content as the other stream.
 * Both [InputStream]s will be closed by the assertion.
 *
 * @param other which content is compared to the actual one
 */
fun Assert<InputStream>.hasSameContentAs(other: InputStream) {

    val msg = doTheStreamHaveTheSameContent(actual, other)
    if (msg != null) {
        expected(msg)
    }

}

/** Asserts that the actual stream has a different content as the other stream.
 * Both [InputStream]s will be closed by the assertion.
 *
 * @param other which content is compared to the actual one
 */
fun Assert<InputStream>.hasNotSameContentAs(other: InputStream) {

    val msg = doTheStreamHaveTheSameContent(actual, other)
    if (msg == null) {
        expected("streams not to be equal, but they were equal")
    }

}

/** Asserts that compute the MD5 hash of the stream content.
 *
 * @param expectedChecksum the expected checksum, every byte is separated by ':'
 */
fun Assert<InputStream>.hasMD5(expectedChecksum: String) = hasChecksum("MD5", expectedChecksum)

/** Asserts that compute the SHA-1 hash of the stream content.
 *
 * @param expectedChecksum the expected checksum, every byte is separated by ':'
 */
fun Assert<InputStream>.hasSHA1(expectedChecksum: String) = hasChecksum("SHA-1", expectedChecksum)

/** Asserts that compute the SHA-256 hash of the stream content.
 *
 * @param expectedChecksum the expected checksum, every byte is separated by ':'
 */
fun Assert<InputStream>.hasSHA256(expectedChecksum: String) = hasChecksum("SHA-256", expectedChecksum)

/** Asserts that compute the CRC32 hash of the stream content.
 *
 * @param expectedChecksum the expected checksum
 */
fun Assert<InputStream>.hasCRC32(expectedChecksum: Long) {
    actual.use {
        val crc32 = CRC32()
        val buffer = ByteArray(BUFFER_SIZE)

        while (true) {
            val len = actual.read(buffer)
            if (len < 0) break
            crc32.update(buffer, 0, len)
        }

        if (crc32.value != expectedChecksum) {
            expected("checksum to be $expectedChecksum, but it was ${crc32.value}")
        }
    }
}

/** Asserts that compute an user specified hash of the stream content.
 *
 * @param algorithm as used by the [MessageDigest.getAlgorithm]
 * @param expectedChecksum the expected expectedChecksum
 *
 */
fun Assert<InputStream>.hasChecksum(algorithm: String, expectedChecksum: String) {
    val actualChecksum = actual.computeChecksum(algorithm)
    if (expectedChecksum != actualChecksum) {
        expected("checksum to be $expectedChecksum, but was $actualChecksum")
    }
}

private fun InputStream.computeChecksum(algorithm: String): String {
    use {
        val buffer = ByteArray(BUFFER_SIZE)

        val md = MessageDigest.getInstance(algorithm)
        while (true) {
            val len = read(buffer)
            if (len < 0) break
            md.update(buffer, 0, len)
        }

        return md.digest().toHexString()
    }
}

private fun ByteArray.toHexString(separator: CharSequence = ":"): String {
    val sb = StringBuilder()
    this.asSequence().map { it.toHexString() }.joinTo(sb, separator)
    return sb.toString()
}

private fun Byte.toHexString() = String.format("%02X", this)

private const val BUFFER_SIZE = 4096

private fun consume(stream: InputStream): Int {
    val buffer = ByteArray(BUFFER_SIZE)

    var consumed = 0
    while (true) {
        val len = stream.read(buffer)
        if (len < 0) {
            return consumed
        }
        consumed += len
    }
}

private fun fillBuffer(stream: InputStream, buffer: ByteArray): Int {
    var pos = 0

    while (true) {
        val len = stream.read(buffer, pos, BUFFER_SIZE - pos)
        if (len == -1) {
            return if (pos < 0) 0 else pos
        }

        pos += len
        if (pos == BUFFER_SIZE) return pos
    }
}

private fun compare(len: Int, actual: ByteArray, other: ByteArray): Int? {
    for (i in 0 until len) {
        if (actual[i] != other[i]) {
            return i
        }
    }

    return null
}

/** Verifies that the streams have the same content.
 * @param actual one of the streams to be compared
 * @param other the other stream to be compared
 *
 * @return a message if the streams don't have the same content, or null if they have the same content
 */
private fun doTheStreamHaveTheSameContent(actual: InputStream, other: InputStream): String? {

    actual.use { actualStream ->
        other.use { otherStream ->

            var size = 0L

            val actualBuffer = ByteArray(BUFFER_SIZE)
            val otherBuffer = ByteArray(BUFFER_SIZE)

            while (true) {
                val actualRead = fillBuffer(actualStream, actualBuffer)
                val otherRead = fillBuffer(otherStream, otherBuffer)

                if (actualRead == otherRead) {

                    if (actualRead == 0) {
                        // the streams have the same size and are finished and have the same content
                        return null
                    } else {
                        val pos = compare(actualRead, actualBuffer, otherBuffer)
                        if (pos == null) {
                            // the current buffers are equals and we can try the next block
                            size += actualRead

                        } else {
                            // the first difference is in position pos

                            val actualSize = size + actualRead + consume(actualStream)
                            val otherSize = size + otherRead + consume(otherStream)

                            return "to have the same content, but actual stream differs at pos $size. Actual stream: value=0x${actualBuffer[pos].toHexString()}, size=$actualSize. Other stream: value=0x${otherBuffer[pos].toHexString()}, size=$otherSize"
                        }
                    }

                } else {

                    if (actualRead == 0) {
                        val actualSize = actualRead + size
                        val otherSize = consume(otherStream) + otherRead + size

                        return "to have the same size, but actual stream size (${actualSize}) differs from other stream size ($otherSize)"
                    } else if (otherRead == 0) {
                        val actualSize = consume(actualStream) + actualRead + size
                        val otherSize = otherRead + size

                        return "to have the same size, but actual stream size ($actualSize) differs from other stream size (${otherSize})"
                    } else {
                        val pos = compare(Math.min(actualRead, otherRead), actualBuffer, otherBuffer)
                        val actualSize = consume(actualStream) + actualRead + size
                        val otherSize = consume(otherStream) + otherRead + size

                        return if (pos == null) {
                            "to have the same size, but actual size (${actualSize}) differs from other size (${otherSize})"
                        } else {
                            "to have the same content, but actual stream differs at pos $size. Actual stream: value=0x${actualBuffer[pos].toHexString()}, size=$actualSize. Other stream: value=0x${otherBuffer[pos].toHexString()}, size=$otherSize"
                        }
                    }
                }
            }

            // the following throw should be unnecessary, as the only way to leave the while loop is either
            // - somewhere in the while loop an exception is thrown
            // - somwhere in the while loop the method is left by a return statement
            throw IllegalStateException("unreachable code")
        }
    }
}