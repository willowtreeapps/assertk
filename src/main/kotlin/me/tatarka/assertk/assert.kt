package me.tatarka.assertk

import java.util.*

internal fun display(value: Any?): String {
    return when (value) {
        null -> "null"
        is String -> "\"$value\""
        is Class<*> -> value.name
        is Array<*> -> value.joinToString(prefix = "[", postfix = "]", transform = ::display)
        is Regex -> "/$value/"
        else -> value.toString()
    }
}

class Assert<T> internal constructor(val actual: T, private val failure: Failure = SimpleFailure()) : Assertable(failure) {
    fun named(name: String?): Assert<T> {
        this.name = name
        return this
    }

}

open class Assertable internal constructor(failure: Failure) : Failure by failure {
    internal var name: String? = null

    fun show(value: Any?): String = "<${display(value)}>"

    fun fail(expected: Any?, actual: Any?) {
        if (expected == null || actual == null || expected == actual) {
            expected(":${show(expected)} but was:${show(actual)}")
        } else {
            val extractor = DiffExtractor(display(expected), display(actual))
            val prefix = extractor.compactPrefix()
            val suffix = extractor.compactSuffix()
            expected(":<$prefix${extractor.expectedDiff()}$suffix> but was:<$prefix${extractor.actualDiff()}$suffix>")
        }
    }

    fun fail(message: String) = fail(AssertionError(message))

    fun expected(message: String) {
        val maybeSpace = if (message.startsWith(":")) "" else " "
        fail("expected${name(prefix = " ")}$maybeSpace$message")
    }

    fun <T> assert(actual: T): Assert<T> = Assert(actual, this).named(name)

    fun <T> assert(actual: T, f: (Assert<T>) -> Unit) {
        val assert = Assert(actual, this).named(name)
        f(assert)
    }

    fun <T> assert(f: () -> T): AssertBlock<T> {
        try {
            return AssertBlock.Value(f(), this)
        } catch (error: Throwable) {
            return AssertBlock.Error(error, this)
        }
    }

    private fun name(prefix: String = "", suffix: String = ""): String {
        return if (name.isNullOrEmpty()) {
            ""
        } else {
            "$prefix[$name]$suffix"
        }
    }
}

private val MAX_CONTEXT_LENGTH = 20

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
        if (sharedPrefix.length <= MAX_CONTEXT_LENGTH) {
            return sharedPrefix
        }
        return "..." + sharedPrefix.substring(sharedPrefix.length - MAX_CONTEXT_LENGTH)
    }

    fun compactSuffix(): String {
        if (sharedSuffix.length <= MAX_CONTEXT_LENGTH) {
            return sharedSuffix
        }
        return sharedSuffix.substring(0, MAX_CONTEXT_LENGTH) + "..."
    }

    fun expectedDiff(): String = extractDiff(expected)

    fun actualDiff(): String = extractDiff(actual)

    private fun extractDiff(source: String): String =
            "[${source.substring(sharedPrefix.length, source.length - sharedSuffix.length)}]"
}

internal interface Failure {
    fun fail(error: AssertionError)

    operator fun invoke() {
    }
}

private class SimpleFailure : Failure {
    override fun fail(error: AssertionError) {
        failWithNotInStacktrace(error)
    }
}

private class SoftFailure : Failure {
    private val failures: MutableList<AssertionError> = ArrayList()

    override fun fail(error: AssertionError) {
        failures.add(error)
    }

    override fun invoke() {
        if (!failures.isEmpty()) {
            fail(compositeErrorMessage(failures))
        }
    }

    private fun compositeErrorMessage(errors: List<AssertionError>): String {
        return if (errors.size == 1) {
            errors.first().message.orEmpty()
        } else {
            errors.joinToString(
                    prefix = "The following ${errors.size} assertions failed:\n",
                    transform = { "- ${it.message}" },
                    separator = "\n"
            )
        }
    }
}

private class TableFailure(private val table: Table) : Failure {
    private val failures: MutableMap<Int, MutableList<AssertionError>> = LinkedHashMap()

