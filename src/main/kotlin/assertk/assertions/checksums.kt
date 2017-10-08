package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import java.io.InputStream
import java.security.MessageDigest
import java.util.zip.CRC32


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



private const val BUFFER_SIZE = 4096

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
    this.asSequence().map { String.format("%02X", it) }.joinTo(sb, separator)
    return sb.toString()
}
