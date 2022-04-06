package assertk.assertions

import assertk.Assert
import assertk.assertions.support.appendName
import assertk.assertions.support.expected
import assertk.assertions.support.expectedListDiff
import assertk.assertions.support.show

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
        while (n < sublist.size && matchOfFirstInTarget + n < target.size) {
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

/**
 * Asserts the list starts with the expected elements, in the same order.
 *
 * [1, 2, 3] startsWith [1, 2] pass
 * [1, 2, 3] startsWith [2, 1] fails
 * [1, 2, 3] startsWith [1, 2, 3] pass
 * [] startsWith [1, 2] fails
 * [1, 2] startsWith [] pass
 * [] startsWith [] pass
 *
 * @see[endsWith]
 */
fun Assert<List<*>>.startsWith(vararg elements: Any?) = given { actual ->
    val sublist = if (actual.size >= elements.size) {
        actual.subList(0, elements.size)
    } else {
        actual
    }

    if (sublist.contentEquals(elements)) return

    expected(
        "to start with:${
            show(elements)
        }, but was:${
            show(sublist)
        } in:${
            show(actual)
        }"
    )
}

/**
 * Asserts the list ends with the expected elements, in the same order.
 *
 * [1, 2, 3] endsWith [2, 3] pass
 * [1, 2, 3] endsWith [3, 2] fails
 * [1, 2, 3] endsWith [1, 2, 3] pass
 * [] endsWith [1, 2] fails
 * [1, 2] endsWith [] pass
 * [] endsWith [] pass
 *
 * @see[startsWith]
 */
fun Assert<List<*>>.endsWith(vararg elements: Any?) = given { actual ->
    val sublist = if (actual.size >= elements.size) {
        actual.subList(actual.size - elements.size, actual.size)
    } else {
        actual
    }

    if (sublist.contentEquals(elements)) return

    expected(
        "to end with:${
            show(elements)
        }, but was:${
            show(sublist)
        } in:${
            show(actual)
        }"
    )
}
