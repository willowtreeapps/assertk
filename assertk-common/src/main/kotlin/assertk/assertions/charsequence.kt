package assertk.assertions

import assertk.Assert
import assertk.PlatformName
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Returns an assert on the CharSequence's length.
 */
fun <T : CharSequence> Assert<T>.length() = prop("length", CharSequence::length)

/**
 * Asserts the char sequence is empty.
 * @see [isNotEmpty]
 * @see [isNullOrEmpty]
 */
@PlatformName("isCharSequenceEmpty")
fun <T : CharSequence> Assert<T>.isEmpty() {
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the char sequence is not empty.
 * @see [isEmpty]
 */
@PlatformName("isCharSequenceNotEmpty")
fun <T : CharSequence> Assert<T>.isNotEmpty() {
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

/**
 * Asserts the char sequence is null or empty.
 * @see [isEmpty]
 */
@PlatformName("isCharSequenceNullOrEmpty")
fun <T : CharSequence?> Assert<T>.isNullOrEmpty() {
    if (actual.isNullOrEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

/**
 * Asserts the char sequence has the expected length.
 */
@PlatformName("charSequenceHasLength")
fun <T : CharSequence> Assert<T>.hasLength(length: Int) {
    length().isEqualTo(length)
}

/**
 * Asserts the char sequence has the same length as the expected one.
 */
fun <T : CharSequence> Assert<T>.hasSameLengthAs(other: CharSequence) {
    val actualLength = actual.length
    val otherLength = other.length
    if (actualLength == otherLength) return
    expected("to have same length as:${show(other)} ($otherLength) but was:${show(actual)} ($actualLength)")
}
