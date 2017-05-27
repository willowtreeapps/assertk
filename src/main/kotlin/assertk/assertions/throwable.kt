package assertk.assertions

import assertk.Assert
import assertk.assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import kotlin.reflect.KClass

fun <T : Throwable> Assert<T>.hasMessage(message: String?) {
    assert("message", actual.message).isNotNull {
        it.isEqualTo(message)
    }
}

fun <T : Throwable> Assert<T>.hasCause(cause: Throwable) {
    assert("cause", actual.cause).isNotNull {
        it.isEqualTo(cause)
    }
}

fun <T : Throwable> Assert<T>.hasNoCause() {
    if (actual.cause == null) return
    expected("[cause] to not exist but was:${show(actual.cause)}")
}

fun <T : Throwable> Assert<T>.hasMessageStartingWith(prefix: String) {
    assert("message", actual.message).isNotNull {
        it.startsWith(prefix)
    }
}

fun <T : Throwable> Assert<T>.hasMessageContaining(string: String) {
    assert("message", actual.message).isNotNull {
        it.contains(string)
    }
}

fun <T : Throwable> Assert<T>.hasMessageMatching(regex: Regex) {
    assert("message", actual.message).isNotNull {
        it.matches(regex)
    }
}

fun <T : Throwable> Assert<T>.hasMessageEndingWith(suffix: String) {
    assert("message", actual.message).isNotNull {
        it.endsWith(suffix)
    }
}

fun <T : Throwable> Assert<T>.hasCauseInstanceOf(kclass: KClass<out T>) {
    assert("cause", actual.cause).isNotNull {
        it.isInstanceOf(kclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseInstanceOf(jclass: Class<out T>) {
    assert("cause", actual.cause).isNotNull {
        it.isInstanceOf(jclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseWithClass(kclass: KClass<out T>) {
    assert("cause", actual.cause).isNotNull {
        it.hasClass(kclass)
    }
}

fun <T : Throwable> Assert<T>.hasCauseWithClass(jclass: Class<out T>) {
    assert("cause", actual.cause).isNotNull {
        it.hasClass(jclass)
    }
}

fun <T : Throwable> Assert<T>.hasRootCause(cause: Throwable) {
    assert("root cause", actual.rootCause()).isEqualTo(cause)

}

fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(kclass: KClass<out T>) {
    assert("root cause", actual.rootCause()).isInstanceOf(kclass)
}

fun <T : Throwable> Assert<T>.hasRootCauseInstanceOf(jclass: Class<out T>) {
    assert("root cause", actual.rootCause()).isInstanceOf(jclass)
}

fun <T : Throwable> Assert<T>.hasRootCauseWithClass(kclass: KClass<out T>) {
    assert("root cause", actual.rootCause()).hasClass(kclass)
}

fun <T : Throwable> Assert<T>.hasRootCauseWithClass(jclass: Class<out T>) {
    assert("root cause", actual.rootCause()).hasClass(jclass)
}

private fun Throwable.rootCause(): Throwable =
        this.cause?.rootCause() ?: this

fun <T : Throwable> Assert<T>.hasStackTraceContaining(description: String) {
    assert("stack trace", actual.stackTrace.map { it.toString() }).contains(description)
}

