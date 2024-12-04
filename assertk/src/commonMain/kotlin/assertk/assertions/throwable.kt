package assertk.assertions

import assertk.Assert
import assertk.all
import kotlin.reflect.KProperty1

/**
 * Returns an assert on the throwable's message.
 */
fun Assert<Throwable>.message() = having("message", Throwable::message)

/**
 * Returns an assert on the throwable's cause.
 */
fun Assert<Throwable>.cause() = having("cause", Throwable::cause)

/**
 * Returns an assert on the throwable's root cause.
 */
fun Assert<Throwable>.rootCause() = having("rootCause", Throwable::rootCause)

/**
 * Asserts the throwable has the expected message.
 */
fun Assert<Throwable>.hasMessage(message: String?) {
    message().isEqualTo(message)
}

/**
 * Asserts the throwable contains the expected text
 */
fun Assert<Throwable>.messageContains(text: String) {
    message().isNotNull().contains(text)
}

/**
 * Asserts the throwable is similar to the expected cause, checking the type and message.
 * @see [hasNoCause]
 */
fun Assert<Throwable>.hasCause(cause: Throwable) {
    cause().isNotNull().all {
        kClass().isEqualTo(cause::class)
        hasMessage(cause.message)
    }
}

/**
 * Asserts the throwable has no cause.
 * @see [hasCause]
 */
fun Assert<Throwable>.hasNoCause() {
    cause().isNull()
}

/**
 * Asserts the throwable is similar to the expected root cause, checking the type and message.
 */
fun Assert<Throwable>.hasRootCause(cause: Throwable) {
    rootCause().all {
        kClass().isEqualTo(cause::class)
        hasMessage(cause.message)
    }
}

/**
 * Asserts the throwable with a specific type have the expected properties for it.
 */
fun <T: Throwable> Assert<T>.hasProperties(vararg pairs: Pair<KProperty1<T, Any>, Any>) {
    all {
        pairs.forEach {
            prop(it.first).isEqualTo(it.second)
        }
    }
}

private fun Throwable.rootCause(): Throwable = this.cause?.rootCause() ?: this

