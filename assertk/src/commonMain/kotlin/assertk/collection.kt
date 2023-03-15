package assertk

import assertk.assertions.support.appendName
import assertk.assertions.support.show
import com.willowtreeapps.opentest4k.AssertionFailedError
import com.willowtreeapps.opentest4k.MultipleFailuresError

private class CollectionFailure<T>(
    private val check: CollectionCheck<T>.() -> Unit
) : Failure {

    var index = 0
    val collectedItems = ArrayList<T>()

    private val failures: MutableMap<Int, MutableList<Throwable>> = LinkedHashMap()

    override fun fail(error: Throwable) {
        failures.getOrPut(index, { ArrayList() }).plusAssign(error)
    }

    override fun invoke() {
        CollectionCheck<T>(collectedItems, failures).check()
    }
}

internal class CollectionCheck<T>(
    private val items: List<T>,
    private val result: Map<Int, List<Throwable>>,
) {
    val size: Int
        get() = items.size

    val failureSize: Int
        get() = result.size

    fun results(): List<Result<T>> {
        return items.mapIndexed { i, item ->
            val failures = result[i]
            if (failures != null) {
                Result.failure(compositeErrorMessage(failures))
            } else {
                Result.success(item)
            }
        }
    }

    fun success(): List<T> {
        return items.filterIndexed { index, _ -> index !in result }
    }

    fun fail(error: Throwable) {
        FailureContext.fail(error)
    }

    fun fail(message: String, results: List<Result<T>> = results()) {
        FailureContext.fail(compositeErrorMessage(results.mapNotNull { it.exceptionOrNull() }, message))
    }

    companion object {
        const val defaultMessage = "The following assertions failed"
    }

    private fun compositeErrorMessage(errors: List<Throwable>, message: String = defaultMessage): Throwable {
        return when (errors.size) {
            0 -> AssertionFailedError(message)
            1 -> errors.first()
            else -> MultipleFailuresError(message, errors).apply {
                errors.forEach(this::addSuppressed)
            }
        }
    }
}

/**
 * A custom check on a collection. Used to implement any/none/exactly etc.
 */
@Suppress("TooGenericExceptionCaught")
internal fun <T> Assert<Iterable<T>>.collection(check: CollectionCheck<T>.() -> Unit, f: (Assert<T>) -> Unit) = given { actual ->
    CollectionFailure(check).run {
        actual.forEachIndexed { i, item ->
            index = i
            collectedItems.add(item)
            try {
                f(assertThat(item, name = appendName(show(i, "[]"))))
            } catch (e: Throwable) {
                fail(e)
            }
        }
    }
}