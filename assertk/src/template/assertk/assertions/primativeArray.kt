package assertk.assertions

import kotlin.jvm.JvmName
import assertk.Assert
import assertk.all
import assertk.assertions.support.expected
import assertk.assertions.support.show
import assertk.assertions.support.fail
import assertk.assertions.support.appendName

$T:$N:$E =
    ByteArray:byteArray:Byte,
    IntArray:intArray:Int,
    ShortArray:shortArray:Short,
    LongArray:longArray:Long,
    FloatArray:floatArray:Float,
    DoubleArray:doubleArray:Double,
    CharArray:charArray:Char

/**
 * Returns an assert on the $T's size.
 */
@JvmName("$NSize")
fun Assert<$T>.size() = prop("size") { it.size }

/**
 * Asserts the $T contents are equal to the expected one, using [contentDeepEquals].
 * @see isNotEqualTo
 */
fun Assert<$T>.isEqualTo(expected: $T) = given { actual ->
    if (actual.contentEquals(expected)) return
    fail(expected, actual)
}

/**
 * Asserts the $T contents are not equal to the expected one, using [contentDeepEquals].
 * @see isEqualTo
 */
fun Assert<$T>.isNotEqualTo(expected: $T) = given { actual ->
    if (!(actual.contentEquals(expected))) return
    val showExpected = show(expected)
    val showActual = show(actual)
    // if they display the same, only show one.
    if (showExpected == showActual) {
        expected("to not be equal to:$showActual")
    } else {
        expected(":$showExpected not to be equal to:$showActual")
    }
}

/**
 * Asserts the $T is empty.
 * @see [isNotEmpty]
 * @see [isNullOrEmpty]
 */
@JvmName("$NIsEmpty")
fun Assert<$T>.isEmpty() = given { actual ->
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the $T is not empty.
 * @see [isEmpty]
 */
@JvmName("$NIsNotEmpty")
fun Assert<$T>.isNotEmpty() = given { actual ->
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

/**
 * Asserts the $T is null or empty.
 * @see [isEmpty]
 */
@JvmName("$NIsNullOrEmpty")
fun Assert<$T?>.isNullOrEmpty() = given { actual ->
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

/**
 * Asserts the $T has the expected size.
 */
@JvmName("$NHasSize")
fun Assert<$T>.hasSize(size: Int) {
    size().isEqualTo(size)
}

/**
 * Asserts the $T has the same size as the expected array.
 */
@JvmName("$NHasSameSizeAs")
fun Assert<$T>.hasSameSizeAs(other: $T) = given { actual ->
    val actualSize = actual.size
    val otherSize = other.size
    if (actualSize == otherSize) return
    expected("to have same size as:${show(other)} ($otherSize) but was size:($actualSize)")
}

/**
 * Returns an assert that assertion on the value at the given index in the array.
 *
 * ```
 * assertThat($NOf(0, 1, 2)).index(1).isPositive()
 * ```
 */
@JvmName("$NIndex")
fun Assert<$T>.index(index: Int): Assert<$E> =
    transform(appendName(show(index, "[]"))) { actual ->
        if (index in 0 until actual.size) {
            actual[index]
        } else {
            expected("index to be in range:[0-${actual.size}) but was:${show(index)}")
        }
    }

/**
 * Asserts on each item in the $T. The given lambda will be run for each item.
 *
 * ```
 * assertThat($NOf("one", "two")).each {
 *   it.hasLength(3)
 * }
 * ```
 */
@JvmName("$NEach")
fun Assert<$T>.each(f: (Assert<$E>) -> Unit) = given { actual ->
    all {
        actual.forEachIndexed { index, item ->
            f(assertThat(item, name = appendName(show(index, "[]"))))
        }
    }
}
