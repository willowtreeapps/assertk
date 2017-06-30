package assertk.assertions

import assertk.Assert
import assertk.assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

fun <T : Collection<*>> Assert<T>.isEmpty() {
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

fun <T : Collection<*>> Assert<T>.isNotEmpty() {
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

fun <T : Collection<*>?> Assert<T>.isNullOrEmpty() {
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

fun <T : Collection<*>> Assert<T>.hasSize(size: Int) {
    assert(actual.size, "size").isEqualTo(size)
}

fun <T : Collection<*>> Assert<T>.hasSameSizeAs(other: Collection<*>) {
    val actualSize = actual.size
    val otherSize = other.size
    if (actualSize == otherSize) return
    expected("to have same size as:${show(other)} ($otherSize) but was size:($actualSize)")
}

fun <T : Collection<*>> Assert<T>.contains(element: Any?) {
    if (element in actual) return
    expected("to contain:${show(element)} but was:${show(actual)}")
}

fun <T : Collection<*>> Assert<T>.doesNotContain(element: Any?) {
    if (element !in actual) return
    expected("to not contain:${show(element)} but was:${show(actual)}")
}

fun <T : Collection<*>> Assert<T>.containsNone(vararg elements: Any?) {
    val elementsItr = elements.iterator()
    while (elementsItr.hasNext()) {
        if (actual.contains(elementsItr.next())) {
            expected("to contain none of:${show(elements)} but was:${show(actual)}")
            break
        }
    }
}

fun <T : Collection<*>> Assert<T>.containsAll(vararg elements: Any?) {
    if (!actual.containsAll(elements.toList())) {
        expected("to contain all:${show(elements)} but was:${show(actual)}")
    }
}

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

fun <E, T : Collection<E>> Assert<T>.all(f: (Assert<E>) -> Unit) {
    for (item in actual) {
        f(assert(item, name))
    }
}
