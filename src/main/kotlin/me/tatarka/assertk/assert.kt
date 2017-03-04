package me.tatarka.assertk

import org.junit.ComparisonFailure
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

class Assert<T> internal constructor(val actual: T, private val failure: Failure = SimpleFailure()) : Failure by failure {
    var name: String? = null

    fun fail(expected: Any?, actual: T) = fail(ComparisonFailure(name(), display(expected), display(actual)))
    fun fail(message: String) = fail(AssertionError(message))
    fun expected(message: String) {
        val maybeSpace = if (message.startsWith(":")) "" else " "
        fail("expected${name(prefix = " ")}$maybeSpace$message")
    }

    fun named(name: String?): Assert<T> {
        this.name = name
        return this
    }

    private fun name(prefix: String = "", suffix: String = ""): String {
        return if (name.isNullOrEmpty()) {
            ""
        } else {
            "$prefix[$name]$suffix"
        }
    }

    fun show(value: Any?): String = "<${display(value)}>"

    fun <T> assert(actual: T): Assert<T> = Assert(actual, failure).named(name)

    fun <T> assert(actual: T, f: (Assert<T>) -> Unit) {
        val assert = Assert(actual, failure).named(name)
        f(assert)
    }
}

interface TestVerb {
    fun <T> that(actual: T): Assert<T>
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
            errors
                    .mapIndexed { i, error -> "$i) ${error.message}" }
                    .joinToString(
                            prefix = "The following ${errors.size} assertions failed:\n",
                            separator = "\n"
                    )
        }
    }
}

class Table {
    internal val rows: MutableList<TableRow<*>> = ArrayList()
    internal var size: Int? = null
        private set
    internal var index: Int = 0
    internal var running = false

    fun <T> row(name: String, vararg values: T): TableRow<T> {
        if (values.isEmpty()) {
            throw IllegalArgumentException("row must have at least one value")
        }
        val row = TableRow(name, values, this)
        if (size == null) {
            size = row.size
        } else {
            if (size != row.size) {
                throw IllegalArgumentException("all rows must have the same size. expected:$size but got:${row.size}")
            }
        }
        rows += row
        return row
    }

    fun assert(f: TestVerb.() -> Unit) {
        val failure = TableFailure(this)
        val verb = object : TestVerb {
            override fun <T> that(actual: T): Assert<T> = Assert(actual, failure)
        }
        val size = size ?: 0
        running = true
        for (i in 0..size - 1) {
            index = i
            verb.f()
        }
        running = false
        failure()
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
        return table.rows.joinToString(
                prefix = "on row:(",
                separator = ",",
                postfix = ")",
                transform = { "${it.name}=<${display(it[index])}>" })
    }
}

class TableRow<out T>(val name: String, private val items: Array<out T>, private val table: Table) {
    val size = items.size

    operator fun invoke(): T {
        if (!table.running) {
            throw IllegalStateException("cannot access row value outside run block")
        }
        return items[table.index]
    }

    operator fun get(i: Int) = items[i]

    override fun equals(other: Any?): Boolean {
        if (other !is TableRow<*>) {
            return false
        }
        return this() == other()
    }

    override fun hashCode(): Int = this()?.hashCode() ?: 0

    override fun toString(): String = buildString {
        append("row(\"").append(name).append("\", ")
        for (i in 0..items.size - 1) {
            val item = this@TableRow[i]
            if (i == table.index) {
                append("<").append(item).append(">")
            } else {
                append(item)
            }
            if (i != items.size - 1) {
                append(", ")
            }
        }
        append(")")
    }
}

sealed class AssertBlock<T> constructor(protected val failure: Failure) {
    abstract infix fun thrown(f: (Assert<Throwable>) -> Unit)
    abstract infix fun returned(f: (Assert<T>) -> Unit)

    class Value<T> internal constructor(private val value: T, failure: Failure) : AssertBlock<T>(failure) {
        override fun thrown(f: (Assert<Throwable>) -> Unit) = fail("expected exception but was:<${display(value)}>")

        override fun returned(f: (Assert<T>) -> Unit) {
            f(Assert(value, failure))
            failure()
        }
    }

    class Error<T> internal constructor(private val error: Throwable, failure: Failure) : AssertBlock<T>(failure) {
        override fun thrown(f: (Assert<Throwable>) -> Unit) {
            f(Assert(error, failure))
            failure()
        }

        override fun returned(f: (Assert<T>) -> Unit) = fail("expected value but threw:<${display(error)}>")
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

fun expect(f: TestVerb.() -> Unit) {
    val softFailure = SoftFailure()
    val testVerb = object : TestVerb {
        override fun <T> that(actual: T): Assert<T> = Assert(actual, softFailure)
    }
    testVerb.f()
    softFailure()
}

fun table(f: Table.() -> Unit) = Table().f()

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
    val filtered = error.stackTrace.drop(1).toTypedArray()
    (error as java.lang.Throwable).stackTrace = filtered
    throw error
}
