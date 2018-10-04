package assertk.assertions

import assertk.Assert
import assertk.PlatformName
import assertk.assertAll
import assertk.assertions.support.ListDiffer
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show

/**
 * Returns an assert on the Arrays's size.
 */
fun <T> Assert<Array<T>>.size() = prop("size", Array<T>::size)

/**
 * Asserts the array contents are equal to the expected one, using [contentDeepEquals].
 * @see isNotEqualTo
 */
fun <T> Assert<Array<T>>.isEqualTo(expected: Array<T>) {
    if (actual.contentDeepEquals(expected)) return
    fail(expected, actual)
}

/**
 * Asserts the array contents are not equal to the expected one, using [contentDeepEquals].
 * @see isEqualTo
 */
fun <T> Assert<Array<T>>.isNotEqualTo(expected: Array<T>) {
    if (!(actual.contentDeepEquals(expected))) return
    val showExpected = show(expected)
    val showActual = show(actual)
    // if they display the same, only show one.
    if (showExpected == showActual) {
        expected("to not be equal to:$showActual")
    } else {
        expected(":$showExpected not to be equal to:$showActual")
    }
}

/**
 * Asserts the array is empty.
 * @see [isNotEmpty]
 * @see [isNullOrEmpty]
 */
@PlatformName("arrayIsEmpty")
fun <T> Assert<Array<T>>.isEmpty() {
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the array is not empty.
 * @see [isEmpty]
 */
@PlatformName("arrayIsNotEmpty")
fun <T> Assert<Array<T>>.isNotEmpty() {
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

/**
 * Asserts the array is null or empty.
 * @see [isEmpty]
 */
@PlatformName("arrayIsNullOrEmpty")
fun <T> Assert<Array<T>?>.isNullOrEmpty() {
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

/**
 * Asserts the array has the expected size.
 */
@PlatformName("arrayHasSize")
fun <T> Assert<Array<T>>.hasSize(size: Int) {
    assert(actual.size, "size").isEqualTo(size)
}

/**
 * Asserts the array has the same size as the expected array.
 */
@PlatformName("arrayHasSameSizeAs")
fun <T> Assert<Array<T>>.hasSameSizeAs(other: Array<*>) {
    val actualSize = actual.size
    val otherSize = other.size
    if (actualSize == otherSize) return
    expected("to have same size as:${show(other)} ($otherSize) but was size:($actualSize)")
}

/**
 * Asserts the array contains the expected element, using `in`.
 * @see [doesNotContain]
 */
@PlatformName("arrayContains")
fun <T> Assert<Array<T>>.contains(element: Any?) {
    if (element in actual) return
    expected("to contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the array does not contain the expected element, using `!in`.
 * @see [contains]
 */
@PlatformName("arrayDoesNotContain")
fun <T> Assert<Array<T>>.doesNotContain(element: Any?) {
    if (element !in actual) return
    expected("to not contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the collection does not contain any of the expected elements.
 * @see [containsAll]
 */
fun <T> Assert<Array<T>>.containsNone(vararg elements: Any?) {
    if (elements.none { it in actual }) {
        return
    }

    val notExpected = elements.filter { it in actual }
    expected("to contain none of:${show(elements)} some elements were not expected:${show(notExpected)}")
}

/**
 * Asserts the array contains all the expected elements, in any order. The array may also contain
 * additional elements.
 * @see [containsExactly]
 */
@PlatformName("arrayContainsAll")
fun <T> Assert<Array<T>>.containsAll(vararg elements: Any?) {
    if (elements.all { actual.contains(it) }) return
    val notFound = elements.filterNot { it in actual }
    expected("to contain all:${show(elements)} some elements were not found:${show(notFound)}")
}

/**
 * Returns an assert that assertion on the value at the given index in the array.
 *
 * ```
 * assert(arrayOf(0, 1, 2)).index(1) { it.isPositive() }
 * ```
 */
fun <T> Assert<Array<T>>.index(index: Int, f: (Assert<T>) -> Unit) {
    if (index in 0 until actual.size) {
        f(assert(actual[index], "${name ?: ""}${show(index, "[]")}"))
    } else {
        expected("index to be in range:[0-${actual.size}) but was:${show(index)}")
    }
}

/**
 * Asserts the array contains exactly the expected elements. They must be in the same order and
 * there must not be any extra elements.
 * @see [containsAll]
 * @see [containsOnly]
 */
@PlatformName("arrayContainsExactly")
fun <T> Assert<Array<T>>.containsExactly(vararg elements: Any?) {
    if (actual.contentEquals(elements)) return

    val diff = ListDiffer.diff(elements.asList(), actual.asList())
        .filterNot { it is ListDiffer.Edit.Eq }

    expected(diff.joinToString(prefix = "to contain exactly:\n", separator = "\n") { edit ->
        when (edit) {
            is ListDiffer.Edit.Del -> " at index:${edit.oldIndex} expected:${show(edit.oldValue)}"
            is ListDiffer.Edit.Ins -> " at index:${edit.newIndex} unexpected:${show(edit.newValue)}"
            else -> throw IllegalStateException()
        }
    })
}

/**
 * Asserts on each item in the array. The given lambda will be run for each item.
 *
 * ```
 * assert(arrayOf("one", "two")).each {
 *   it.hasLength(3)
 * }
 * ```
 */
@PlatformName("arrayEach")
fun <T> Assert<Array<T>>.each(f: (Assert<T>) -> Unit) {
    assertAll {
        actual.forEachIndexed { index, item ->
            f(assert(item, "${name ?: ""}${show(index, "[]")}"))
        }
    }
}
