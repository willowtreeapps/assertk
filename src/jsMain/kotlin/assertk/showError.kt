package assertk

import assertk.assertions.support.show

internal actual fun Assert<Any?>.showError(e: Throwable) = show(e)