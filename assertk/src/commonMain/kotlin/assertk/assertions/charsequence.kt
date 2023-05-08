package assertk.assertions

import assertk.Assert
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
fun Assert<CharSequence>.isEmpty() = given { actual ->
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the char sequence is not empty.
 * @see [isEmpty]
 */
fun Assert<CharSequence>.isNotEmpty() = given { actual ->
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

/**
 * Asserts the char sequence is null or empty.
 * @see [isEmpty]
 */
fun Assert<CharSequence?>.isNullOrEmpty() = given { actual ->
    if (actual.isNullOrEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

/**
 * Asserts the char sequence has the expected length.
 */
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

/**
 * Asserts the char sequence has the expected number of lines.
 */
fun Assert<CharSequence>.hasLineCount(lineCount: Int) = given { actual ->
    val actualLineCount = actual.lines().size
    if (actualLineCount == lineCount) return
    expected("to have line count:${show(lineCount)} but was:${show(actualLineCount)}")
}

/**
 * Asserts the char sequence contains the expected subsequence.
 * @param ignoreCase true to compare ignoring case, the default if false.
 */
fun Assert<CharSequence>.contains(expected: CharSequence, ignoreCase: Boolean = false) = given { actual ->
    if (actual.contains(expected, ignoreCase)) return
    expected("to contain:${show(expected)} but was:${show(actual)}")
}

/**
 * Asserts the char sequence contains the expected subsequence(s).
 * @param ignoreCase true to compare ignoring case, the default if false.
 */
fun Assert<CharSequence>.contains(vararg expected: CharSequence, ignoreCase: Boolean = false) = given { actual ->
    if (expected.all { actual.contains(it, ignoreCase) }) return
    expected("to contain:${show(expected)} but was:${show(actual)}")
}

/**
 * Asserts the char sequence contains the expected char sequences.
 * @param ignoreCase true to compare ignoring case, the default if false.
 */
fun Assert<CharSequence>.contains(expected: Iterable<CharSequence>, ignoreCase: Boolean = false) = given { actual ->
    if (expected.all { actual.contains(it, ignoreCase) }) return
    expected("to contain:${show(expected)} but was:${show(actual)}")
}

/**
 * Asserts the char sequence does not contain the specified char sequence.
 * @param ignoreCase true to compare ignoring case, the default if false.
 */
fun Assert<CharSequence>.doesNotContain(expected: CharSequence, ignoreCase: Boolean = false) = given { actual ->
    if (!actual.contains(expected, ignoreCase)) return
    expected("to not contain:${show(expected)} but was:${show(actual)}")
}

/**
 * Asserts the char sequence does not contain the specified char sequence(s).
 * @param ignoreCase true to compare ignoring case, the default if false.
 */
fun Assert<CharSequence>.doesNotContain(vararg expected: CharSequence, ignoreCase: Boolean = false) = given { actual ->
    if (expected.none { actual.contains(it, ignoreCase) }) return
    expected("to not contain:${show(expected)} but was:${show(actual)}")
}

/**
 * Asserts the char sequence does not contain the specified char sequences.
 * @param ignoreCase true to compare ignoring case, the default if false.
 */
fun Assert<CharSequence>.doesNotContain(expected: Iterable<CharSequence>, ignoreCase: Boolean = false) = given { actual ->
    if (expected.none { actual.contains(it, ignoreCase) }) return
    expected("to not contain:${show(expected)} but was:${show(actual)}")
}

/**
 * Asserts the char sequence starts with the expected char sequence.
 * @param ignoreCase true to compare ignoring case, the default if false.
 * @see [endsWith]
 */
fun Assert<CharSequence>.startsWith(other: CharSequence, ignoreCase: Boolean = false) = given { actual ->
    if (actual.startsWith(other, ignoreCase)) return
    expected("to start with:${show(other)} but was:${show(actual)}")
}

/**
 * Asserts the char sequence ends with the expected char sequence.
 * @param ignoreCase true to compare ignoring case, the default if false.
 * @see [startsWith]
 */
fun Assert<CharSequence>.endsWith(other: CharSequence, ignoreCase: Boolean = false) = given { actual ->
    if (actual.endsWith(other, ignoreCase)) return
    expected("to end with:${show(other)} but was:${show(actual)}")
}

/**
 * Asserts the char sequence matches the expected regular expression.
 */
fun Assert<CharSequence>.matches(regex: Regex) = given { actual ->
    if (actual.matches(regex)) return
    expected("to match:${show(regex)} but was:${show(actual)}")
}

/**
 * Asserts the char sequence contains a match of the regular expression.
 */
fun Assert<CharSequence>.containsMatch(regex: Regex) = given { actual ->
    if (regex.containsMatchIn(actual)) return
    expected("to contain match:${show(regex)} but was:${show(actual)}")
}
