package assertk.assertions.support

internal actual fun displayPlatformSpecific(value: Any?): String {
    return value.toString()
}
