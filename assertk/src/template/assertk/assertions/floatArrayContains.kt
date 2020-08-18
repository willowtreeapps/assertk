package assertk.assertions

import kotlin.jvm.JvmName
import assertk.Assert
import assertk.all
import assertk.assertions.support.expected
import assertk.assertions.support.show
import assertk.assertions.support.fail
import assertk.assertions.support.appendName

$T:$N:$E = FloatArray:floatArray:Float, DoubleArray:doubleArray:Double

/**
 * Asserts the $T contains the expected element, using `in`.
 * @see [doesNotContain]
 */
@JvmName("$NContains")
fun Assert<$T>.contains(element: $E) = given { actual ->
    if (element in actual.asList()) return
    expected("to contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the $T does not contain the expected element, using `!in`.
 * @see [contains]
 */
@JvmName("$NDoesNotContain")
fun Assert<$T>.doesNotContain(element: $E) = given { actual ->
    if (element !in actual.asList()) return
    expected("to not contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the $T does not contain any of the expected elements.
 * @see [containsAll]
 */
fun Assert<$T>.containsNone(vararg elements: $E) = given { actual ->
    val actualList = actual.asList()
    if (elements.none { it in actualList }) {
        return
    }

    val notExpected = elements.filter { it in actualList }
    expected("to contain none of:${show(elements)} but was:${show(actual)}\n elements not expected:${show(notExpected)}")
}

/**
 * Asserts the $T contains all the expected elements, in any order. The array may also contain
 * additional elements.
 * @see [containsExactly]
 */
@JvmName("$NContainsAll")
fun Assert<$T>.containsAll(vararg elements: $E) = given { actual ->
    val actualList =  actual.asList()
    val elementsList = elements.asList()
    if (elementsList.all { actualList.contains(it) }) return
    val notFound = elementsList.filterNot { it in actualList }
    expected("to contain all:${show(elements)} but was:${show(actual)}\n elements not found:${show(notFound)}")
}

/**
 * Asserts the $T contains only the expected elements, in any order.
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsAll]
 */
fun Assert<$T>.containsOnly(vararg elements: $E) = given { actual ->
    val actualList = actual.asList()
    val elementsList = elements.asList()
    val notInActual = elementsList.filterNot { it in actualList }
    val notInExpected = actualList.filterNot { it in elementsList }
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
 * Asserts the $T contains exactly the expected elements. They must be in the same order and
 * there must not be any extra elements.
 * @see [containsAll]
 */
@JvmName("$NContainsExactly")
fun Assert<$T>.containsExactly(vararg elements: $E) = given { actual ->
    val actualList = actual.asList()
    val elementsList = elements.asList()
    if (actualList == elementsList) return

    expected(listDifferExpected(elementsList, actualList))
}

