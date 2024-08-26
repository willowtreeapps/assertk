package assertk.assertions

import assertk.Assert
import assertk.assertions.support.*

/**
 * Returns an assert on the Collection's size.
 */
fun Assert<Collection<*>>.havingSize() = having("size", Collection<*>::size)

@Deprecated(
    message = "Function size has been renamed to havingSize",
    replaceWith = ReplaceWith("havingSize()"),
    level = DeprecationLevel.WARNING
)
fun Assert<Collection<*>>.size() = havingSize()

/**
 * Asserts the collection is empty.
 * @see [isNotEmpty]
 * @see [isNullOrEmpty]
 */
fun Assert<Collection<*>>.isEmpty() = given { actual ->
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

/**
 * Asserts the collection is not empty.
 * @see [isEmpty]
 */
fun Assert<Collection<*>>.isNotEmpty() = given { actual ->
    if (actual.isNotEmpty()) return
    expected("to not be empty")
}

/**
 * Asserts the collection is null or empty.
 * @see [isEmpty]
 */
fun Assert<Collection<*>?>.isNullOrEmpty() = given { actual ->
    if (actual == null || actual.isEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

/**
 * Asserts the collection has the expected size.
 */
fun Assert<Collection<*>>.hasSize(size: Int) {
    havingSize().isEqualTo(size)
}

/**
 * Asserts the collection has the same size as the expected collection.
 */
fun Assert<Collection<*>>.hasSameSizeAs(other: Collection<*>) = given { actual ->
    val actualSize = actual.size
    val otherSize = other.size
    if (actualSize == otherSize) return
    expected("to have same size as:${show(other)} ($otherSize) but was size:($actualSize)")
}
