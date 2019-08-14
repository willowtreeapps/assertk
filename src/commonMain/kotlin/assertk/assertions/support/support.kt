package assertk.assertions.support

import assertk.Assert
import assertk.NONE
import assertk.assertThat
import assertk.fail

/**
 * Shows the primary value in a failure message.
 * @param value The value to display.
 * @param wrap What characters to wrap around the value. This should be a pair of characters where the first is at the
 * beginning and the second is at the end. For example, "()" will show (value). The default is "<>".
 */
fun show(value: Any?, wrap: String = "<>"): String = assertThat(Unit).show(value, wrap)

/**
 * Shows the primary value in a failure message.
 * @param value The value to display.
 * @param wrap What characters to wrap around the value. This should be a pair of characters where the first is at the
 * beginning and the second is at the end. For example, "()" will show (value). The default is "<>".
 */
fun Assert<Any?>.show(value: Any?, wrap: String = "<>"): String =
        "${prefix(wrap)}${display(value, displayWith)}${suffix(wrap)}"

private fun prefix(wrap: String): String = if (wrap.length > 0) wrap[0].toString() else ""
private fun suffix(wrap: String): String = if (wrap.length > 1) wrap[1].toString() else ""

internal fun Assert<Any?>.display(value: Any?): String = display(value, displayWith)

internal fun display(value: Any?, displayWith: (Any?) -> String?): String {
    val custom = displayWith(value)
    if (custom != null) {
        return custom
    }
    return when (value) {
        null -> "null"
        is String -> "\"$value\""
        is Char -> "'$value'"
        is Long -> "${value}L"
        is Array<*> -> value.joinToString(prefix = "[", postfix = "]", transform = { display(it, displayWith) })
        is Collection<*> -> value.joinToString(prefix = "[", postfix = "]", transform = { display(it, displayWith) })
        is Map<*, *> -> value.entries.joinToString(
                prefix = "{",
                postfix = "}",
                transform = { (k, v) -> "${display(k, displayWith)}=${display(v, displayWith)}" })
        is BooleanArray -> value.joinToString(prefix = "[", postfix = "]", transform = { display(it, displayWith) })
        is CharArray -> value.joinToString(prefix = "[", postfix = "]", transform = { display(it, displayWith) })
        is IntArray -> value.joinToString(prefix = "[", postfix = "]", transform = { display(it, displayWith) })
        is DoubleArray -> value.joinToString(prefix = "[", postfix = "]", transform = { display(it, displayWith) })
        is LongArray -> value.joinToString(prefix = "[", postfix = "]", transform = { display(it, displayWith) })
        is ShortArray -> value.joinToString(prefix = "[", postfix = "]", transform = { display(it, displayWith) })
        is ByteArray -> value.joinToString(prefix = "[", postfix = "]", transform = { display(it, displayWith) })
        is FloatArray -> value.joinToString(prefix = "[", postfix = "]", transform = { display(it, displayWith) })
        is Pair<*, *> -> "(${display(value.first, displayWith)}, ${display(value.second, displayWith)})"
        is Triple<*, *, *> -> "(${display(value.first, displayWith)}, ${display(value.second, displayWith)}, ${display(value.third, displayWith)})"
        else -> displayPlatformSpecific(value)
    }
}

internal expect fun displayPlatformSpecific(value: Any?): String

/**
 * Fails an assert with the given expected and actual values.
 */
fun <T> Assert<T>.fail(expected: Any?, actual: Any?) {
    if (expected == null || actual == null || expected == actual) {
        expected(message = ":${show(expected)} but was:${show(actual)}", expected = expected, actual = actual)
    } else {
        val extractor = DiffExtractor(display(expected), display(actual))
        val prefix = extractor.compactPrefix()
        val suffix = extractor.compactSuffix()
        expected(
                message = ":<$prefix${extractor.expectedDiff()}$suffix> but was:<$prefix${extractor.actualDiff()}$suffix>",
                expected = expected,
                actual = actual
        )
    }
}

/**
 * Fails an assert with the given expected message. These should be in the format:
 *
 * expected("to be:${show(expected)} but was:${show(actual)}")
 *
 * -> "expected to be: <1> but was <2>"
 */
fun <T> Assert<T>.expected(message: String, expected: Any? = NONE, actual: Any? = NONE): Nothing {
    val maybeSpace = if (message.startsWith(":")) "" else " "
    val maybeInstance = if (subject != null) " ${show(subject, "()")}" else ""
    fail(
            message = "expected${formatName(name)}$maybeSpace$message$maybeInstance",
            expected = expected,
            actual = actual
    )
}

private fun formatName(name: String?): String {
    return if (name.isNullOrEmpty()) {
        ""
    } else {
        " [$name]"
    }
}
