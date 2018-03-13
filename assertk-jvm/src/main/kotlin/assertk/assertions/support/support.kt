@file:JvmName("SupportJVMKt")
package assertk.assertions.support

actual
@Suppress("UndocumentedPublicFunction")
internal fun displayPlatformSpecific(value: Any?): String {
    return when (value) {
        is Class<*> -> value.name
        else -> value.toString()
    }
}

