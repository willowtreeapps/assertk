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
 * val divisibleBy5 : (Int) -> Boolean = { it % 5 == 0 }
 * assert(listOf(5, 2, 10) as Iterable<Int>).atLeast(2) { it -> it.assertPredicate(divisibleBy5) }
 * ```
 */

fun <E, T : Iterable<E>> Assert<T>.atLeast(times: Int, f: (Assert<E>) -> Unit) {
    var count = 0
    var nonMatchedIndex = ""
    actual.forEachIndexed { index, item ->
        try {
            f(assert(item, "${name ?: ""}${show(index, "[]")}"))
            count++
        } catch (e: Throwable) {
            nonMatchedIndex += "${show(index, "[]")},"
        }
    }
    if (count >= times) return
    expected("atleast $times occurences in ${show(actual)} but ${nonMatchedIndex.substring(0, nonMatchedIndex.lastIndex)} did not match")
}
