package assertk.assertions.support

import assertk.Assert
import assertk.NONE
import assertk.fail

/**
 * Shows the primary value in a failure message.
 * @param value The value to display.
 * @param wrap What characters to wrap around the value. This should be a pair of characters where the first is at the
 * beginning and the second is at the end. For example, "()" will show (value). The default is "<>".
 */
fun show(value: Any?, wrap: String = "<>"): String =
    "${prefix(wrap)}${display(value)}${suffix(wrap)}"

private fun prefix(wrap: String): String = if (wrap.length > 0) wrap[0].toString() else ""
private fun suffix(wrap: String): String = if (wrap.length > 1) wrap[1].toString() else ""

internal fun display(value: Any?): String {
    return when (value) {
        null -> "null"
        is String -> "\"$value\""
        is Char -> "'$value'"
        is Long -> "${value}L"
        is Array<*> -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is Collection<*> -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is Map<*, *> -> value.entries.joinToString(
            prefix = "{",
            postfix = "}",
            transform = { (k, v) -> "${display(k)}=${display(v)}" })
        is BooleanArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is CharArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is IntArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is DoubleArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is LongArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is ShortArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is ByteArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is FloatArray -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is Pair<*, *> -> "(${display(value.first)}, ${display(value.second)})"
        is Triple<*, *, *> -> "(${display(value.first)}, ${display(value.second)}, ${display(value.third)})"
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
        val expectedDiff = extractor.expectedDiff().renderSpecialWhitespace()
        val actualDiff = extractor.actualDiff().renderSpecialWhitespace()
        expected(
            message = ":<$prefix[${expectedDiff}]$suffix> but was:<$prefix[${actualDiff}]$suffix>",
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
    val maybeInstance = if (context != null) " ${show(context, "()")}" else ""
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

private val specialWhitespace = Regex("[\r\n\t]|  +")

private fun String.renderSpecialWhitespace(): String = replace(specialWhitespace) {
    when (val v = it.value) {
        "\r" -> "\\r"
        "\n" -> "\\n"
        "\t" -> "\\t"
        else -> v
    }
}
