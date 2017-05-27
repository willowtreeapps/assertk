package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

fun <T : Number> Assert<T>.isZero() {
    if (actual == 0) return
    expected("to be 0 but was:${show(actual)}")
}

fun <T : Number> Assert<T>.isNotZero() {
    if (actual != 0) return
    expected("to not be 0")
}

fun <T> Assert<T>.isPositive() where T : Number, T : Comparable<T> {
    @Suppress("UNCHECKED_CAST")
    if (actual > 0 as T) return
    expected("to be positive but was:${show(actual)}")
}

fun <T> Assert<T>.isNegative() where T : Number, T : Comparable<T> {
    @Suppress("UNCHECKED_CAST")
    if (actual < 0 as T) return
    expected("to be negative but was:${show(actual)}")
}
