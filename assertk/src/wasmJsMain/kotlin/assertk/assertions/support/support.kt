package assertk.assertions.support

internal actual fun displayPlatformSpecific(value: Any?): String {
    return when (value) {
        is Byte -> formatHex(value)
        is Short -> formatHex(value)
        else -> value.toString()
    }
}

//TODO: replace with HexFormat when stable
private fun formatHex(byte: Byte) : String {
    val topNibble = (byte.toInt() and 0xF0) ushr 4
    val bottomNibble = byte.toInt() and 0x0F
    return "0x${topNibble.toString(16).uppercase()}${bottomNibble.toString(16).uppercase()}"
}

private fun formatHex(short: Short) : String {
    val nibble1 = (short.toInt() and 0xF000) ushr 12
    val nibble2 = (short.toInt() and 0x0F00) ushr 8
    val nibble3 = (short.toInt() and 0x00F0) ushr 4
    val nibble4 = short.toInt() and 0x000F
    return "0x${nibble1.toString(16).uppercase()}${nibble2.toString(16).uppercase()}${nibble3.toString(16).uppercase()}${nibble4.toString(16).uppercase()}"
}
