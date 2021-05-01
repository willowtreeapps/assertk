package assertk.assertions

import assertk.Assert
import assertk.assertions.support.appendName
import assertk.assertions.support.expected
import assertk.assertions.support.expectedListDiff
import assertk.assertions.support.show

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
 * [1, 2] containsOnly [2, 1] fails
 * [1, 2, 2] containsOnly [2, 1] fails
 * [1, 2] containsOnly [2, 2, 1] fails
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

/**
 * Asserts that a collection contains a subset of items the same order, but may have other items in the list.
 *
 * Usages:
 *
 * - `[]` containsAllInOrder `[1,2,3]` fails
 * - `[1,2,3]` containsAllInOrder `[4,5,6]` fails
 * - `[]` containsAllInOrder `[]` pass
 * - `[1,2]` containsAllInOrder `[1,2,3]` pass
 * - `[2,3,4]` containsAllInOrder `[1,2,3,4,5]` pass
 *
 * @param sublist The list of items it the actual list should contain in the same order.
 */
fun Assert<List<*>>.containsAllInOrder(sublist: List<*>) = given { actual: List<*> ->

    if (sublist.isEmpty() && actual.isEmpty()) return@given

    val sublistMatchCount =
        when (val firstMatchOfSublist = if (sublist.isEmpty()) -1 else actual.indexOf(sublist.first())) {
            -1 -> 0
            else -> {
                var n = 1
                while (n < sublist.size && n < actual.size) {
                    val a = actual[firstMatchOfSublist + n]
                    val b = sublist[n]
                    if (a != b) break
                    n += 1
                }
                n
            }
        }

    if (sublist.isEmpty() || sublistMatchCount < sublist.size) expected(
        "to contain the exact sublist (in the same order) as:${
            show(sublist)
        }, but found none matching in:${
            show(actual)
        }"
    )
}
