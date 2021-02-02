package assertk.assertions

import assertk.Assert
import assertk.assertions.support.*

/**
 * Returns an assert that assertion on the value at the given index in the list.
 *
 * ```
 * assertThat(listOf(0, 1, 2)).index(1).isPositive()
 * ```
 */
fun <T> Assert<List<T>>.index(index: Int): Assert<T> =
    transform(appendName(show(index, "[]"))) { actual ->
        if (index in actual.indices) {
            actual[index]
        } else {
            expected("index to be in range:[0-${actual.size}) but was:${show(index)}")
        }
    }

/**
 * Asserts the list contains exactly the expected elements. They must be in the same order and
 * there must not be any extra elements.
 *
 * [1, 2] containsExactly [2, 1] fails
 * [1, 2, 2] containsExactly [2, 1] fails
 * [1, 2] containsExactly [2, 2, 1] fails
 *
 * @see [containsAll]
 * @see [containsOnly]
 * @see [containsExactlyInAnyOrder]
 */
fun Assert<List<*>>.containsExactly(vararg elements: Any?) = given { actual ->
    if (actual.contentEquals(elements)) return

    expectedListDiff(elements.toList(), actual)
}

/**
 * Checks if the contents of the list is the same as the given array.
 */
private fun List<*>.contentEquals(other: Array<*>): Boolean {
    if (size != other.size) return false
    for (i in 0 until size) {
        if (get(i) != other[i]) return false
    }
    return true
}