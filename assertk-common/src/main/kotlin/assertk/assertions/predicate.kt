package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Asserts if the values satisfies the predicate provided.
 *
 * ```
 * val divisibleBy5 : (Int) -> Boolean = { it % 5 == 0 }
 * assert(10).assertPredicate(divisibleBy5)
 * ```
 */

fun <T> Assert<T>.assertPredicate(f: (T) -> Boolean) {
    if (f(actual)) return
    expected("$actual to satisfy the predicate")
}

/**
 * Asserts on each item in the iterable. The given lambda will be run for each item.
 *
 * ```
 * fun divisibleBy5(value: Double) : Boolean {
 * return value % 5 == 0.0
 * }
 * assert(listOf("one", "two")).areAtLeast(2) { divisibleBy5(it) }
 * ```
 */

fun <E, T : Iterable<E>> Assert<T>.areAtLeast(times: Int, f: ((E) -> Boolean)) {
    if (actual.filter { f(it) }.size >= times) return
    expected("atleast $times occurences in ${show(actual)}")
}
