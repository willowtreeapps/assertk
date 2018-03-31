package assertk.assertions

import assertk.Assert
import assertk.assertions.support.*

/**
 * Returns an assert that assertion on the value at the given index in the list.
 *
 * ```
 * assert(listOf(0, 1, 2)).index(1) { it.isPositive() }
 * ```
 */
fun <T> Assert<List<T>>.index(index: Int, f: (Assert<T>) -> Unit) {
    if (index in 0 until actual.size)
        {
        f( assert(actual[index], "${name ?: ""}${show(index, "[]")}"))
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

    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override val oldListSize: Int = elements.size
        override val newListSize: Int = actual.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            elements[oldItemPosition] == actual[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = true
    }, detectMoves = false)

    val diffList = diff.createDiffList(elements.asList(), actual)

    expected(diffList.joinToString(prefix = "to contain exactly:\n", separator = "\n") { (index, item, op) ->
        when (op) {
            DiffUtil.DiffResult.OP_REMOVE -> "[$index] missing expected element:${show(item)}"
            DiffUtil.DiffResult.OP_ADD -> "[$index] extra actual element:${show(item)}"
            DiffUtil.DiffResult.OP_UNCHANGED -> "[$index] ${show(item)}"
            else -> throw AssertionError()
        }
    })
}
