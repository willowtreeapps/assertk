package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

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

fun <E, T : Iterable<E>> Assert<T>.areAtLeast(times: Int, f: ((E) -> Any)? = null) {
    var count = 0
    if (f == null) expected("condition should be not null")
    actual.forEach { item ->
        if (f?.let { it(item!!) } == true) count++
    }
    if (count >= times) return
    expected("atleast $times occurences in ${show(actual)}")
}
