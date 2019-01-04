package assertk.assertions

import assertk.*
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show

/**
 * Returns an assert on the Arrays's size.
 */
fun Assert<Array<*>>.size() = prop("size") { it.size }

/**
 * Asserts the array contents are equal to the expected one, using [contentDeepEquals].
 * @see isNotEqualTo
 */
fun <T> Assert<Array<T>>.isEqualTo(expected: Array<T>) = given { actual ->
    if (actual.contentDeepEquals(expected)) return
    fail(expected, actual)
}

/**
 * Asserts the array contents are not equal to the expected one, using [contentDeepEquals].
 * @see isEqualTo
 */
fun <T> Assert<Array<T>>.isNotEqualTo(expected: Array<T>) = given { actual ->
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
fun Assert<Array<*>>.isEmpty() = given { actual ->
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the array is not empty.
 * @see [isEmpty]
 */
@PlatformName("arrayIsNotEmpty")
fun Assert<Array<*>>.isNotEmpty() = given { actual ->
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

/**
 * Asserts the array is null or empty.
 * @see [isEmpty]
 */
@PlatformName("arrayIsNullOrEmpty")
fun Assert<Array<*>?>.isNullOrEmpty() = given { actual ->
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

/**
 * Asserts the array has the expected size.
 */
@PlatformName("arrayHasSize")
fun Assert<Array<*>>.hasSize(size: Int) {
    size().isEqualTo(size)
}

/**
 * Asserts the array has the same size as the expected array.
 */
@PlatformName("arrayHasSameSizeAs")
fun <T> Assert<Array<T>>.hasSameSizeAs(other: Array<*>) = given { actual ->
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
fun <T> Assert<Array<T>>.contains(element: Any?) = given { actual ->
    if (element in actual) return
    expected("to contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the array does not contain the expected element, using `!in`.
 * @see [contains]
 */
@PlatformName("arrayDoesNotContain")
fun <T> Assert<Array<T>>.doesNotContain(element: Any?) = given { actual ->
    if (element !in actual) return
    expected("to not contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the collection does not contain any of the expected elements.
 * @see [containsAll]
 */
fun <T> Assert<Array<T>>.containsNone(vararg elements: Any?) = given { actual ->
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
fun <T> Assert<Array<T>>.containsAll(vararg elements: Any?) = given { actual ->
    if (elements.all { actual.contains(it) }) return
    val notFound = elements.filterNot { it in actual }
    expected("to contain all:${show(elements)} but was:${show(actual)}. Missing elements:${show(notFound)}")
}

/**
 * Returns an assert that assertion on the value at the given index in the array.
 *
 * ```
 * assert(arrayOf(0, 1, 2)).index(1) { it.isPositive() }
 * ```
 */
@Deprecated(message = "Use index(index) instead.", replaceWith = ReplaceWith("index(index).let(f)"))
fun <T> Assert<Array<T>>.index(index: Int, f: (Assert<T>) -> Unit) {
    index(index).let(f)
}

/**
 * Returns an assert that assertion on the value at the given index in the array.
 *
 * ```
 * assert(arrayOf(0, 1, 2)).index(1).isPositive()
 * ```
 */
fun <T> Assert<Array<T>>.index(index: Int): Assert<T> =
    transform("${name ?: ""}${show(index, "[]")}") { actual ->
        if (index in 0 until actual.size) {
            actual[index]
        } else {
            expected("index to be in range:[0-${actual.size}) but was:${show(index)}")
        }
    }

/**
 * Asserts the array contains exactly the expected elements. They must be in the same order and
 * there must not be any extra elements.
 * @see [containsAll]
 */
@PlatformName("arrayContainsExactly")
fun <T> Assert<Array<T>>.containsExactly(vararg elements: Any?) = given { actual ->
    if (actual.contentEquals(elements)) return

    expected(listDifferExpected(elements.asList(), actual.asList()))
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
fun <T> Assert<Array<T>>.each(f: (Assert<T>) -> Unit) = given { actual ->
    all {
        actual.forEachIndexed { index, item ->
            f(assert(item, name = "${name ?: ""}${show(index, "[]")}"))
        }
    }
}
