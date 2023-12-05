package assertk

/**
 * Aborts the test instead of failing it, this gives you a way to skip tests based on a dynamic assertion.
 *
 * ```
 * // only run test on windows
 * assume {
 *     assertThat(System.getProperty("os.name")).startsWith("Windows")
 * }
 * ```
 */
inline fun assume(f: () -> Unit) {
    AssumptionFailure.run { f() }
}