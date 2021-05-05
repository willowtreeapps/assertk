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
 * - `[]` containsSubList `[1,2,3]` fails
 * - `[1,2,3]` containsSubList `[4,5,6]` fails
 * - `[]` containsSubList `[]` pass
 * - `[1,2]` containsSubList `[1,2,3]` pass
 * - `[2,3,4]` containsSubList `[1,2,3,4,5]` pass
 *
 * @param sublist The list of items it the actual list should contain in the same order.
 */
fun Assert<List<*>>.containsSubList(sublist: List<*>) = given { actual: List<*> ->

    var sublistMatched = actual.isEmpty() && sublist.isEmpty()
    var target: List<*> = actual

    while (!sublistMatched) {
        val matchOfFirstInTarget = target.indexOf(sublist.first())
        if (matchOfFirstInTarget == -1) break
        var n = 1
        while (n < sublist.size && n < target.size) {
            val a = target[matchOfFirstInTarget + n]
            val b = sublist[n]
            if (a != b) break
            n += 1
        }
        sublistMatched = (n == sublist.size)
        if (sublistMatched) break
        if (matchOfFirstInTarget + n == target.size) break
        target = target.subList(matchOfFirstInTarget + 1, target.size)
    }

    if (sublistMatched) return@given

    expected(
        "to contain the exact sublist (in the same order) as:${
            show(sublist)
        }, but found none matching in:${
            show(actual)
        }"
    )
}