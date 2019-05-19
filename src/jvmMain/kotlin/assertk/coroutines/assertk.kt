package assertk.coroutines

import assertk.AssertBlock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineExceptionHandler
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.UncompletedCoroutinesError
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@ExperimentalCoroutinesApi
actual fun <T> assertThat(f: suspend () -> T): AssertBlock<T> {
    val handler = TestCoroutineExceptionHandler()
    val dispatcher = TestCoroutineDispatcher()
    val job = SupervisorJob()
    val context = EmptyCoroutineContext + dispatcher + handler + job
    val startingJobs = context.activeJobs()
    val scope = TestCoroutineScope(context)
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

    scope.cleanupTestCoroutines()
    val endingJobs = context.activeJobs()
    if ((endingJobs - startingJobs).isNotEmpty()) {
        throw UncompletedCoroutinesError("Test finished with active jobs: $endingJobs")
    }

    return result
}

private fun CoroutineContext.activeJobs(): Set<Job> {
    return checkNotNull(this[Job]).children.filter { it.isActive }.toSet()
}
