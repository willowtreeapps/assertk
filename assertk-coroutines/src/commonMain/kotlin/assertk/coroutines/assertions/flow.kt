package assertk.coroutines.assertions

import assertk.Assert
import assertk.assertions.*
import assertk.assertions.support.expected
import assertk.assertions.support.expectedListDiff
import assertk.assertions.support.show
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
suspend fun Assert<Flow<*>>.count(): Assert<Int> = suspendCall("count()", Flow<*>::count)

/**
 * Asserts the flow is empty. Fails as soon as the flow delivers an element.
 * @see isNotEmpty
 * @see isNullOrEmpty
 */
suspend fun Assert<Flow<*>>.isEmpty() = given { actual ->
    var count = 0
    var firstValue: Any? = null
    try {
        actual.collect {
            count++
            firstValue = it
            throw AbortFlowException()
        }
    } catch (e: AbortFlowException) {
        // Do nothing
    }
    if (count == 0) return
    expected("to be empty but received:${show(firstValue)}")
}

/**
 * Asserts the flow is not empty.
 * @see isEmpty
 */
suspend fun Assert<Flow<*>>.isNotEmpty() = given { actual ->
    var count = 0
    actual.collect {
        count++
    }
    if (count != 0) return
    expected("to not be empty")
}

@ExperimentalCoroutinesApi
suspend fun Assert<Flow<*>>.hasCount(count: Int) {
    count().isEqualTo(count)
}

/**
 * Asserts the flow contains the expected element, using `in`. Succeeds as soon as that element is received.
 * @see [doesNotContain]
 */
suspend fun Assert<Flow<*>>.contains(element: Any?) = given { actual ->
    val receivedElements = mutableListOf<Any?>()
    try {
        actual.collect {
            if (element == it) {
                throw AbortFlowException()
            }
            receivedElements.add(it)
        }
    } catch (e: AbortFlowException) {
        // Found element
        return
    }
    expected("to contain:${show(element)} but received:${show(receivedElements)}")
}

/**
 * Asserts the flow does not contain any of the expected elements. Fails as soon as that element is received.
 * @see [containsAll]
 */
suspend fun Assert<Flow<*>>.doesNotContain(element: Any?) = given { actual ->
    val receivedElements = mutableListOf<Any?>()
    try {
        actual.collect {
            receivedElements.add(it)
            if (element == it) {
                throw AbortFlowException()
            }
        }
    } catch (e: AbortFlowException) {
        // Found element
        expected("to not contain:${show(element)} but received:${show(receivedElements)}")
    }
}

/**
 * Asserts the collection does not contain any of the expected elements. Fails as soon as one of the expected elements
 * is received.
 * @see [containsAll]
 */
suspend fun Assert<Flow<*>>.containsNone(vararg elements: Any?) = given { actual ->
    val receivedElements = mutableListOf<Any?>()
    try {
        actual.collect {
            receivedElements.add(it)
            if (it in elements) {
                throw AbortFlowException()
            }
        }
    } catch (e: AbortFlowException) {
        // Found element
        expected(
            "to contain none of:${show(elements)} but received:${show(receivedElements)}\n element not expected:${show(
                receivedElements.last()
            )}"
        )
    }
}

/**
 * Asserts the flow emits all the expected elements, in any order. The flow may also
 * emit additional elements. Succeeds as soon as all expected elements are received.
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsOnly]
 */
suspend fun Assert<Flow<*>>.containsAll(vararg elements: Any?) = given { actual ->
    val remainingElements = MutableList(elements.size) { index -> elements[index] }
    val receivedElements = mutableListOf<Any?>()
    try {
        actual.collect {
            if (it in elements) {
                remainingElements.remove(it)
                if (remainingElements.isEmpty()) {
                    throw AbortFlowException()
                }
            }
            receivedElements.add(it)
        }
    } catch (e: AbortFlowException) {
        // Found all elements
        return
    }
    expected(
        "to contain all:${show(elements)} but received:${show(receivedElements)}\n elements not found:${show(
            remainingElements
        )}"
    )
}

/**
 * Asserts the flow contains only the expected elements, in any order.
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsAll]
 */
suspend fun Assert<Flow<*>>.containsOnly(vararg elements: Any?) = given { actual ->
    val actualList = actual.toList()
    val notInActual = elements.filterNot { it in actualList }
    val notInExpected = actualList.filterNot { it in elements }
    if (notInExpected.isEmpty() && notInActual.isEmpty()) {
        return
    }
    expected(StringBuilder("to contain only:${show(elements)} but received:${show(actualList)}").apply {
        if (notInActual.isNotEmpty()) {
            append("\n elements not found:${show(notInActual)}")
        }
        if (notInExpected.isNotEmpty()) {
            append("\n extra elements found:${show(notInExpected)}")
        }
    }.toString())
}

/**
 * Asserts the flow contains exactly the expected elements. They must be in the same order and
 * there must not be any extra elements.
 * @see [containsAll]
 */
suspend fun Assert<Flow<*>>.containsExactly(vararg elements: Any?) = given { actual ->
    val expected = elements.toList()
    val asList = actual.toList()
    if (asList == expected) return
    expectedListDiff(expected, asList)
}

private class AbortFlowException :
    CancellationException("Flow was aborted, no more elements needed")