    override fun fail(error: AssertionError) {
        failures.getOrPut(table.index, { ArrayList() }) += error
    }

    override fun invoke() {
        if (!failures.isEmpty()) {
            fail(compositeErrorMessage(failures))
        }
    }

    private fun compositeErrorMessage(errors: Map<Int, List<AssertionError>>): String {
        val errorCount = errors.map { it.value.size }.sum()
        val prefix = if (errorCount == 1) {
            "The following assertion failed:\n"
        } else {
            "The following $errorCount assertions failed:\n"
        }
        return errors
                .map {
                    val (index, failures) = it
                    failures.joinToString(
                            transform = { "- ${it.message}" },
                            prefix = rowMessage(index) + "\n",
                            separator = "\n")
                }.joinToString(
                prefix = prefix,
                separator = "\n\n")
    }

    private fun rowMessage(index: Int): String {
        val row = table.rows[index]
        return table.columnNames.mapIndexed { i, name -> Pair(name, row[i]) }.joinToString(
                prefix = "on row:(",
                separator = ",",
                postfix = ")",
                transform = { "${it.first}=<${display(it.second)}>" })
    }
}

sealed class AssertBlock<T> constructor(protected val failure: Failure) {
    abstract fun throwsError(f: (Assert<Throwable>) -> Unit)
    abstract fun returnsValue(f: (Assert<T>) -> Unit)

    class Value<T> internal constructor(private val value: T, failure: Failure) : AssertBlock<T>(failure) {
        override fun throwsError(f: (Assert<Throwable>) -> Unit) = fail("expected exception but was:<${display(value)}>")

        override fun returnsValue(f: (Assert<T>) -> Unit) {
            f(Assert(value, failure))
            failure()
        }
    }

    class Error<T> internal constructor(private val error: Throwable, failure: Failure) : AssertBlock<T>(failure) {
        override fun throwsError(f: (Assert<Throwable>) -> Unit) {
            f(Assert(error, failure))
            failure()
        }

        override fun returnsValue(f: (Assert<T>) -> Unit) = fail("expected value but threw:<${display(error)}>")
    }
}

fun <T> assert(actual: T): Assert<T> = Assert(actual)

fun <T> assert(actual: T, f: (Assert<T>) -> Unit) {
    val softFailure = SoftFailure()
    val assert = Assert(actual, softFailure)
    f(assert)
    softFailure()
}

fun <T> assert(f: () -> T): AssertBlock<T> {
    val softFailure = SoftFailure()
    try {
        return AssertBlock.Value(f(), softFailure)
    } catch (error: Throwable) {
        return AssertBlock.Error(error, softFailure)
    }
}

fun assertAll(f: Assertable.() -> Unit) {
    val softFailure = SoftFailure()
    val assert = Assertable(softFailure)
    assert.f()
    softFailure()
}

internal interface TableFun {
    operator fun invoke(assert: Assertable, values: Array<out Any?>)
}

sealed class Table(internal val columnNames: Array<String>) {
    internal val rows = arrayListOf<Array<out Any?>>()
    internal var index = 0

    internal fun row(vararg values: Any?) {
        var size: Int? = null
        for (row in rows) {
            if (size == null) {
                size = row.size
            } else {
                if (size != row.size) {
                    throw IllegalArgumentException("all rows must have the same size. expected:$size but got:${row.size}")
                }
            }
        }
        rows.add(values)
    }

    internal fun forAll(f: TableFun) {
        val failure = TableFailure(this)
        val assert = Assertable(failure = failure)
        for (i in 0..rows.size - 1) {
            index = i
            f(assert, rows[i])
        }
        failure()
    }
}

class Table1<C1>(columnNames: Array<String>) : Table(columnNames) {
    fun row(val1: C1): Table1<C1> = apply {
        super.row(val1)
    }

    fun forAll(f: Assertable.(C1) -> Unit) {
        forAll(object : TableFun {
            override fun invoke(assert: Assertable, values: Array<out Any?>) {
                @Suppress("UNCHECKED_CAST")
                assert.f(values[0] as C1)
            }
        })
    }
}

