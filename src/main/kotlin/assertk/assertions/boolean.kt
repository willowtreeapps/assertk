package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected

fun Assert<Boolean>.isTrue() {
    if (actual) return
    expected("to be true")
}

fun Assert<Boolean>.isFalse() {
    if (!actual) return
    expected("to be false")
}
