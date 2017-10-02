package assertk.assertions

import assertk.Assert
import assertk.assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

@JvmName("arrayIsEmpty")
fun <T> Assert<Array<T>>.isEmpty() {
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

@JvmName("arrayIsNotEmpty")
fun <T> Assert<Array<T>>.isNotEmpty() {
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

@JvmName("arrayIsNullOrEmpty")
fun <T> Assert<Array<T>?>.isNullOrEmpty() {
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

@JvmName("arrayHasSize")
fun <T> Assert<Array<T>>.hasSize(size: Int) {
    assert(actual.size, "size").isEqualTo(size)
}

@JvmName("arrayContains")
fun <T> Assert<Array<T>>.contains(element: Any?) {
    if (element in actual) return
    expected("to contain:${show(element)} but was:${show(actual)}")
}

@JvmName("arrayDoesNotContain")
fun <T> Assert<Array<T>>.doesNotContain(element: Any?) {
    if (element !in actual) return
    expected("to not contain:${show(element)} but was:${show(actual)}")
}

@JvmName("arrayContainsAll")
fun <T> Assert<Array<T>>.containsAll(vararg elements: Any?) {
    if (elements.all { actual.contains(it) }) return
    expected("to contain all:${show(elements)} but was:${show(actual)}")
}

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

@JvmName("arrayAll")
fun <T> Assert<Array<T>>.all(f: (Assert<T>) -> Unit) {
    for (item in actual) {
        f(assert(item, name))
    }
}
