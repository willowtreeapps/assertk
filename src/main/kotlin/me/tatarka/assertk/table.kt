package me.tatarka.assertk

import me.tatarka.assertk.assertions.support.show

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
                transform = { "${it.first}=${show(it.second)}" })
    }
}

sealed class Table(internal val columnNames: Array<String>) {
    internal val rows = arrayListOf<Array<out Any?>>()
    internal var index = 0

    protected interface TableFun {
        operator fun invoke(values: Array<out Any?>)
    }


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

    protected fun forAll(f: TableFun) {
        FailureContext.run(TableFailure(this)) {
            for (i in 0..rows.size - 1) {
                index = i
                f(rows[i])
            }
        }
    }
}

class Table1<C1> internal constructor(columnNames: Array<String>) : Table(columnNames) {
    fun row(val1: C1): Table1<C1> = apply {
        super.row(val1)
    }

    fun forAll(f: (C1) -> Unit) {
        forAll(object : TableFun {
            override fun invoke(values: Array<out Any?>) {
                @Suppress("UNCHECKED_CAST")
                f(values[0] as C1)
            }
        })
    }
}

class Table2<C1, C2> internal constructor(columnNames: Array<String>) : Table(columnNames) {
    fun row(val1: C1, val2: C2): Table2<C1, C2> = apply {
        super.row(val1, val2)
    }

    fun forAll(f: (C1, C2) -> Unit) {
        forAll(object : TableFun {
            override fun invoke(values: Array<out Any?>) {
                @Suppress("UNCHECKED_CAST")
                f(values[0] as C1, values[1] as C2)
            }
        })
    }
}

class Table3<C1, C2, C3> internal constructor(columnNames: Array<String>) : Table(columnNames) {
    fun row(val1: C1, val2: C2, val3: C3): Table3<C1, C2, C3> = apply {
        super.row(val1, val2, val3)
    }

    fun forAll(f: (C1, C2, C3) -> Unit) {
        forAll(object : TableFun {
            override fun invoke(values: Array<out Any?>) {
                @Suppress("UNCHECKED_CAST")
                f(values[0] as C1, values[1] as C2, values[2] as C3)
            }
        })
    }
}

class Table4<C1, C2, C3, C4> internal constructor(columnNames: Array<String>) : Table(columnNames) {
    fun row(val1: C1, val2: C2, val3: C3, val4: C4): Table4<C1, C2, C3, C4> = apply {
        super.row(val1, val2, val3, val4)
    }

    fun forAll(f: (C1, C2, C3, C4) -> Unit) {
        forAll(object : TableFun {
            override fun invoke(values: Array<out Any?>) {
                @Suppress("UNCHECKED_CAST")
                f(values[0] as C1, values[1] as C2, values[2] as C3, values[3] as C4)
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

class Table1Builder internal constructor(columnNames: Array<String>) : TableBuilder(columnNames) {
    fun <C1> row(val1: C1): Table1<C1> =
            Table1<C1>(columnNames).apply { row(val1) }
}

class Table2Builder internal constructor(columnNames: Array<String>) : TableBuilder(columnNames) {
    fun <C1, C2> row(val1: C1, val2: C2): Table2<C1, C2> =
            Table2<C1, C2>(columnNames).apply { row(val1, val2) }
}

class Table3Builder internal constructor(columnNames: Array<String>) : TableBuilder(columnNames) {
    fun <C1, C2, C3> row(val1: C1, val2: C2, val3: C3): Table3<C1, C2, C3> =
            Table3<C1, C2, C3>(columnNames).apply { row(val1, val2, val3) }
}

class Table4Builder internal constructor(columnNames: Array<String>) : TableBuilder(columnNames) {
    fun <C1, C2, C3, C4> row(val1: C1, val2: C2, val3: C3, val4: C4): Table4<C1, C2, C3, C4> =
            Table4<C1, C2, C3, C4>(columnNames).apply { row(val1, val2, val3, val4) }
}

