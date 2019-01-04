package assertk.assertions

import assertk.Assert
import assertk.assertions.support.ListDiffer
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Returns an assert that assertion on the value at the given index in the list.
 *
 * ```
 * assert(listOf(0, 1, 2)).index(1) { it.isPositive() }
 * ```
 */
@Deprecated(message = "Use index(index) instead.", replaceWith = ReplaceWith("index(index).let(f)"))
fun <T> Assert<List<T>>.index(index: Int, f: (Assert<T>) -> Unit) {
    index(index).let(f)
}

/**
 * Returns an assert that assertion on the value at the given index in the list.
 *
 * ```
 * assert(listOf(0, 1, 2)).index(1).isPositive()
 * ```
 */
fun <T> Assert<List<T>>.index(index: Int): Assert<T> =
    transform("${name ?: ""}${show(index, "[]")}") { actual ->
        if (index in 0 until actual.size) {
            actual[index]
        } else {
            expected("index to be in range:[0-${actual.size}) but was:${show(index)}")
        }
    }

/**
 * Asserts the list contains exactly the expected elements. They must be in the same order and
 * there must not be any extra elements.
 * @see [containsAll]
 */
fun Assert<List<*>>.containsExactly(vararg elements: Any?) = given { actual ->
    if (actual == elements.asList()) return

    expected(listDifferExpected(elements.toList(), actual))
}

internal fun listDifferExpected(elements: List<Any?>, actual: List<Any?>): String {
    val diff = ListDiffer.diff(elements, actual)
        .filterNot { it is ListDiffer.Edit.Eq }
        .sortedBy { when(it) {
            is ListDiffer.Edit.Ins -> it.newIndex
            is ListDiffer.Edit.Del -> it.oldIndex
            else -> throw IllegalStateException()
        } }

    return diff.joinToString(prefix = "to contain exactly:\n", separator = "\n") { edit ->
        when (edit) {
            is ListDiffer.Edit.Del -> " at index:${edit.oldIndex} expected:${show(edit.oldValue)}"
            is ListDiffer.Edit.Ins -> " at index:${edit.newIndex} unexpected:${show(edit.newValue)}"
            else -> throw IllegalStateException()
        }
    }
}