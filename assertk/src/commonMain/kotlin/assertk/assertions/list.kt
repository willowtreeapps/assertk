package assertk.assertions

import assertk.Assert
import assertk.assertions.support.ListDiffer
import assertk.assertions.support.appendName
import assertk.assertions.support.expected
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
 * [1, 2] containsExactly [2, 1] fails
 * [1, 2, 2] containsExactly [2, 1] fails
 * [1, 2] containsExactly [2, 2, 1] fails
 *
 * @see [containsAll]
 * @see [containsOnly]
 * @see [containsExactlyInAnyOrder]
 */
fun Assert<List<*>>.containsExactly(vararg elements: Any?) = given { actual ->
    val expected = elements.toList()
    if (actual == expected) return

    expected(listDifferExpected(expected, actual), expected, actual)
}

internal fun listDifferExpected(elements: List<Any?>, actual: List<Any?>): String {
    val diff = ListDiffer.diff(elements, actual)
        .filterNot { it is ListDiffer.Edit.Eq }
        .sortedBy { when(it) {
            is ListDiffer.Edit.Ins -> it.newIndex
            is ListDiffer.Edit.Del -> it.oldIndex
            else -> throw IllegalStateException()
        } }

    return diff.joinToString(prefix = "to contain exactly:${show(elements)} but was:${show(actual)}\n", separator = "\n") { edit ->
        when (edit) {
            is ListDiffer.Edit.Del -> " at index:${edit.oldIndex} expected:${show(edit.oldValue)}"
            is ListDiffer.Edit.Ins -> " at index:${edit.newIndex} unexpected:${show(edit.newValue)}"
            else -> throw IllegalStateException()
        }
    }
}