package test.assertk

/**
 * A quick shim for common, coroutine-based tests since the coroutines library
 * does not yet support WASM target.
 */
expect fun runTest(block: suspend () -> Unit)
