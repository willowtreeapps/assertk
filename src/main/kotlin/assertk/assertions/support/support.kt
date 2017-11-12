package assertk.assertions.support

import assertk.Assert
import assertk.fail
import assertk.rootActual

/**
 * Shows the primary value in a failure message.
 * @param value The value to display.
 * @param wrap What characters to wrap around the value. This should be a pair of characters where the first is at the
 * beginning and the second is at the end. For example, "()" will show (value). The default is "<>".
 */
fun show(value: Any?, wrap: String = "<>"): String = "${wrap[0]}${display(value)}${wrap[1]}"

private fun display(value: Any?): String {
    return when (value) {
        null -> "null"
        is String -> "\"$value\""
        is Class<*> -> value.name
        is Array<*> -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is Collection<*> -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is Map<*, *> -> value.entries.joinToString(
                prefix = "{",
                postfix = "}",
                transform = { (k, v) -> "${display(k)}=${display(v)}" })
        is Regex -> "/$value/"
        else -> value.toString()
    }
}

/**
 * Fails an assert with the given expected and actual values.
 */
fun <T> Assert<T>.fail(expected: Any?, actual: Any?) {
    if (expected == null || actual == null || expected == actual) {
        expected(":${show(expected)} but was:${show(actual)}")
    } else {
        val extractor = DiffExtractor(display(expected), display(actual))
        val prefix = extractor.compactPrefix()
        val suffix = extractor.compactSuffix()
        expected(":<$prefix${extractor.expectedDiff()}$suffix> but was:<$prefix${extractor.actualDiff()}$suffix>")
    }
}

/**
 * Fails an assert with the given expected message. These should be in the format:
 *
 * expected("to be:${show(expected)} but was:${show(actual)}")
 *
 * -> "expected to be: <1> but was <2>"
 */
fun <T> Assert<T>.expected(message: String) {
    val maybeSpace = if (message.startsWith(":")) "" else " "
    val maybeInstance = if (parent != null) " ${show(rootActual(), "()")}" else ""
    fail("expected${formatName(name)}$maybeSpace$message$maybeInstance")
}

private fun formatName(name: String?): String {
    return if (name.isNullOrEmpty()) {
        ""
    } else {
        " [$name]"
    }
}
