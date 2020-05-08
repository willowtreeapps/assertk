package assertk.coroutines

import assertk.Assert
import assertk.Failure
import assertk.SoftFailure
/**
 * All assertions in the given lambda are run.
 *
 * ```
 * assertThat("test", name = "test").all {
 *   startsWith("t")
 *   endsWith("t")
 * }
 * ```
 * @param body The body to execute.
 */
suspend fun <T> Assert<T>.all(body: suspend Assert<T>.() -> Unit) {
    SoftFailure().run {
        body()
    }
}

suspend fun  <T> Failure.run(f: suspend () -> T): T {
    pushFailure()
    try {
        return f()
    } finally {
        popFailure()
        invoke()
    } }
