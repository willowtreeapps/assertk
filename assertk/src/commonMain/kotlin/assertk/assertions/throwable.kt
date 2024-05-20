package assertk.assertions

import assertk.Assert
import assertk.all

/**
 * Returns an assert on the throwable's message.
 */
fun Assert<Throwable>.havingMessage() = having("message", Throwable::message)

@Deprecated(
    message = "Function message has been renamed to havingMessage",
    replaceWith = ReplaceWith("havingMessage()"),
    level = DeprecationLevel.WARNING
)
fun Assert<Throwable>.message() = havingMessage()

/**
 * Returns an assert on the throwable's cause.
 */
fun Assert<Throwable>.havingCause() = having("cause", Throwable::cause)

@Deprecated(
    message = "Function cause has been renamed to havingCause",
    replaceWith = ReplaceWith("havingCause()"),
    level = DeprecationLevel.WARNING
)
fun Assert<Throwable>.cause() = havingCause()

/**
 * Returns an assert on the throwable's root cause.
 */
fun Assert<Throwable>.havingRootCause() = having("rootCause", Throwable::rootCause)

@Deprecated(
    message = "Function rootCause has been renamed to havingRootCause",
    replaceWith = ReplaceWith("havingRootCause()"),
    level = DeprecationLevel.WARNING
)
fun Assert<Throwable>.rootCause() = havingRootCause()

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
        havingKClass().isEqualTo(cause::class)
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
        havingKClass().isEqualTo(cause::class)
        hasMessage(cause.message)
    }
}

private fun Throwable.rootCause(): Throwable = this.cause?.rootCause() ?: this

