package assertk

import com.willowtreeapps.opentest4k.AssertionFailedError
import com.willowtreeapps.opentest4k.MultipleFailuresError

/**
 * Assertions are run in a failure context which captures failures to report them.
 */
internal object FailureContext {
    private val failureRef = ThreadLocalRef<Failure>().apply {
        value = SimpleFailure()
    }

    /**
     * Run the given block of assertions in the given context. If we are already in a context a new one will not be
     * created.
     */
    fun <T> run(failure: Failure, f: () -> T): T {
        val currentFailure = failureRef.value
        if (currentFailure is SimpleFailure) {
            failureRef.value = failure
            try {
                return f()
            } finally {
                failureRef.value = currentFailure
                failure()
            }
        } else {
            return f()
        }
    }

    fun fail(error: AssertionError) {
        failureRef.value!!.fail(error)
    }
}

internal interface Failure {
    /**
     * Record a failure. Depending on the implementation this may throw an exception or collect the failure for later.
     */
    fun fail(error: AssertionError)

    /**
     * Triggers any collected failures.
     */
    operator fun invoke() {
    }
}

/**
 * Failure that immediately thrown an exception.
 */
internal class SimpleFailure : Failure {
    override fun fail(error: AssertionError) {
        failWithNotInStacktrace(error)
    }
}

/**
 * Failure that collects all failures and displays them at once.
 */
internal class SoftFailure(
    val message: String = defaultMessage,
    val failIf: (List<AssertionError>) -> Boolean = { it.isNotEmpty() }
) :
    Failure {
    private val failures: MutableList<AssertionError> = ArrayList()

    override fun fail(error: AssertionError) {
        failures.add(error)
    }

    override fun invoke() {
        if (failIf(failures)) {
            FailureContext.fail(compositeErrorMessage(failures))
        }
    }

    private fun compositeErrorMessage(errors: List<AssertionError>): AssertionError {
        return if (errors.size == 1) {
            errors.first()
        } else {
            MultipleFailuresError(message, errors)
        }
    }

    companion object {
        const val defaultMessage = "The following assertions failed"
    }
}

/**
 * Fail the test with the given {@link AssertionError}.
 */
fun fail(error: AssertionError): Nothing {
    throw error
}

internal val NONE: Any = Any()

/**
 * Fail the test with the given message.
 */
fun fail(message: String, expected: Any? = NONE, actual: Any? = NONE): Nothing {
    if (expected === NONE && actual === NONE) {
        throw AssertionFailedError(message)
    } else {
        throw AssertionFailedError(
            message,
            if (expected === NONE) null else expected,
            if (actual === NONE) null else actual
        )
    }
}

fun notifyFailure(e: Throwable) {
    FailureContext.fail(if (e is AssertionError) e else AssertionError(e))
}

internal expect inline fun failWithNotInStacktrace(error: AssertionError): Nothing

/*
 * Copyright (C) 2018 Touchlab, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
expect open class ThreadLocalRef<T>() {
    fun get(): T?
    fun set(value: T?)
}

var <T> ThreadLocalRef<T>.value: T?
    get() = get()
    set(value) {
        set(value)
    }
