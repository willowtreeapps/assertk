@file:JvmName("SupportJVMKt")
package assertk.assertions.support

actual
@Suppress("UndocumentedPublicFunction")
internal fun displayPlatformSpecific(value: Any?): String {
    return when (value) {
        is Byte -> "0x%02X".format(value)
        is Float -> "${value}f"
        is ByteArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is DoubleArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is FloatArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is IntArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is ShortArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is Class<*> -> value.name
        else -> value.toString()
    }
}

