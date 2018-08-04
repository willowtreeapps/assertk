package assertk.assertions.support

import kotlin.reflect.KClass

internal actual fun displayPlatformSpecific(value: Any?): String {
    return when (value) {
        is KClass<*> -> {
            val name = value.qualifiedName ?: value.simpleName
            if (name != null) "class $name" else value.toString()
        }
        else -> value.toString()
    }
}