class Table2<C1, C2>(columnNames: Array<String>) : Table(columnNames) {
    fun row(val1: C1, val2: C2): Table2<C1, C2> = apply {
        super.row(val1, val2)
    }

    fun forAll(f: Assertable.(C1, C2) -> Unit) {
        forAll(object : TableFun {
            override fun invoke(assert: Assertable, values: Array<out Any?>) {
                @Suppress("UNCHECKED_CAST")
                assert.f(values[0] as C1, values[1] as C2)
            }
        })
    }
}

class Table3<C1, C2, C3>(columnNames: Array<String>) : Table(columnNames) {
    fun row(val1: C1, val2: C2, val3: C3): Table3<C1, C2, C3> = apply {
        super.row(val1, val2, val3)
    }

    fun forAll(f: Assertable.(C1, C2, C3) -> Unit) {
        forAll(object : TableFun {
            override fun invoke(assert: Assertable, values: Array<out Any?>) {
                @Suppress("UNCHECKED_CAST")
                assert.f(values[0] as C1, values[1] as C2, values[2] as C3)
            }
        })
    }
}

class Table4<C1, C2, C3, C4>(columnNames: Array<String>) : Table(columnNames) {
    fun row(val1: C1, val2: C2, val3: C3, val4: C4): Table4<C1, C2, C3, C4> = apply {
        super.row(val1, val2, val3, val4)
    }

    fun forAll(f: Assertable.(C1, C2, C3, C4) -> Unit) {
        forAll(object : TableFun {
            override fun invoke(assert: Assertable, values: Array<out Any?>) {
                @Suppress("UNCHECKED_CAST")
                assert.f(values[0] as C1, values[1] as C2, values[2] as C3, values[3] as C4)
            }
        })
    }
}

fun tableOf(name1: String): Table1Builder
        = Table1Builder(arrayOf(name1))

fun tableOf(name1: String, name2: String): Table2Builder
        = Table2Builder(arrayOf(name1, name2))

fun tableOf(name1: String, name2: String, name3: String): Table3Builder
        = Table3Builder(arrayOf(name1, name2, name3))

fun tableOf(name1: String, name2: String, name3: String, name4: String): Table4Builder
        = Table4Builder(arrayOf(name1, name2, name3, name4))

sealed class TableBuilder(internal val columnNames: Array<String>)

class Table1Builder(columnNames: Array<String>) : TableBuilder(columnNames) {
    fun <C1> row(val1: C1): Table1<C1> =
            Table1<C1>(columnNames).apply { row(val1) }
}

class Table2Builder(columnNames: Array<String>) : TableBuilder(columnNames) {
    fun <C1, C2> row(val1: C1, val2: C2): Table2<C1, C2> =
            Table2<C1, C2>(columnNames).apply { row(val1, val2) }
}

class Table3Builder(columnNames: Array<String>) : TableBuilder(columnNames) {
    fun <C1, C2, C3> row(val1: C1, val2: C2, val3: C3): Table3<C1, C2, C3> =
            Table3<C1, C2, C3>(columnNames).apply { row(val1, val2, val3) }
}

class Table4Builder(columnNames: Array<String>) : TableBuilder(columnNames) {
    fun <C1, C2, C3, C4> row(val1: C1, val2: C2, val3: C3, val4: C4): Table4<C1, C2, C3, C4> =
            Table4<C1, C2, C3, C4>(columnNames).apply { row(val1, val2, val3, val4) }
}


fun catch(f: () -> Unit): Throwable? {
    try {
        f()
        return null
    } catch (e: Throwable) {
        return e
    }
}

fun fail(message: String): Nothing = failWithNotInStacktrace(AssertionError(message))

@Suppress("NOTHING_TO_INLINE", "PLATFORM_CLASS_MAPPED_TO_KOTLIN")
private inline fun failWithNotInStacktrace(error: AssertionError): Nothing {
    val filtered = error.stackTrace
            .dropWhile { it.className.startsWith("me.tatarka.assertk") }
            .toTypedArray()
    (error as java.lang.Throwable).stackTrace = filtered
    throw error
}
