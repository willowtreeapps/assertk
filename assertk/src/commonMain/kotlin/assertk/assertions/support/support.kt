package assertk.assertions.support

import assertk.Assert
import assertk.NONE
import assertk.ValueAssert
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

@Suppress("ComplexMethod")
internal fun display(value: Any?): String {
    return when (value) {
        null -> "null"
        is String -> "\"$value\""
        is Char -> "'$value'"
        is Long -> "${value}L"
        is Array<*> -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is Collection<*> -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is Sequence<*> -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
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
    val maybeInstance = if (context.originatingSubject != null) " (${context.displayOriginatingSubject()})" else ""

    // Attempt to extract a helpful Throwable to use as a cause if the current value
    // or originating subject are a Throwable or failure Result.
    var cause: Throwable? = null
    if (this is ValueAssert<*>) {
        cause = when (value) {
            is Throwable -> value
            is Result<*> -> value.exceptionOrNull()
            else -> null
        }
        if (cause == null) {
            cause = when (val originating = context.originatingSubject) {
                is Throwable -> originating
                is Result<*> -> originating.exceptionOrNull()
                else -> null
            }
        }
    }

    fail(
        message = "expected${formatName(name)}$maybeSpace$message$maybeInstance",
        expected = expected,
        actual = actual,
        cause = cause,
    )
}

/**
 * Fails an assert with an error message diffing the two given lists.
 */
fun <T> Assert<T>.expectedListDiff(expected: List<Any?>, actual: List<Any?>) {
    expected(listDifferExpected(expected, actual), expected, actual)
}

/**
 * Constructs a new name appending to the existing name if available using the given separator.
 * @param name The new name to append to the current name, or the new name if there is no current one.
 * @param separator The separator between the current name and the suffix if the current name exists.
 */
fun Assert<*>.appendName(name: String, separator: String = "") =
    if (this.name != null) this.name + separator + name else name

private fun formatName(name: String?): String {
    return if (name.isNullOrEmpty()) {
        ""
    } else {
        " [$name]"
    }
}

private val specialWhitespace = Regex("[\r\n\t]")

private fun String.renderSpecialWhitespace(): String = replace(specialWhitespace) {
    when (val v = it.value) {
        "\r" -> "\\r"
        "\n" -> "\\n"
        "\t" -> "\\t"
        else -> v
    }
}

private fun listDifferExpected(elements: List<Any?>, actual: List<Any?>): String {
    val diff = ListDiffer.diff(elements, actual)
        .filterIsInstance<ListDiffer.Edit.Mod>()
        .sortedBy {
            when (it) {
                is ListDiffer.Edit.Ins -> it.newIndex
                is ListDiffer.Edit.Del -> it.oldIndex
            }
        }

    return diff.joinToString(
        prefix = "to contain exactly:${show(elements)} but was:${show(actual)}\n",
        separator = "\n"
    ) { edit ->
        when (edit) {
            is ListDiffer.Edit.Del -> " at index:${edit.oldIndex} expected:${show(edit.oldValue)}"
            is ListDiffer.Edit.Ins -> " at index:${edit.newIndex} unexpected:${show(edit.newValue)}"
        }
    }
}