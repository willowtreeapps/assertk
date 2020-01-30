package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Returns an assert on the Collection's size.
 */
fun Assert<Collection<*>>.size() = prop("size", Collection<*>::size)

/**
 * Asserts the collection is empty.
 * @see [isNotEmpty]
 * @see [isNullOrEmpty]
 */
fun Assert<Collection<*>>.isEmpty() = given { actual ->
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the collection is not empty.
 * @see [isEmpty]
 */
fun Assert<Collection<*>>.isNotEmpty() = given { actual ->
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

/**
 * Asserts the collection is null or empty.
 * @see [isEmpty]
 */
fun Assert<Collection<*>?>.isNullOrEmpty() = given { actual ->
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

/**
 * Asserts the collection has the expected size.
 */
fun Assert<Collection<*>>.hasSize(size: Int) {
    size().isEqualTo(size)
}

/**
 * Asserts the collection has the same size as the expected collection.
 */
fun Assert<Collection<*>>.hasSameSizeAs(other: Collection<*>) = given { actual ->
    val actualSize = actual.size
    val otherSize = other.size
    if (actualSize == otherSize) return
    expected("to have same size as:${show(other)} ($otherSize) but was size:($actualSize)")
}

/**
 * Asserts the collection does not contain any of the expected elements.
 * @see [containsAll]
 */
fun Assert<Collection<*>>.containsNone(vararg elements: Any?) = given { actual ->
    if (elements.none { it in actual }) {
        return
    }

    val notExpected = elements.filter { it in actual }
    expected("to contain none of:${show(elements)} but was:${show(actual)}\n elements not expected:${show(notExpected)}")
}

/**
 * Asserts the collection contains all the expected elements, in any order. The collection may also
 * contain additional elements.
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsOnly]
 */
fun Assert<Collection<*>>.containsAll(vararg elements: Any?) = given { actual ->
    if (actual.containsAll(elements.toList())) {
        return
    }

    val notFound = elements.filterNot { it in actual }
    expected("to contain all:${show(elements)} but was:${show(actual)}\n elements not found:${show(notFound)}")
}

/**
 * Asserts the collection contains only the expected elements
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsAll]
 */
fun Assert<Collection<*>>.containsOnly(vararg elements: Any?) = given { actual ->
    val notInActual = elements.filterNot { it in actual }
    val notInExpected = actual.filterNot { elements.contains(it) }
    if (notInExpected.isEmpty() && notInActual.isEmpty()) {
        return
    }
    expected(StringBuilder("to contain only:${show(elements)} but was:${show(actual)}").apply {
        if (notInActual.isNotEmpty()) {
            append("\n elements not found:${show(notInActual)}")
        }
        if (notInExpected.isNotEmpty()) {
            append("\n extra elements found:${show(notInExpected)}")
        }
    }.toString())
}
