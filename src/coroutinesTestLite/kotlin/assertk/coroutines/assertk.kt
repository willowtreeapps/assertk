package assertk.coroutines

import assertk.AssertBlock
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@ExperimentalCoroutinesApi
@UseExperimental(InternalCoroutinesApi::class)
actual fun <T> assertThat(f: suspend () -> T): AssertBlock<T> {
    val dispatcher = TestCoroutineDispatcher()
    val job = SupervisorJob()
    val context = EmptyCoroutineContext + dispatcher + job
    val startingJobs = context.activeJobs()
    val scope = CoroutineScope(context)
    val deferred = scope.async {
        f()
    }

    dispatcher.advanceUntilIdle()

    val result: AssertBlock<T>

    val error = deferred.getCompletionExceptionOrNull()

    if (error != null) {
        result = AssertBlock.Error(error)
    } else {
        result = AssertBlock.Value(deferred.getCompleted())
    }

    val endingJobs = context.activeJobs()
    if ((endingJobs - startingJobs).isNotEmpty()) {
        throw AssertionError("Test finished with active jobs: $endingJobs")
    }

    return result
}

private fun CoroutineContext.activeJobs(): Set<Job> {
    return checkNotNull(this[Job]).children.filter { it.isActive }.toSet()
}

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
private class TestCoroutineDispatcher : CoroutineDispatcher(), Delay {

    private val queue = mutableListOf<Runnable>()

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        block.run()
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        queue.add(CancellableContinuationRunnable(continuation) { resumeUndispatched(Unit) })
    }

    override fun invokeOnTimeout(timeMillis: Long, block: Runnable): DisposableHandle {
        queue.add(block)
        return object : DisposableHandle {
            override fun dispose() {
                queue.remove(block)
            }
        }
    }

    fun advanceUntilIdle() {
        for (block in queue) {
            block.run()
        }
        queue.clear()
    }
}

/**
 * This class exists to allow cleanup code to avoid throwing for cancelled continuations scheduled
 * in the future.
 */
private class CancellableContinuationRunnable<T>(
    val continuation: CancellableContinuation<T>,
    private val block: CancellableContinuation<T>.() -> Unit
) : Runnable {
    override fun run() = continuation.block()
}
