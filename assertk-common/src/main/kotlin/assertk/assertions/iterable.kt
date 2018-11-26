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
 * assert(listOf("one", "two")).each {
 *   it.hasLength(3)
 * }
 * ```
 */
fun <E> Assert<Iterable<E>>.each(f: (Assert<E>) -> Unit) = given { actual ->
    all {
        actual.forEachIndexed { index, item ->
            f(assert(item, name = "${name ?: ""}${show(index, "[]")}"))
        }
    }
}
