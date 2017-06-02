package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show


fun <T : Map<*, *>> Assert<T>.hasSameSizeAs(other: Map<*, *>) {
    val actualSize = actual.size
    val otherSize = other.size
    if (actualSize == otherSize) return
    expected("to have same size as:${show(other)} ($otherSize) but was size:($actualSize)")
}

@JvmName("mapContains")
fun <K, V> Assert<Map<K, V>>.contains(key : K, value : V) {
    if (actual[key] == value) {
        return
    }
    expected("to contain:${show(mapOf(key to value))} but was:${show(actual)}")
}

@JvmName("mapContainsPair")
fun <K, V> Assert<Map<K, V>>.contains(element: Pair<K, V>) {
    contains(element.first, element.second)
}

@JvmName("mapContainsAll")
fun <K, V> Assert<Map<K, V>>.containsAll(vararg elements: Pair<K, V>) {
    if (elements.all { (k, v) -> actual[k] == v }) return
    expected("to contain:${show(mapOf(*elements))} but was:${show(actual)}")
}

@JvmName("mapDoesNotContain")
fun <K, V> Assert<Map<K, V>>.doesNotContain(element: Pair<K, V>) {
    if (actual[element.first] != element.second) {
        return
    }
    expected("to not contain:${show(mapOf(element))} but was:${show(actual)}")
}

@JvmName("mapContainsNone")
fun <K, V> Assert<Map<K, V>>.containsNone(vararg elements: Pair<K, V>) {
    if (elements.all { (k, v) -> actual[k] != v }) return
    expected("to not contain:${show(mapOf(*elements))} but was:${show(actual)}")
}

@JvmName("mapContainsExactly")
fun <K, V> Assert<Map<K, V>>.containsExactly(vararg elements: Pair<K, V>) {
    if (actual.size == elements.size && elements.all { (k, v) -> actual[k] == v }) return
    expected("to contain exactly:${show(mapOf(*elements))} but was:${show(actual)}")
}
