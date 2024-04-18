package assertk.assertions

import assertk.Assert
import assertk.all
import assertk.assertions.support.appendName
import assertk.assertions.support.expected
import assertk.assertions.support.expectedListDiff
import assertk.assertions.support.show
import assertk.collection

/**
 * Asserts the sequence contains the expected element, using `in`.
 * @see [doesNotContain]
 */
fun Assert<Sequence<*>>.contains(element: Any?) = given { actual ->
    val actualList = actual.toList()
    if (element in actualList) return
    expected("to contain:${show(element)} but was:${show(actualList)}")
}

/**
 * Asserts the sequence does not contain the expected element, using `!n`.
 * @see [contains]
 */
fun Assert<Sequence<*>>.doesNotContain(element: Any?) = given { actual ->
    val actualList = actual.toList()
    if (element !in actualList) return
    expected("to not contain:${show(element)} but was:${show(actualList)}")
}

/**
 * Asserts the sequence does not contain any of the expected elements.
 * @see [containsAtLeast]
 */
fun Assert<Sequence<*>>.containsNone(vararg elements: Any?) = given { actual ->
    val actualList = actual.toList()
    val notExpected = elements.filter { it in actualList }
    if (notExpected.isEmpty()) {
        return
    }
    expected(
        "to contain none of:${show(elements)} but was:${show(actualList)}\n elements not expected:${
            show(
                notExpected
            )
        }"
    )
}

/**
 * Asserts the sequence contains at least the expected elements, in any order. The collection may also
 * contain additional elements.
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsOnly]
 */
@Deprecated("renamed to containsAtLeast", ReplaceWith("containsAtLeast(*elements)"))
fun Assert<Sequence<*>>.containsAll(vararg elements: Any?) = containsAtLeast(*elements)

/**
 * Asserts the sequence contains at least the expected elements, in any order. The collection may also
 * contain additional elements.
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsOnly]
 */
fun Assert<Sequence<*>>.containsAtLeast(vararg elements: Any?) = given { actual ->
    val actualList = actual.toList()
    val notFound = elements.filterNot { it in actualList }
    if (notFound.isEmpty()) {
        return
    }
    expected("to contain all:${show(elements)} but was:${show(actualList)}\n elements not found:${show(notFound)}")
}

/**
 * Asserts the sequence contains only the expected elements, in any order. Duplicate values
 * in the expected and actual are ignored.
 *
 * [1, 2] containsOnly [2, 1] passes
 * [1, 2, 2] containsOnly [2, 1] passes
 * [1, 2] containsOnly [2, 2, 1] passes
 *
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsAtLeast]
 * @see [containsExactlyInAnyOrder]
 */
fun Assert<Sequence<*>>.containsOnly(vararg elements: Any?) = given { actual ->
    val actualList = actual.toList()
    val notInActual = elements.filterNot { it in actualList }
    val notInExpected = actualList.filterNot { it in elements }
    if (notInExpected.isEmpty() && notInActual.isEmpty()) {
        return
    }
    expected(StringBuilder("to contain only:${show(elements)} but was:${show(actualList)}").apply {
        if (notInActual.isNotEmpty()) {
            append("\n elements not found:${show(notInActual)}")
        }
        if (notInExpected.isNotEmpty()) {
            append("\n extra elements found:${show(notInExpected)}")
        }
    }.toString())
}

/**
 * Asserts the sequence contains exactly the expected elements. They must be in the same order and
 * there must not be any extra elements.
 *
 * [1, 2] containsExactly [2, 1] fails
 * [1, 2, 2] containsExactly [2, 1] fails
 * [1, 2] containsExactly [1, 2, 2] fails
 *
 * @see [containsAtLeast]
 * @see [containsOnly]
 * @see [containsExactlyInAnyOrder]
 */
fun Assert<Sequence<*>>.containsExactly(vararg elements: Any?) = given { actual ->
    val actualList = actual.toList()
    if (actualList.contentEquals(elements)) return

    expectedListDiff(elements.toList(), actualList)
}

