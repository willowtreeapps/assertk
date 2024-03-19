package assertk

import assertk.assertions.support.show

internal actual fun showError(e: Throwable) = show(e)