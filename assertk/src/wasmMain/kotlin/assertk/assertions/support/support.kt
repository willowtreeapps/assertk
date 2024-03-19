package assertk.assertions.support

internal actual fun displayPlatformSpecific(value: Any?): String {
    return when (value) {
        is Byte -> formatHex(value)
        else -> value.toString()
    }
}

//TODO: replace with HexFormat when stable
private fun formatHex(byte: Byte) : String {
    val topNibble = (byte.toInt() and 0xF0) ushr 4
    val bottomNibble = byte.toInt() and 0x0F
    return "0x${topNibble.toString(16).uppercase()}${bottomNibble.toString(16).uppercase()}"
}