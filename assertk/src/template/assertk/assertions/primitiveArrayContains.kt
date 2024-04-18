package assertk.assertions

import kotlin.ExperimentalUnsignedTypes
import kotlin.jvm.JvmName
import assertk.Assert
import assertk.all
import assertk.assertions.support.expected
import assertk.assertions.support.expectedListDiff
import assertk.assertions.support.show
import assertk.assertions.support.fail
import assertk.assertions.support.appendName

$T:$N:$E:$A =
    ByteArray:byteArray:Byte:,
    IntArray:intArray:Int:,
    ShortArray:shortArray:Short:,
    LongArray:longArray:Long:,
    CharArray:charArray:Char:,
    UByteArray:ubyteArray:UByte:@ExperimentalUnsignedTypes,
    UShortArray:ushortArray:UShort:@ExperimentalUnsignedTypes,
    UIntArray:uintArray:UInt:@ExperimentalUnsignedTypes,
    ULongArray:ulongArray:ULong:@ExperimentalUnsignedTypes

/**
 * Asserts the $T contains the expected element, using `in`.
 * @see [doesNotContain]
 */
@JvmName("$NContains")
$A
fun Assert<$T>.contains(element: $E) = given { actual ->
    if (element in actual) return
    expected("to contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the $T does not contain the expected element, using `!in`.
 * @see [contains]
 */
@JvmName("$NDoesNotContain")
$A
fun Assert<$T>.doesNotContain(element: $E) = given { actual ->
    if (element !in actual) return
    expected("to not contain:${show(element)} but was:${show(actual)}")
}

/**
 * Asserts the $T does not contain any of the expected elements.
 * @see [containsAtLeast]
 */
$A
fun Assert<$T>.containsNone(vararg elements: $E) = given { actual ->
    if (elements.none { it in actual }) {
        return
    }

    val notExpected = elements.filter { it in actual }
    expected("to contain none of:${show(elements)} but was:${show(actual)}\n elements not expected:${show(notExpected)}")
}

/**
 * Asserts the $T contains at least the expected elements, in any order. The array may also contain
 * additional elements.
 * @see [containsExactly]
 */
@JvmName("$NcontainsAll")
@Deprecated("renamed to containsAtLeast", ReplaceWith("containsAtLeast(*elements)"))
$A
fun Assert<$T>.containsAll(vararg elements: $E) = containsAtLeast(*elements)

/**
 * Asserts the $T contains at least the expected elements, in any order. The array may also contain
 * additional elements.
 * @see [containsExactly]
 */
@JvmName("$NcontainsAtLeast")
$A
fun Assert<$T>.containsAtLeast(vararg elements: $E) = given { actual ->
    if (elements.all { actual.contains(it) }) return
    val notFound = elements.filterNot { it in actual }
    expected("to contain all:${show(elements)} but was:${show(actual)}\n elements not found:${show(notFound)}")
}

/**
 * Asserts the $T contains only the expected elements, in any order. Duplicate values
 * in the expected and actual are ignored.
 *
 * [1, 2] containsOnly [2, 1] passes
 * [1, 2, 2] containsOnly [2, 1] passes
 * [1, 2] containsOnly [2, 2, 1] passes
 *
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsAtLeast]
 */
$A
fun Assert<$T>.containsOnly(vararg elements: $E) = given { actual ->
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
 * Asserts the $T contains exactly the expected elements. They must be in the same order and
 * there must not be any extra elements.
 *
 * [1, 2] containsOnly [2, 1] fails
 * [1, 2, 2] containsOnly [2, 1] fails
 * [1, 2] containsOnly [2, 2, 1] fails
 *
 * @see [containsAtLeast]
 */
@JvmName("$NContainsExactly")
$A
fun Assert<$T>.containsExactly(vararg elements: $E) = given { actual ->
    if (actual.contentEquals(elements)) return

    expectedListDiff(elements.toList(), actual.toList())
}

/**
 * Asserts the $T contains exactly the expected elements, in any order. Each value in expected
 * must correspond to a matching value in actual, and visa-versa.
 *
 * [1, 2] containsExactlyInAnyOrder [2, 1] passes
 * [1, 2, 2] containsExactlyInAnyOrder [2, 1] fails
 * [1, 2] containsExactlyInAnyOrder [2, 2, 1] fails
 *
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsAtLeast]
 * @see [containsOnly]
 */
@JvmName("$NContainsExactlyInAnyOrder")
$A
fun Assert<$T>.containsExactlyInAnyOrder(vararg elements: $E) = given { actual ->
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