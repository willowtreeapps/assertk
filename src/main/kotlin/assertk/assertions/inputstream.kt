package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import java.io.InputStream

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

private fun Byte.toHexString() = String.format("%02X", this)

private const val BUFFER_SIZE = 4096

private fun consume(stream: InputStream): Int {
    val buffer = ByteArray(BUFFER_SIZE)

    var consumed = 0
    while (true) {
        val len = stream.read(buffer)
        if (len < 0) {
            stream.close()
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
            stream.close()
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

    val actualStream = InputStreamWithIdempotentCloseCall(actual)
    val otherStream = InputStreamWithIdempotentCloseCall(other)

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
                if (pos == null) {
                    return "to have the same size, but actual size (${actualSize}) differs from other size (${otherSize})"
                } else {
                    return "to have the same content, but actual stream differs at pos $size. Actual stream: value=0x${actualBuffer[pos].toHexString()}, size=$actualSize. Other stream: value=0x${otherBuffer[pos].toHexString()}, size=$otherSize"
                }
            }
        }
    }
}

/** As you can't ask if an InputStream was already closed this wrapper will record it. */
private class InputStreamWithIdempotentCloseCall(private val original: InputStream) : InputStream() {
    private var closedCalled = false

    override fun read() = original.read()
    override fun close() {
        if (!closedCalled) {
            closedCalled = true
            super.close()
        }
    }
}