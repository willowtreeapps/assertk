@file:JvmName("FailureJVMKt")
@file:Suppress("NOTHING_TO_INLINE")

package assertk

import com.willowtreeapps.opentest4k.AssertionFailedError
import com.willowtreeapps.opentest4k.TestAbortedException

internal actual inline fun failWithNotInStacktrace(error: Throwable): Nothing {
    val filtered = error.stackTrace
        .dropWhile { it.className.startsWith("assertk") }
        .toTypedArray()
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UnsafeCast")
    error.stackTrace = filtered
    throw error
}

@PublishedApi
internal actual inline fun Throwable.isFatal(): Boolean =
    // https://github.com/ReactiveX/RxJava/blob/6a44e5d0543a48f1c378dc833a155f3f71333bc2/src/main/java/io/reactivex/exceptions/Exceptions.java#L66
    this is VirtualMachineError || this is ThreadDeath || this is LinkageError

internal object AssumptionFailure : Failure {
    override fun fail(error: Throwable) {
        failWithNotInStacktrace(
            TestAbortedException(
                buildString {
                    append("Assumption failed")
                    error.message?.let { message ->
                        append(": ")
                        append(message)
                    }
                },
                // unwrap assertion errors
                if (error is AssertionFailedError) error.cause else error
            )
        )
    }
}