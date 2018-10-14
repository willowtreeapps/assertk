package assertk.assertions

import assertk.Assert
import assertk.all
import kotlin.reflect.KClass

/**
 * Returns an assert on the throwable's message.
 */
fun <T : Throwable> Assert<T>.message() = prop("message", Throwable::message)

/**
 * Returns an assert on the throwable's cause.
 */
fun <T : Throwable> Assert<T>.cause() = prop("cause", Throwable::cause)

/**
 * Returns an assert on the throwable's root cause.
 */
fun <T : Throwable> Assert<T>.rootCause() = prop("rootCause", Throwable::rootCause)

/**
 * Asserts the throwable has the expected message.
 */
fun <T : Throwable> Assert<T>.hasMessage(message: String?) {
    message().isEqualTo(message)
}

/**
 * Asserts the throwable is similar to the expected cause, checking the type and message.
 * @see [hasNoCause]
 */
fun <T : Throwable> Assert<T>.hasCause(cause: Throwable) {
    cause().isNotNull {
        it.kClass().isEqualTo(cause::class)
        it.hasMessage(cause.message)
    }
}

/**
 * Asserts the throwable has no cause.
 * @see [hasCause]
 */
fun <T : Throwable> Assert<T>.hasNoCause() {
    cause().isNull()
}

/**
 * Asserts the throwable is similar to the expected root cause, checking the type and message.
 */
fun <T : Throwable> Assert<T>.hasRootCause(cause: Throwable) {
    rootCause().all {
        kClass().isEqualTo(cause::class)
        hasMessage(cause.message)
    }
}

private fun Throwable.rootCause(): Throwable = this.cause?.rootCause() ?: this

