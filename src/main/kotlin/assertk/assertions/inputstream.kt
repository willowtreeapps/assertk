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

    while (true) {
        val valueActual = actual.read()
        val valueOther = other.read()

        if (valueActual == valueOther) {

            if (valueActual == -1) {
                // stream are empty now and no previous differences found, streams have the same content
                return
            } else {
                size++
            }

        } else {
            if (valueActual == -1) {

                val actualSize = consume(actual) + size
                val otherSize = consume(other) + size + 1
                expected("to have the same size, but actual stream size ($actualSize) differs from other stream size ($otherSize)")

            } else if (valueOther == -1) {

                val actualSize = consume(actual) + size + 1
                val otherSize = consume(other) + size
                expected("to have the same size, but actual stream size ($actualSize) differs from other stream size ($otherSize)")

            } else if (valueOther != valueActual) {

                val actualSize = consume(actual) + size + 1
                val otherSize = consume(other) + size + 1
                expected("to have the same content, but actual stream differs at pos $size. Actual stream: value=0x${valueActual.toHexString()}, size=$actualSize. Other stream: value=0x${valueOther.toHexString()}, size=$otherSize")

            }
        }
    }
}

/** Asserts that the actual stream has a different content as the other stream.
 *
 * @param other whichs content is compared to the actual one
 */
fun Assert<InputStream>.hasNotSameContentAs(other: InputStream) {
    val buf1 = ByteArray(BUFFER_SIZE)
    val buf2 = ByteArray(BUFFER_SIZE)

    var equals: Boolean
    while (true) {
        val len1 = actual.read(buf1)
        val len2 = other.read(buf2)

        if (len1 != len2) {
            equals = false
            break
        }
        if (len1 == -1) {
            equals = true
            break
        }

        if (!buf1.contentEquals(buf2)) {
            equals = false
            break
        }
    }

    if (equals) {
        expected("stream not to be equal, but they were equal")
    }
}

private fun Int.toHexString() = String.format("%02X", toByte())

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