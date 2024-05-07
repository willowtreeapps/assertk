package assertk.assertions

import assertk.Assert
import assertk.all

/**
 * Returns an assert on the throwable's message.
 */
fun Assert<Throwable>.havingMessage() = having("message", Throwable::message)

/**
 * Returns an assert on the throwable's cause.
 */
fun Assert<Throwable>.havingCause() = having("cause", Throwable::cause)

/**
 * Returns an assert on the throwable's root cause.
 */
fun Assert<Throwable>.havingRootCause() = having("rootCause", Throwable::rootCause)

/**
 * Asserts the throwable has the expected message.
 */
fun Assert<Throwable>.hasMessage(message: String?) {
    havingMessage().isEqualTo(message)
}

/**
 * Asserts the throwable contains the expected text
 */
fun Assert<Throwable>.messageContains(text: String) {
    havingMessage().isNotNull().contains(text)
}

/**
 * Asserts the throwable is similar to the expected cause, checking the type and message.
 * @see [hasNoCause]
 */
fun Assert<Throwable>.hasCause(cause: Throwable) {
    havingCause().isNotNull().all {
        kClass().isEqualTo(cause::class)
        hasMessage(cause.message)
    }
}

/**
 * Asserts the throwable has no cause.
 * @see [hasCause]
 */
fun Assert<Throwable>.hasNoCause() {
    havingCause().isNull()
}

/**
 * Asserts the throwable is similar to the expected root cause, checking the type and message.
 */
fun Assert<Throwable>.hasRootCause(cause: Throwable) {
    havingRootCause().all {
        kClass().isEqualTo(cause::class)
        hasMessage(cause.message)
    }
}

private fun Throwable.rootCause(): Throwable = this.cause?.rootCause() ?: this

