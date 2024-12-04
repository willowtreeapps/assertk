package assertk.assertions

import assertk.Assert
import assertk.assertions.support.appendName
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Returns an assert on the Maps's size.
 */
fun Assert<Map<*, *>>.size() = having("size", Map<*, *>::size)

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
    if (actual.containsKey(key) && actual[key] == value) {
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
 * Asserts the map contains at least the expected elements. The map may also contain additional elements.
 * @see [containsNone]
 * @see [containsExactly]
 */
@Deprecated("renamed to containsAtLeast", ReplaceWith("containsAtLeast(*elements)"))
fun <K, V> Assert<Map<K, V>>.containsAll(vararg elements: Pair<K, V>) = containsAtLeast(*elements)

/**
 * Asserts the map contains at least the expected elements. The map may also contain additional elements.
 * @see [containsNone]
 * @see [containsExactly]
 */
fun <K, V> Assert<Map<K, V>>.containsAtLeast(vararg elements: Pair<K, V>) = given { actual ->
    if (elements.all { (k, v) -> actual.containsKey(k) && actual[k] == v }) {
        return
    }

    val notFound = elements.filterNot { (k, v) -> actual.containsKey(k) && actual[k] == v }
    expected("to contain all:${show(elements.toMap())} but was:${show(actual)}\n elements not found:${show(notFound.toMap())}")
}

/**
 * Asserts the map does not contain the expected key-value pair.
 * @see [contains]
 */
fun <K, V> Assert<Map<K, V>>.doesNotContain(key: K, value: V) = given { actual ->
    if (!actual.containsKey(key) || actual[key] != value) {
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
 * Asserts the map does not contain the expected key.
 */
fun <K, V> Assert<Map<K, V>>.doesNotContainKey(key: K) = given {
    if (!it.containsKey(key)) return
    expected("to not contain key:${show(key)} but had value: ${show(it[key])}")
}

/**
 * Asserts the map does not contain any of the expected elements.
 * @see [containsAtLeast]
 */
fun <K, V> Assert<Map<K, V>>.containsNone(vararg elements: Pair<K, V>) = given { actual ->
    if (elements.all { (k, v) -> !actual.containsKey(k) || actual[k] != v }) return
    val notExpected = elements.filter { (k, v) -> actual[k] == v }
    expected(
        "to contain none of:${show(elements.toMap())} but was:${show(actual)}\n elements not expected:${show(
            notExpected.toMap()
        )}"
    )
}

/**
 * Asserts the map contains only the expected elements. There must not be any extra elements.
 * @see [containsAtLeast]
 */
fun <K, V> Assert<Map<K, V>>.containsOnly(vararg elements: Pair<K, V>) = given { actual ->
    val elementMap = elements.toMap()
    val notInActual = elementMap.filterNot { (key, value) -> actual.containsKey(key) && actual[key] == value }
    val notInExpected =
        actual.filterNot { (key, value) -> elementMap.containsKey(key) && elementMap[key] == value}
    if (notInActual.isEmpty() && notInExpected.isEmpty()) {
        return
    }
    expected(StringBuilder("to contain only:${show(elementMap)} but was:${show(actual)}").apply {
        if (notInActual.isNotEmpty()) {
            append("\n elements not found:${show(notInActual)}")
        }
        if (notInExpected.isNotEmpty()) {
            append("\n extra elements found:${show(notInExpected)}")
        }
    }.toString())
}

/**
 * Returns an assert that asserts on the value at the given key in the map.
 *
 * ```
 * assertThat(mapOf("key" to "value")).key("key").isEqualTo("value")
 * ```
 */
fun <K, V> Assert<Map<K, V>>.key(key: K): Assert<V> = transform(appendName(show(key, "[]"))) { actual ->
    if (key in actual) {
        actual.getValue(key)
    } else {
        expected("to have key:${show(key)}")
    }
}

/**
 * Returns an assert that has a collection of the keys in the map.
 * ```
 * assertThat(mapOf("key" to "value")).havingKeys().containsOnly("key")
 * ```
 * @see havingValues
 */
fun <K, V> Assert<Map<K, V>>.havingKeys(): Assert<Set<K>> = transform {
    if (it.isEmpty()) {
        expected("map to not be empty")
    } else {
        it.keys
    }
}

/**
 * Returns an assert that has a collection of the values in the map.
 * ```
 * assertThat(mapOf("key" to "value")).havingValues().containsOnly("value")
 * ```
 * @see havingKeys
 */
fun <K, V> Assert<Map<K, V>>.havingValues(): Assert<Collection<V>> = transform {
    if (it.isEmpty()) {
        expected("map to not be empty")
    } else {
        it.values
    }
}