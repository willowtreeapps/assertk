package assertk.assertions.support

private const val MAX_CONTEXT_LENGTH = 20

internal class DiffExtractor(val expected: String, val actual: String) {
    private val sharedPrefix: String
    private val sharedSuffix: String

    init {
        sharedPrefix = sharedPrefix()
        sharedSuffix = sharedSuffix()
    }

    private fun sharedPrefix(): String {
        val end = minOf(expected.length, actual.length)
        for (i in 0..end - 1) {
            if (expected[i] != actual[i]) {
                return expected.substring(0, i)
            }
        }
        return expected.substring(0, end)
    }

    private fun sharedSuffix(): String {
        var suffixLength = 0
        val maxSuffixLength = minOf(expected.length - sharedPrefix.length,
                actual.length - sharedPrefix.length) - 1
        while (suffixLength <= maxSuffixLength) {
            if (expected[expected.length - 1 - suffixLength] != actual[actual.length - 1 - suffixLength]) {
                break
            }
            suffixLength++
        }
        return expected.substring(expected.length - suffixLength)
    }

    fun compactPrefix(): String {
        if (sharedPrefix.length <= assertk.MAX_CONTEXT_LENGTH) {
            return sharedPrefix
        }
        return "..." + sharedPrefix.substring(sharedPrefix.length - assertk.MAX_CONTEXT_LENGTH)
    }

    fun compactSuffix(): String {
        if (sharedSuffix.length <= assertk.MAX_CONTEXT_LENGTH) {
            return sharedSuffix
        }
        return sharedSuffix.substring(0, assertk.MAX_CONTEXT_LENGTH) + "..."
    }

    fun expectedDiff(): String = extractDiff(expected)

    fun actualDiff(): String = extractDiff(actual)

    private fun extractDiff(source: String): String =
            "[${source.substring(sharedPrefix.length, source.length - sharedSuffix.length)}]"
}