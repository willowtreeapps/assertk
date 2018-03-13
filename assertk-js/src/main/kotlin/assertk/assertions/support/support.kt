package assertk.assertions.support

actual
@Suppress("UndocumentedPublicFunction")
internal fun displayPlatformSpecific(value: Any?): String {
    return value.toString()
}
