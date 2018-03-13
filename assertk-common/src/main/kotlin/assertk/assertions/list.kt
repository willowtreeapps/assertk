package assertk.assertions

import assertk.Assert
import assertk.assertions.support.show

/**
 * Returns an assert that assertion on the value at the given index in the list.
 *
 * ```
 * assert(listOf(0, 1, 2)).index(1).isPositive()
 * ```
 */
fun <T> Assert<List<T>>.index(index: Int)
        = assert(actual[index], "${name ?: ""}${show(index, "[]")}")

