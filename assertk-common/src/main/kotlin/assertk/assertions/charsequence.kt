package assertk.assertions

import assertk.Assert
import assertk.PlatformName
import assertk.assertions.support.expected
import assertk.assertions.support.show

/**
 * Returns an assert on the CharSequence's length.
 */
fun Assert<CharSequence>.length() = prop("length", CharSequence::length)

/**
 * Asserts the char sequence is empty.
 * @see [isNotEmpty]
 * @see [isNullOrEmpty]
 */
@PlatformName("isCharSequenceEmpty")
fun Assert<CharSequence>.isEmpty() = given { actual ->
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the char sequence is not empty.
 * @see [isEmpty]
 */
@PlatformName("isCharSequenceNotEmpty")
fun Assert<CharSequence>.isNotEmpty() = given { actual ->
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

/**
 * Asserts the char sequence is null or empty.
 * @see [isEmpty]
 */
@PlatformName("isCharSequenceNullOrEmpty")
fun Assert<CharSequence?>.isNullOrEmpty() = given { actual ->
    if (actual.isNullOrEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

/**
 * Asserts the char sequence has the expected length.
 */
@PlatformName("charSequenceHasLength")
fun Assert<CharSequence>.hasLength(length: Int) {
    length().isEqualTo(length)
}

/**
 * Asserts the char sequence has the same length as the expected one.
 */
fun Assert<CharSequence>.hasSameLengthAs(other: CharSequence) = given { actual ->
    val actualLength = actual.length
    val otherLength = other.length
    if (actualLength == otherLength) return
    expected("to have same length as:${show(other)} ($otherLength) but was:${show(actual)} ($actualLength)")
}