/**
 * Asserts the sequence contains exactly the expected elements, in any order. Each value in expected
 * must correspond to a matching value in actual, and visa-versa.
 *
 * [1, 2] containsExactlyInAnyOrder [2, 1] passes
 * [1, 2, 2] containsExactlyInAnyOrder [2, 1] fails
 * [1, 2] containsExactlyInAnyOrder [2, 2, 1] fails
 *
 * @see [containsNone]
 * @see [containsExactly]
 * @see [containsAtLeast]
 * @see [containsOnly]
 */
fun Assert<Sequence<*>>.containsExactlyInAnyOrder(vararg elements: Any?) = given { actual ->
    val actualList = actual.toList()
    val notInActual = elements.toMutableList()
    val notInExpected = actualList.toMutableList()
    elements.forEach {
        if (notInExpected.contains(it)) {
            notInExpected.removeFirst(it)
            notInActual.removeFirst(it)
        }
    }
    if (notInExpected.isEmpty() && notInActual.isEmpty()) {
        return
    }
    expected(StringBuilder("to contain exactly in any order:${show(elements)} but was:${show(actualList)}").apply {
        if (notInActual.isNotEmpty()) {
            append("\n elements not found:${show(notInActual)}")
        }
        if (notInExpected.isNotEmpty()) {
            append("\n extra elements found:${show(notInExpected)}")
        }
    }.toString())
}

/**
 * Asserts on each item in the sequence. The given lambda will be run for each item.
 *
 * ```
 * assertThat(sequenceOf("one", "two")).each {
 *   it.hasLength(3)
 * }
 * ```
 */
fun <E> Assert<Sequence<E>>.each(f: (Assert<E>) -> Unit) = given { actual ->
    all {
        actual.forEachIndexed { index, item ->
            f(assertThat(item, name = appendName(show(index, "[]"))))
        }
    }
}

/**
 * Extracts a value of from each item in the sequence, allowing you to assert on a list of those values.
 *
 * ```
 * assertThat(people)
 *   .eachHaving(Person::name)
 *   .containsExactly("Sue", "Bob")
 * ```
 */
fun <E, R> Assert<Sequence<E>>.eachHaving(f1: (E) -> R): Assert<Sequence<R>> = transform { actual ->
    actual.map(f1)
}

@Deprecated(
    message = "Function extracting has been renamed to eachHaving",
    replaceWith = ReplaceWith("eachHaving(f1)"),
    level = DeprecationLevel.WARNING
)
fun <E, R> Assert<Sequence<E>>.extracting(f1: (E) -> R): Assert<Sequence<R>> = transform { actual ->
    actual.map(f1)
}

/**
 * Extracts two values of from each item in the sequence, allowing you to assert on a list of paris of those values.
 *
 * ```
 * assertThat(people)
 *   .eachHaving(Person::name, Person::age)
 *   .containsExactly("Sue" to 20, "Bob" to 22)
 * ```
 */
fun <E, R1, R2> Assert<Sequence<E>>.eachHaving(f1: (E) -> R1, f2: (E) -> R2): Assert<Sequence<Pair<R1, R2>>> =
    transform { actual ->
        actual.map { f1(it) to f2(it) }
    }

@Deprecated(
    message = "Function extracting has been renamed to eachHaving",
    replaceWith = ReplaceWith("eachHaving(f1, f2)"),
    level = DeprecationLevel.WARNING
)
fun <E, R1, R2> Assert<Sequence<E>>.extracting(f1: (E) -> R1, f2: (E) -> R2): Assert<Sequence<Pair<R1, R2>>> =
    transform { actual ->
        actual.map { f1(it) to f2(it) }
    }

/**
 * Extracts three values from each item in the sequence, allowing you to assert on a list of triples of those values.
 *
 * ```
 * assertThat(people)
 *   .eachHaving(Person::name, Person::age, Person::address)
 *   .contains(Triple("Sue", 20, "123 Street"), Triple("Bob", 22, "456 Street")
 * ```
 */
