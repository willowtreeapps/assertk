package assertk.assertions.support

import assertk.Assert
import assertk.fail

/**
 * Shows a value in a failure message.
 */
fun show(value: Any?): String = "<${display(value)}>"

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
    fail("expected${formatName(name)}$maybeSpace$message")
}

private fun formatName(name: String?): String {
    return if (name.isNullOrEmpty()) {
        ""
    } else {
        " [$name]"
    }
}
