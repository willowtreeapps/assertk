package assertk.assertions

import assertk.Assert
import assertk.all
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
 * Asserts on each item in the iterable, passing if at least `times` items pass.
 * The given lambda will be run for each item.
 *
 * ```
 * assert(listOf(-1, 1, 2) as Iterable<Int>).atLeast(2) { it -> it.isPositive() }
 * ```
 */
fun <E, T : Iterable<E>> Assert<T>.atLeast(times: Int, f: (Assert<E>) -> Unit) {
    var count = 0
    all(message = "expected to pass at least $times times",
        body = { each { item -> count++; f(item) } },
        failIf = { count - it.size < times })
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
