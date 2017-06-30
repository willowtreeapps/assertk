package assertk.assertions

import assertk.Assert
import assertk.assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import kotlin.reflect.KClass

fun <T : Throwable> Assert<T>.hasMessage(message: String?) {
    assert(actual.message, "message").isNotNull {
        it.isEqualTo(message)
    }
}

fun <T : Throwable> Assert<T>.hasCause(cause: Throwable) {
    assert(actual.cause, "cause").isNotNull {
        it.isEqualTo(cause)
    }
}

fun <T : Throwable> Assert<T>.hasNoCause() {
    if (actual.cause == null) return
    expected("[cause] to not exist but was:${show(actual.cause)}")
}

fun <T : Throwable> Assert<T>.hasMessageStartingWith(prefix: String) {
    assert(actual.message, "message").isNotNull {
        it.startsWith(prefix)
    }
}

fun <T : Throwable> Assert<T>.hasMessageContaining(string: String) {
    assert(actual.message, "message").isNotNull {
        it.contains(string)
    }
}

fun <T : Throwable> Assert<T>.hasMessageMatching(regex: Regex) {
    assert(actual.message, "message").isNotNull {
        it.matches(regex)
    }
}

fun <T : Throwable> Assert<T>.hasMessageEndingWith(suffix: String) {
    assert(actual.message, "message").isNotNull {
        it.endsWith(suffix)
    }
}

fun <T : Throwable> Assert<T>.hasCauseInstanceOf(kclass: KClass<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.isInstanceOf(kclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseInstanceOf(jclass: Class<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.isInstanceOf(jclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseWithClass(kclass: KClass<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.hasClass(kclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseWithClass(jclass: Class<out T>) {
    assert(actual.cause, "cause").isNotNull {
        it.hasClass(jclass)
    }
}

fun <T : Throwable> Assert<T>.hasRootCause(cause: Throwable) {
    assert(actual.rootCause(), "root cause").isEqualTo(cause)

}

fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(kclass: KClass<out T>) {
    assert(actual.rootCause(), "root cause").isInstanceOf(kclass)
}

fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(jclass: Class<out T>) {
    assert(actual.rootCause(), "root cause").isInstanceOf(jclass)
}

fun <T : Throwable> Assert<T>.hasRootCauseWithClass(kclass: KClass<out T>) {
    assert(actual.rootCause(), "root cause").hasClass(kclass)
}

fun <T : Throwable> Assert<T>.hasRootCauseWithClass(jclass: Class<out T>) {
    assert(actual.rootCause(), "root cause").hasClass(jclass)
}

private fun Throwable.rootCause(): Throwable =
        this.cause?.rootCause() ?: this

fun <T : Throwable> Assert<T>.hasStackTraceContaining(description: String) {
    assert(actual.stackTrace.map { it.toString() }, "stack trace").contains(description)
}

