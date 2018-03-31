package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show

/**
 * Returns an assert that assertion on the value at the given index in the list.
 *
 * ```
 * assert(listOf(0, 1, 2)).index(1) { it.isPositive() }
 * ```
 */
fun <T> Assert<List<T>>.index(index: Int, f: (Assert<T>) -> Unit) {
    if (index in 0 until actual.size) {
        f(assert(actual[index], "${name ?: ""}${show(index, "[]")}"))
    } else {
        expected("index to be in range:[0-${actual.size}) but was:${show(index)}")
    }
}

