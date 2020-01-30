package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Returns an assert on the Maps's size.
 */
fun Assert<Map<*, *>>.size() = prop("size", Map<*, *>::size)

/**
 * Asserts the collection is empty.
 * @see [isNotEmpty]
 * @see [isNullOrEmpty]
 */
fun Assert<Map<*, *>>.isEmpty() = given { actual ->
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the collection is not empty.
 * @see [isEmpty]
 */
fun Assert<Map<*, *>>.isNotEmpty() = given { actual ->
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

/**
 * Asserts the collection is null or empty.
 * @see [isEmpty]
 */
fun Assert<Map<*, *>?>.isNullOrEmpty() = given { actual ->
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

/**
 * Asserts the collection has the expected size.
 */
fun Assert<Map<*, *>>.hasSize(size: Int) {
    size().isEqualTo(size)
}

/**
 * Asserts the map has the same size as the expected map.
 */
fun Assert<Map<*, *>>.hasSameSizeAs(other: Map<*, *>) = given { actual ->
    val actualSize = actual.size
    val otherSize = other.size
    if (actualSize == otherSize) return
    expected("to have same size as:${show(other)} ($otherSize) but was size:($actualSize)")
}

/**
 * Asserts the map contains the expected key-value pair.
 * @see [doesNotContain]
 */
fun <K, V> Assert<Map<K, V>>.contains(key: K, value: V) = given { actual ->
    if (actual[key] == value) {
        return
    }
    expected("to contain:${show(mapOf(key to value))} but was:${show(actual)}")
}

/**
 * Asserts the map contains the expected key-value pair.
 * @see [doesNotContain]
 */
fun <K, V> Assert<Map<K, V>>.contains(element: Pair<K, V>) {
    contains(element.first, element.second)
}

/**
 * Asserts the map contains all the expected elements. The map may also contain additional elements.
 * @see [containsNone]
 * @see [containsExactly]
 */
fun <K, V> Assert<Map<K, V>>.containsAll(vararg elements: Pair<K, V>) = given { actual ->
    if (elements.all { (k, v) -> actual[k] == v }) return
    val notFound = elements.filterNot { (k, v) -> actual[k] == v }
    expected("to contain all:${show(elements.toMap())} but was:${show(actual)}. Missing elements: ${show(notFound.toMap())}")
}

/**
 * Asserts the map does not contain the expected key-value pair.
 * @see [contains]
 */
fun <K, V> Assert<Map<K, V>>.doesNotContain(key: K, value: V) = given { actual ->
    if (actual[key] != value) {
        return
    }
    expected("to not contain:${show(mapOf(key to value))} but was:${show(actual)}")
}

/**
 * Asserts the map does not contain the expected key-value pair.
 * @see [contains]
 */
fun <K, V> Assert<Map<K, V>>.doesNotContain(element: Pair<K, V>) {
    doesNotContain(element.first, element.second)
}

/**
 * Asserts the map does not contain any of the expected elements.
 * @see [containsAll]
 */
fun <K, V> Assert<Map<K, V>>.containsNone(vararg elements: Pair<K, V>) = given { actual ->
    if (elements.all { (k, v) -> actual[k] != v }) return
    val notExpected = elements.filter { (k, v) -> actual[k] == v }
    expected("to contain none of:${show(elements.toMap())} some elements were not expected:${show(notExpected.toMap())}")
}

/**
 * Asserts the map contains only the expected elements. There must not be any extra elements.
 * @see [containsAll]
 */
fun <K, V> Assert<Map<K, V>>.containsOnly(vararg elements: Pair<K, V>) = given { actual ->
    if (actual.size == elements.size && elements.all { (k, v) -> actual[k] == v }) return
    expected("to contain only:${show(mapOf(*elements))} but was:${show(actual)}")
}

/**
 * Returns an assert that asserts on the value at the given key in the map.
 *
 * ```
 * assertThat(mapOf("key" to "value")).key("key").isEqualTo("value")
 * ```
 */
fun <K, V> Assert<Map<K, V>>.key(key: K): Assert<V> = transform("${name ?: ""}${show(key, "[]")}") { actual ->
    if (key in actual) {
        actual.getValue(key)
    } else {
        expected("to have key:${show(key)}")
    }
}
