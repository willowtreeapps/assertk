package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import java.io.InputStream

/** Asserts that the actual stream has the same content as the other stream.
 *
 * @param other whichs content is compared to the actual one
 */
fun Assert<InputStream>.hasSameContentAs(other: InputStream) {

    var size = 0L

    val actualBuffer = ByteArray(BUFFER_SIZE)
    val otherBuffer = ByteArray(BUFFER_SIZE)

    while (true) {
        val actualRead = fillBuffer(actual, actualBuffer)
        val otherRead = fillBuffer(other, otherBuffer)

        if (actualRead == otherRead) {

            if(actualRead==0) {
                // the streams have the same size and are finished and have the same content
                return
            }
            else {
                val pos = compare(actualRead, actualBuffer, otherBuffer)
                if(pos==null) {
                    // the current buffers are equals and we can try the next block
                    size += actualRead

                } else {
                    // the first difference is in position pos

                    val actualSize = size + actualRead + consume(actual)
                    val otherSize = size + otherRead + consume(other)

                    expected("to have the same content, but actual stream differs at pos $size. Actual stream: value=0x${actualBuffer[pos].toHexString()}, size=$actualSize. Other stream: value=0x${otherBuffer[pos].toHexString()}, size=$otherSize")
                }
            }

        } else {

            if(actualRead==0) {
                val actualSize = actualRead + size
                val otherSize = consume(other) + otherRead + size
                expected("to have the same size, but actual stream size (${actualSize}) differs from other stream size ($otherSize)")
            }
            else if (otherRead==0) {
                val actualSize = consume(actual) + actualRead + size
                val otherSize = otherRead + size
                expected("to have the same size, but actual stream size ($actualSize) differs from other stream size (${otherSize})")
            }
            else {
                val pos = compare(Math.min(actualRead, otherRead), actualBuffer, otherBuffer)
                val actualSize = consume(actual) + actualRead + size
                val otherSize = consume(other) + otherRead + size
                if(pos==null) {
                    expected("to have the same size, but actual size (${actualSize}) differs from other size (${otherSize})")
                }
                else {
                    expected("to have the same content, but actual stream differs at pos $size. Actual stream: value=0x${actualBuffer[pos].toHexString()}, size=$actualSize. Other stream: value=0x${otherBuffer[pos].toHexString()}, size=$otherSize")
                }
            }
        }
    }
}

/** Asserts that the actual stream has a different content as the other stream.
 *
 * @param other whichs content is compared to the actual one
 */
fun Assert<InputStream>.hasNotSameContentAs(other: InputStream) {
    try {
        hasSameContentAs(other)
    }
    catch(e: AssertionError) {
        return
    }
    expected("streams not to be equal, but they were equal")
}

private fun Byte.toHexString() = String.format("%02X", this)

private const val BUFFER_SIZE = 4096
private fun consume(stream: InputStream): Int {
    val buffer = ByteArray(BUFFER_SIZE)

    var consumed = 0
    while (true) {
        val len = stream.read(buffer)
        if (len < 0) return consumed
        consumed += len
    }
}

private fun fillBuffer(stream: InputStream, buffer: ByteArray): Int {
    var pos = 0

    while(true) {
        val len = stream.read(buffer, pos, BUFFER_SIZE-pos)
        if(len==-1) return if(pos<0) 0 else pos
        pos += len
        if(pos==BUFFER_SIZE) return pos
    }
}

private fun compare(len: Int, actual: ByteArray, other: ByteArray): Int? {
    for(i in 0 until len) {
        if(actual[i]!=other[i]) {
            return i
        }
    }

    return null
}