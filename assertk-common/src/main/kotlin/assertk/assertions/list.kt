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
fun <T> Assert<List<T>>.index(index: Int, f: (Assert<T>) -> Unit) {
    if (index in 0 until actual.size) {
        f(assert(actual[index], "${name ?: ""}${show(index, "[]")}"))
    } else {
        expected("index to be in range:[0-${actual.size}) but was:${show(index)}")
    }
}

/**
 * Asserts the list contains exactly the expected elements. They must be in the same order and
 * there must not be any extra elements.
 * @see [containsAll]
 */
fun <T : List<*>> Assert<T>.containsExactly(vararg elements: Any?) {
    if (actual == elements.asList()) return

    val diff = ListDiffer.diff(elements.asList(), actual)
        .filterNot { it is ListDiffer.Edit.Eq }

    expected(diff.joinToString(prefix = "to contain exactly:\n", separator = "\n") { edit ->
        when (edit) {
            is ListDiffer.Edit.Del -> " at index:${edit.oldIndex} expected:${show(edit.oldValue)}"
            is ListDiffer.Edit.Ins -> " at index:${edit.newIndex} unexpected:${show(edit.newValue)}"
            else -> throw IllegalStateException()
        }
    })
}
