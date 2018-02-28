package assertk.assertions

import assertk.Assert
import assertk.assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Asserts the array is empty.
 * @see [isNotEmpty]
 * @see [isNullOrEmpty]
 */
@JvmName("arrayIsEmpty")
fun <T> Assert<Array<T>>.isEmpty() {
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the array is not empty.
 * @see [isEmpty]
 */
@JvmName("arrayIsNotEmpty")
fun <T> Assert<Array<T>>.isNotEmpty() {
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

/**
 * Asserts the array is null or empty.
 * @see [isEmpty]
 */
@JvmName("arrayIsNullOrEmpty")
fun <T> Assert<Array<T?>?>.isNullOrEmpty() {
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

/**
 * Asserts the array has the expected size.
 */
@JvmName("arrayHasSize")
fun <T> Assert<Array<T>>.hasSize(size: Int) {
    assert(actual.size, "size").isEqualTo(size)
}

/**
 * Asserts the array contains the expected element, using `in`.
 * @see [doesNotContain]
 */
@JvmName("arrayContains")
fun <T> Assert<Array<T>>.contains(element: Any?) {
    if (element in actual) return
    expected("to contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the array does not contain the expected element, using `!in`.
 * @see [contains]
 */
@JvmName("arrayDoesNotContain")
fun <T> Assert<Array<T>>.doesNotContain(element: Any?) {
    if (element !in actual) return
    expected("to not contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the array contains all the expected elements, in any order. The array may also contain
 * additional elements.
 * @see [containsExactly]
 */
@JvmName("arrayContainsAll")
fun <T> Assert<Array<T>>.containsAll(vararg elements: Any?) {
    if (elements.all { actual.contains(it) }) return
    expected("to contain all:${show(elements)} but was:${show(actual)}")
}

/**
 * Asserts the array contains exactly the expected elements. They must be in the same order and
 * there must not be any extra elements.
 * @see [containsAll]
 */
@JvmName("arrayContainsExactly")
fun <T> Assert<Array<T>>.containsExactly(vararg elements: Any?) {
    if (actual.size == elements.size) {
        for (i in 0 until actual.size) {
            if (actual[i] != elements[i]) {
                expected("to contain exactly:${show(elements)} but was:${show(actual)}")
                break
            }
        }
    } else {
        expected("to contain exactly:${show(elements)} but was:${show(actual)}")
    }
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
@JvmName("arrayEach")
fun <T> Assert<Array<T>>.each(f: (Assert<T>) -> Unit) {
    actual.forEachIndexed { index, item -> f(assert(item, "${name ?: ""}${show(index, "[]")}")) }
}
