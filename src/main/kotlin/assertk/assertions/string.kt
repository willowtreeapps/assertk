package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show

fun Assert<String>.hasLineCount(lineCount: Int) {
    val actualLineCount = actual.lines().size
    if (actualLineCount == lineCount) return
    expected("To have line count:${show(lineCount)} but was:${show(actualLineCount)}")
}

fun Assert<String>.isEqualTo(other: String, ignoreCase: Boolean = false) {
    if (actual.equals(other, ignoreCase)) return
    fail(other, actual)
}

fun Assert<String>.isNotEqualTo(other: String, ignoreCase: Boolean = false) {
    if (!actual.equals(other, ignoreCase)) return
    expected(":${show(other)} not to be equal to:${show(actual)}")
}

fun Assert<String>.contains(other: CharSequence, ignoreCase: Boolean = false) {
    if (actual.contains(other, ignoreCase)) return
    expected("to contain:${show(other)} but was:${show(actual)}")
}

fun Assert<String>.startsWith(other: String, ignoreCase: Boolean = false) {
    if (actual.startsWith(other, ignoreCase)) return
    expected("to start with:${show(other)} but was:${show(actual)}")
}

fun Assert<String>.endsWith(other: String, ignoreCase: Boolean = false) {
    if (actual.endsWith(other, ignoreCase)) return
    expected("to end with:${show(other)} but was:${show(actual)}")
}

fun Assert<String>.matches(regex: Regex) {
    if (actual.matches(regex)) return
    expected("to match:${show(regex)} but was:${show(actual)}")
}
