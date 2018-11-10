package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import java.io.Closeable
import java.io.InputStream

/** Asserts that the actual stream has the same content as the expected stream.
 * Both [InputStream]s will be closed by the assertion.
 *
 * @param expected which content is compared to the actual one
 */
fun Assert<InputStream>.hasSameContentAs(expected: InputStream) = given { actual ->
    val msg = doTheStreamHaveTheSameContent(actual, expected)
    if (msg != null) {
        expected(msg)
    }

}

/** Asserts that the actual stream has a different content as the expected stream.
 * Both [InputStream]s will be closed by the assertion.
 *
 * @param expected which content is compared to the actual one
 */
fun Assert<InputStream>.hasNotSameContentAs(expected: InputStream) = given { actual ->
    val msg = doTheStreamHaveTheSameContent(actual, expected)
    if (msg == null) {
        expected("streams not to be equal, but they were equal")
    }
}

private const val BUFFER_SIZE = 4096

private fun Byte.toHexString() = String.format("%02X", this)

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

private fun compare(len: Int, actual: ByteArray, expected: ByteArray): Int? {
    return (0 until len).firstOrNull { actual[it] != expected[it] }
}

@Suppress("NestedBlockDepth")
private fun doTheStreamHaveTheSameContent(actual: InputStream, expected: InputStream): String? = use(actual, expected) {
    var size = 0L

    val actualBuffer = ByteArray(BUFFER_SIZE)
    val otherBuffer = ByteArray(BUFFER_SIZE)

    while (true) {
        val actualRead = fillBuffer(actual, actualBuffer)
        val otherRead = fillBuffer(expected, otherBuffer)

        if (actualRead == otherRead) {
            if (actualRead == 0) {
                // the streams have the same size and are finished and have the same content
                return null
            }

            val pos = compare(actualRead, actualBuffer, otherBuffer)
            if (pos == null) {
                // the current buffers are equals and we can try the next block
                size += actualRead
            } else {
                // the first difference is in position pos
                val actualSize = size + actualRead + consume(actual)
                val otherSize = size + otherRead + consume(expected)

                return "to have the same content, but actual stream differs at pos $size." +
                        " Actual stream: value=0x${actualBuffer[pos].toHexString()}, size=$actualSize." +
                        " Other stream: value=0x${otherBuffer[pos].toHexString()}, size=$otherSize"
            }
        } else {
            if (actualRead == 0) {
                val actualSize = actualRead + size
                val otherSize = consume(expected) + otherRead + size

                return "to have the same size," +
                        " but actual stream size ($actualSize) differs from other stream size ($otherSize)"
            }

            if (otherRead == 0) {
                val actualSize = consume(actual) + actualRead + size
                val otherSize = otherRead + size

                return "to have the same size," +
                        " but actual stream size ($actualSize) differs from other stream size ($otherSize)"
            }

            val pos = compare(Math.min(actualRead, otherRead), actualBuffer, otherBuffer)
            val actualSize = consume(actual) + actualRead + size
            val otherSize = consume(expected) + otherRead + size

            return if (pos == null) {
                "to have the same size," +
                        " but actual size ($actualSize) differs from other size ($otherSize)"
            } else {
                "to have the same content, but actual stream differs at pos $size." +
                        " Actual stream: value=0x${actualBuffer[pos].toHexString()}, size=$actualSize." +
                        " Other stream: value=0x${otherBuffer[pos].toHexString()}, size=$otherSize"
            }
        }
    }

    // the following throw should be unnecessary, as the only way to leave the while loop is either
    // - somewhere in the while loop an exception is thrown
    // - somewhere in the while loop the method is left by a return statement
    throw IllegalStateException("unreachable code")
}

private inline fun <R> use(a: Closeable, b: Closeable, f: () -> R): R {
    return a.use { b.use { f() } }
}

