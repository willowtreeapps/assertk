package assertk.assertions

import assertk.Assert
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
    val notExpected = actual.toMutableList()
    val notFound = mutableListOf<Any?>()

    for (element in elements) {
        if (element in notExpected) {
            notExpected.removeAt(notExpected.indexOfFirst { it == element })
        } else {
            notFound += element
        }
    }

    if (notExpected.isEmpty() && notFound.isEmpty()) {
        return
    }

    if (notExpected.isEmpty()) {
        expected("to contain exactly:${show(elements)} but was:${show(actual)}" +
                " some elements were not found:${show(notFound)}")
    } else if (notFound.isEmpty()) {
        expected("to contain exactly:${show(elements)} but was:${show(actual)}" +
                " some elements were not expected:${show(notExpected)}")
    } else {
        expected("to contain exactly:${show(elements)} but was:${show(actual)}" +
                " some elements were not found:${show(notFound)}" +
                " some elements were not expected:${show(notExpected)}")
    }
}