fun <E, R1, R2, R3> Assert<Sequence<E>>.eachHaving(
    f1: (E) -> R1,
    f2: (E) -> R2,
    f3: (E) -> R3
): Assert<Sequence<Triple<R1, R2, R3>>> = transform { actual ->
    actual.map { Triple(f1(it), f2(it), f3(it)) }
}

@Deprecated(
    message = "Function extracting has been renamed to eachHaving",
    replaceWith = ReplaceWith("eachHaving(f1, f2, f3)"),
    level = DeprecationLevel.WARNING
)
fun <E, R1, R2, R3> Assert<Sequence<E>>.extracting(
    f1: (E) -> R1,
    f2: (E) -> R2,
    f3: (E) -> R3
): Assert<Sequence<Triple<R1, R2, R3>>> = transform { actual ->
    actual.map { Triple(f1(it), f2(it), f3(it)) }
}

/**
 * Asserts on each item in the sequence, passing if none of the items pass.
 * The given lambda will be run for each item.
 *
 * ```
 * assertThat(sequenceOf("one", "two")).none {
 *   it.hasLength(2)
 * }
 * ```
 */
fun <E> Assert<Sequence<E>>.none(f: (Assert<E>) -> Unit) =
    collection(check = {
        if (size != failureSize) {
            fail(
                "expected none to pass\n" + results().mapIndexedNotNull { index, result ->
                    val result = result.getOrNull()
                    if (result != null) {
                        " at index:$index passed:${show(result)}"
                    } else {
                        null
                    }
                }.joinToString("\n"),
                emptyList()
            )
        }
    }, f)

/**
 * Asserts on each item in the sequence, passing if at least `times` items pass.
 * The given lambda will be run for each item.
 *
 * ```
 * assertThat(sequenceOf(-1, 1, 2)).atLeast(2) { it.isPositive() }
 * ```
 */
fun <E, T : Sequence<E>> Assert<T>.atLeast(times: Int, f: (Assert<E>) -> Unit) =
    collection(check = {
        if (size - failureSize < times) {
            fail("expected to pass at least $times times")
        }
    }, f)

/**
 * Asserts on each item in the sequence, passing if at most `times` items pass.
 * The given lambda will be run for each item.
 *
 * ```
 * assertThat(sequenceOf(-2, -1, 1)).atMost(2) { it.isPositive() }
 * ```
 */
fun <E, T : Sequence<E>> Assert<T>.atMost(times: Int, f: (Assert<E>) -> Unit) =
    collection(check = {
        if (size - failureSize > times) {
            fail("expected to pass at most $times times")
        }
    }, f)

/**
 * Asserts on each item in the sequence, passing if exactly `times` items pass.
 * The given lambda will be run for each item.
 *
 * ```
 * assertThat(sequenceOf(-1, 1, 2)).exactly(2) { it.isPositive() }
 * ```
 */
fun <E, T : Sequence<E>> Assert<T>.exactly(times: Int, f: (Assert<E>) -> Unit) =
    collection(check = {
        if (size - failureSize != times) {
            fail("expected to pass exactly $times times")
        }
    }, f)

/**
 * Asserts on each item in the sequence, passing if any of the items pass.
 * The given lambda will be run for each item.
 *
 * ```
 * assertThat(sequenceOf(-1, -2, 1)).any { it.isPositive() }
 * ```
 */
fun <E, T : Sequence<E>> Assert<T>.any(f: (Assert<E>) -> Unit) =
    collection(check = {
        if (size == failureSize) {
            fail("expected any item to pass", results())
        }
    }, f)

/**
 * Asserts the sequence is empty.
 * @see [isNotEmpty]
 * @see [isNullOrEmpty]
 */
fun Assert<Sequence<*>>.isEmpty() = given { actual ->
    val actualList = actual.toList()
    if (actualList.isEmpty()) return
    expected("to be empty but was:${show(actualList)}")
}

/**
 * Asserts the sequence is not empty.
 * @see [isEmpty]
 */
fun Assert<Sequence<*>>.isNotEmpty() = given { actual ->
    if (actual.any()) return
    expected("to not be empty")
}
