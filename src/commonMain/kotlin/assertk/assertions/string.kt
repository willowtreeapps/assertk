package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show

/**
 * Asserts the string has the expected number of lines.
 */
fun Assert<String>.hasLineCount(lineCount: Int) = given { actual ->
    val actualLineCount = actual.lines().size
    if (actualLineCount == lineCount) return
    expected("to have line count:${show(lineCount)} but was:${show(actualLineCount)}")
}

/**
 * Asserts the string is equal to the expected string.
 * @param ignoreCase true to compare ignoring case, the default if false.
 * @see [isNotEqualTo]
 */
fun Assert<String?>.isEqualTo(other: String?, ignoreCase: Boolean = false) = given { actual ->
    if (actual.equals(other, ignoreCase)) return
    fail(other, actual)
}

/**
 * Asserts the string is not equal to the expected string.
 * @param ignoreCase true to compare ignoring case, the default if false.
 * @see [isEqualTo]
 */
fun Assert<String?>.isNotEqualTo(other: String?, ignoreCase: Boolean = false) = given { actual ->
    if (!actual.equals(other, ignoreCase)) return
    if (ignoreCase) {
        expected(":${show(other)} not to be equal to (ignoring case):${show(actual)}")
    } else {
        expected("to not be equal to:${show(actual)}")
    }
}

/**
 * Asserts the string contains the expected substring(s).
 * @param ignoreCase true to compare ignoring case, the default if false.
 */
fun Assert<String>.contains(expected: CharSequence, ignoreCase: Boolean = false) {
    contains(listOf(expected), ignoreCase)
}

/**
 * Asserts the string contains the expected substring(s).
 * @param ignoreCase true to compare ignoring case, the default if false.
 */
fun Assert<String>.contains(vararg expected: CharSequence, ignoreCase: Boolean = false) {
    contains(expected.toList(), ignoreCase)
}

/**
 * Asserts the string contains the expected strings.
 * @param ignoreCase true to compare ignoring case, the default if false.
 */
fun Assert<String>.contains(expected: Iterable<CharSequence>, ignoreCase: Boolean = false) {
    given { actual ->
        if (expected.all { actual.contains(it, ignoreCase) }) return
        expected("to contain:${show(expected)} but was:${show(actual)}")
    }
}

/**
 * Asserts the string does not contain the specified string.
 * @param ignoreCase true to compare ignoring case, the default if false.
 */
fun Assert<String>.doesNotContain(other: CharSequence, ignoreCase: Boolean = false) = given { actual ->
    if (!actual.contains(other, ignoreCase)) return
    expected("to not contain:${show(other)}")
}

/**
 * Asserts the string starts with the expected string.
 * @param ignoreCase true to compare ignoring case, the default if false.
 * @see [endsWith]
 */
fun Assert<String>.startsWith(other: String, ignoreCase: Boolean = false) = given { actual ->
    if (actual.startsWith(other, ignoreCase)) return
    expected("to start with:${show(other)} but was:${show(actual)}")
}

/**
 * Asserts the string ends with the expected string.
 * @param ignoreCase true to compare ignoring case, the default if false.
 * @see [startsWith]
 */
fun Assert<String>.endsWith(other: String, ignoreCase: Boolean = false) = given { actual ->
    if (actual.endsWith(other, ignoreCase)) return
    expected("to end with:${show(other)} but was:${show(actual)}")
}

/**
 * Asserts the string matches the expected regular expression.
 */
fun Assert<String>.matches(regex: Regex) = given { actual ->
    if (actual.matches(regex)) return
    expected("to match:${show(regex)} but was:${show(actual)}")
}
