package test.assertk

actual fun runTest(block: suspend () -> Unit) {
    kotlinx.coroutines.test.runTest { block() }
}
