package assertk.assertions

import assertk.Assert
import assertk.assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Asserts the collection is empty.
 * @see [isNotEmpty]
 * @see [isNullOrEmpty]
 */
fun <T : Collection<*>> Assert<T>.isEmpty() {
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the collection is not empty.
 * @see [isEmpty]
 */
fun <T : Collection<*>> Assert<T>.isNotEmpty() {
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

/**
 * Asserts the collection is null or empty.
 * @see [isEmpty]
 */
fun <T : Collection<*>?> Assert<T>.isNullOrEmpty() {
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

/**
 * Asserts the collection has the expected size.
 */
fun <T : Collection<*>> Assert<T>.hasSize(size: Int) {
    assert(actual.size, "size").isEqualTo(size)
}

/**
 * Asserts the collection has the same size as the expected collection.
 */
fun <T : Collection<*>> Assert<T>.hasSameSizeAs(other: Collection<*>) {
    val actualSize = actual.size
    val otherSize = other.size
    if (actualSize == otherSize) return
    expected("to have same size as:${show(other)} ($otherSize) but was size:($actualSize)")
}

/**
 * Asserts the collection contains the expected element, using `in`.
 * @see [doesNotContain]
 */
fun <T : Collection<*>> Assert<T>.contains(element: Any?) {
    if (element in actual) return
    expected("to contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the collection does not contain the expected element, using `!in`.
 * @see [contains]
 */
fun <T : Collection<*>> Assert<T>.doesNotContain(element: Any?) {
    if (element !in actual) return
    expected("to not contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the collection does not contain any of the expected elements.
 * @see [containsAll]
 */
fun <T : Collection<*>> Assert<T>.containsNone(vararg elements: Any?) {
    val elementsItr = elements.iterator()
    while (elementsItr.hasNext()) {
        if (actual.contains(elementsItr.next())) {
            expected("to contain none of:${show(elements)} but was:${show(actual)}")
            break
        }
    }
}

/**
 * Asserts the collection contains all the expected elements, in any order. The collection may also
 * contain additional elements.
 * @see [containsNone]
 * @see [containsExactly]
 */
fun <T : Collection<*>> Assert<T>.containsAll(vararg elements: Any?) {
    if (!actual.containsAll(elements.toList())) {
        expected("to contain all:${show(elements)} but was:${show(actual)}")
    }
}

/**
 * Asserts the collection contains exactly the expected elements. They must be in the same order and
 * there must not be any extra elements.
 * @see [containsAll]
 */
fun <T : Collection<*>> Assert<T>.containsExactly(vararg elements: Any?) {
    if (actual.size == elements.size) {
        val itr = actual.iterator()
        var i = 0

        while (itr.hasNext()) {
            if (itr.next() != elements[i]) {
                expected("to contain exactly:${show(elements)} but was:${show(actual)}")
                break
            }
            i += 1
        }
    } else {
        expected("to contain exactly:${show(elements)} but was:${show(actual)}")
    }
}

/**
 * Asserts on all items in the collection. The given lambda will be run for each item.
 *
 * ```
 * assert(listOf("one", "two")) {
 *   hasLength(3)
 * }
 * ```
 */
fun <E, T : Collection<E>> Assert<T>.all(f: (Assert<E>) -> Unit) {
    for (item in actual) {
        f(assert(item, name))
    }
}
