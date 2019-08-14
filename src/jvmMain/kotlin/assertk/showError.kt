package assertk

import assertk.assertions.support.show
import java.io.PrintWriter
import java.io.StringWriter

internal actual fun Assert<Any?>.showError(e: Throwable): String {
    val stackTrace = StringWriter()
    e.printStackTrace(PrintWriter(stackTrace))
    return "${show(e)}\n$stackTrace"
}