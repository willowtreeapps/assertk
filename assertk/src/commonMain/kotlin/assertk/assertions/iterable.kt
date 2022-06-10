package assertk.assertions

import assertk.Assert
import assertk.SoftFailure
import assertk.all
import assertk.assertions.support.appendName
import assertk.assertions.support.expected
import assertk.assertions.support.show
import assertk.run

/**
 * Asserts the iterable contains the expected element, using `in`.
 * @see [doesNotContain]
 */
fun Assert<Iterable<*>>.contains(element: Any?) = given { actual ->
    if (element in actual) return
    expected("to contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the iterable does not contain the expected element, using `!in`.
 * @see [contains]
 */
fun Assert<Iterable<*>>.doesNotContain(element: Any?) = given { actual ->
    if (element !in actual) return
    expected("to not contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the iterable does not contain any of the expected elements.
 * @see [containsAll]
 */
fun Assert<Iterable<*>>.containsNone(vararg elements: Any?) = given { actual ->
    val notExpected = elements.filter { it in actual }
    if (notExpected.isEmpty()) {
        return
    }
    expected("to contain none of:${show(elements)} but was:${show(actual)}\n elements not expected:${show(notExpected)}")
}

/**
 * Asserts the iterable contains all the expected elements, in any order. The collection may also
 * contain additional elements.
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsOnly]
 */
fun Assert<Iterable<*>>.containsAll(vararg elements: Any?) = given { actual ->
    val notFound = elements.filterNot { it in actual }
    if (notFound.isEmpty()) {
        return
    }
    expected("to contain all:${show(elements)} but was:${show(actual)}\n elements not found:${show(notFound)}")
}

/**
 * Asserts the iterable contains only the expected elements, in any order. Duplicate values
 * in the expected and actual are ignored.
 *
 * [1, 2] containsOnly [2, 1] passes
 * [1, 2, 2] containsOnly [2, 1] passes
 * [1, 2] containsOnly [2, 2, 1] passes
 *
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsAll]
 * @see [containsExactlyInAnyOrder]
 */
fun Assert<Iterable<*>>.containsOnly(vararg elements: Any?) = given { actual ->
    val notInActual = elements.filterNot { it in actual }
    val notInExpected = actual.filterNot { it in elements }
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

/**
 * Asserts the iterable contains exactly the expected elements, in any order. Each value in expected
 * must correspond to a matching value in actual, and visa-versa.
 *
 * [1, 2] containsExactlyInAnyOrder [2, 1] passes
 * [1, 2, 2] containsExactlyInAnyOrder [2, 1] fails
 * [1, 2] containsExactlyInAnyOrder [2, 2, 1] fails
 *
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsAll]
 * @see [containsOnly]
 */
fun Assert<Iterable<*>>.containsExactlyInAnyOrder(vararg elements: Any?) = given { actual ->
    val notInActual = elements.toMutableList()
    val notInExpected = actual.toMutableList()
    elements.forEach {
        if (notInExpected.contains(it)) {
            notInExpected.removeFirst(it)
            notInActual.removeFirst(it)
        }
    }
    if (notInExpected.isEmpty() && notInActual.isEmpty()) {
        return
    }
    expected(StringBuilder("to contain exactly in any order:${show(elements)} but was:${show(actual)}").apply {
        if (notInActual.isNotEmpty()) {
            append("\n elements not found:${show(notInActual)}")
        }
        if (notInExpected.isNotEmpty()) {
            append("\n extra elements found:${show(notInExpected)}")
        }
    }.toString())
}

internal fun MutableList<*>.removeFirst(value: Any?) {
    val index = indexOf(value)
    if (index > -1) removeAt(index)
}

/**
 * Asserts on each item in the iterable. The given lambda will be run for each item.
 *
 * ```
 * assertThat(listOf("one", "two")).each {
 *   it.hasLength(3)
 * }
 * ```
 */
fun <E> Assert<Iterable<E>>.each(f: (Assert<E>) -> Unit) = given { actual ->
    all {
        actual.forEachIndexed { index, item ->
            f(assertThat(item, name = appendName(show(index, "[]"))))
        }
    }
}

/**
 * Extracts a value of from each item in the iterable, allowing you to assert on a list of those values.
 *
 * ```
 * assertThat(people)
 *   .extracting(Person::name)
 *   .contains("Sue", "Bob")
 * ```
 */
fun <E, R> Assert<Iterable<E>>.extracting(f1: (E) -> R): Assert<List<R>> = transform { actual ->
    actual.map(f1)
}

/**
 * Extracts two values of from each item in the iterable, allowing you to assert on a list of paris of those values.
 *
 * ```
 * assertThat(people)
 *   .extracting(Person::name, Person::age)
 *   .contains("Sue" to 20, "Bob" to 22)
 * ```
 */
fun <E, R1, R2> Assert<Iterable<E>>.extracting(f1: (E) -> R1, f2: (E) -> R2): Assert<List<Pair<R1, R2>>> =
    transform { actual ->
        actual.map { f1(it) to f2(it) }
    }

/**
 * Extracts three values from each item in the iterable, allowing you to assert on a list of triples of those values.
 *
 * ```
 * assertThat(people)
 *   .extracting(Person::name, Person::age, Person::address)
 *   .contains(Triple("Sue", 20, "123 Street"), Triple("Bob", 22, "456 Street")
 * ```
 */
fun <E, R1, R2, R3> Assert<Iterable<E>>.extracting(
    f1: (E) -> R1,
    f2: (E) -> R2,
    f3: (E) -> R3
): Assert<List<Triple<R1, R2, R3>>> = transform { actual ->
    actual.map { Triple(f1(it), f2(it), f3(it)) }
}

/**
 * Asserts on each item in the iterable, passing if none of the items pass.
 * The given lambda will be run for each item.
 *
 * ```
 * assertThat(listOf("one", "two")).none {
 *   it.hasLength(2)
 * }
 * ```
 */
fun <E> Assert<Iterable<E>>.none(f: (Assert<E>) -> Unit) = given { actual ->
    if (actual.count() > 0) {
        all(message = "expected none to pass",
            body = { each { item -> f(item) } },
            failIf = { it.isEmpty() })
    }
}

/**
 * Asserts on each item in the iterable, passing if at least `times` items pass.
 * The given lambda will be run for each item.
 *
 * ```
 * assert(listOf(-1, 1, 2)).atLeast(2) { it.isPositive() }
 * ```
 */
fun <E, T : Iterable<E>> Assert<T>.atLeast(times: Int, f: (Assert<E>) -> Unit) {
    var count = 0
    all(message = "expected to pass at least $times times",
        body = { each { item -> count++; f(item) } },
        failIf = { count - it.size < times })
}

/**
 * Asserts on each item in the iterable, passing if at most `times` items pass.
 * The given lambda will be run for each item.
 *
 * ```
 * assert(listOf(-2, -1, 1)).atMost(2) { it.isPositive() }
 * ```
 */
fun <E, T : Iterable<E>> Assert<T>.atMost(times: Int, f: (Assert<E>) -> Unit) {
    var count = 0
    all(message = "expected to pass at most $times times",
        body = { each { item -> count++; f(item) } },
        failIf = { count - it.size > times })
}

/**
 * Asserts on each item in the iterable, passing if exactly `times` items pass.
 * The given lambda will be run for each item.
 *
 * ```
 * assert(listOf(-1, 1, 2)).exactly(2) { it.isPositive() }
 * ```
 */
fun <E, T : Iterable<E>> Assert<T>.exactly(times: Int, f: (Assert<E>) -> Unit) {
    var count = 0
    all(
        message = "expected to pass exactly $times times",
        body = {
            each { item ->
                count++
                SoftFailure(groupFailures = true).run { f(item) }
            }
        },
        failIf = {count - it.size != times }
    )
}

/**
 * Asserts on each item in the iterable, passing if any of the items pass.
 * The given lambda will be run for each item.
 *
 * ```
 * assert(listOf(-1, -2, 1)).any { it.isPositive() }
 * ```
 */
fun <E, T : Iterable<E>> Assert<T>.any(f: (Assert<E>) -> Unit) {
    var lastFailureCount = 0
    var itemPassed = false
    all(message = "expected any item to pass",
        body = { failure ->
            given { actual ->
                actual.forEachIndexed { index, item ->
                    f(assertThat(item, name = appendName(show(index, "[]"))))
                    if (lastFailureCount == failure.count) {
                        itemPassed = true
                    }
                    lastFailureCount = failure.count
                }
            }
        },
        failIf = {
            !itemPassed
        })
}

/**
 * Asserts the iterable is empty.
 * @see [isNotEmpty]
 * @see [isNullOrEmpty]
 */
fun Assert<Iterable<*>>.isEmpty() = given { actual ->
    if (actual.none()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the iterable is not empty.
 * @see [isEmpty]
 */
fun Assert<Iterable<*>>.isNotEmpty() = given { actual ->
    if (actual.any()) return
    expected("to not be empty")
}

/**
 * Asserts the iterable is not empty, and returns an assert on the first element.
 */
fun <E, T : Iterable<E>> Assert<T>.first(): Assert<E> {
    return transform(appendName("first", ".")) { iterable ->
        val iterator = iterable.iterator()
        if (iterator.hasNext()) {
            iterator.next()
        } else {
            expected("to not be empty")
        }
    }
}

/**
 * Asserts the iterable contains exactly one element, and returns an assert on that element.
 */
fun <E, T : Iterable<E>> Assert<T>.single(): Assert<E> {
    return transform(appendName("single", ".")) { iterable ->
        val iterator = iterable.iterator()
        if (iterator.hasNext()) {
            val single = iterator.next()
            if (iterator.hasNext()) {
                val size = if (iterable is Collection<*>) iterable.size.toString() else "multiple"
                expected("to have single element but has $size: ${show(iterable)}")
            } else {
                single
            }
        } else {
            expected("to have single element but was empty")
        }
    }
}
