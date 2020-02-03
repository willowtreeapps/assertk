package assertk.assertions

import assertk.Assert
import assertk.all
import assertk.assertions.support.expected
import assertk.assertions.support.show

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
            f(assertThat(item, name = "${name ?: ""}${show(index, "[]")}"))
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
    all(message = "expected to pass exactly $times times",
        body = { each { item -> count++; f(item) } },
        failIf = { count - it.size != times })
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
    var count = 0
    all(message = "expected any item to pass",
        body = { each { item -> count++; f(item) } },
        failIf = { count == 0 || it.size == count })
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
