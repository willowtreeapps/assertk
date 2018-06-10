package assertk.assertions.support

internal actual fun displayPlatformSpecific(value: Any?): String {
    "".toList().joinToString(transform = { it.toByte().toString()})
    return value.toString()

}
