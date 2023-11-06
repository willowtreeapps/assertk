package assertk.android.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import org.junit.Test

class GoogleTruthDetectorTest : LintDetectorTest() {
    override fun getDetector() = GoogleTruthDetector()
    override fun getIssues() = listOf(
        GoogleTruthDetector.ISSUE.setEnabledByDefault(true),
    )

    @Test
    fun `test clean`() {
        val code = """
            package error

            import java.io.File

            class TestingTesting {
                fun testingTest() {
                    val first = File()
                    val second = File()
                }
            }
        """.trimIndent()

        lint().files(kotlin(code)).run().expectClean()
    }

    @Test
    fun `test google truth assertion not reported in java source`() {
        val code = """
            package error;

            import java.io.File;
            import com.google.common.truth.Truth.assertThat;

            class TestingTesting {
                fun testingTest() {
                    val first = File();
                    val second = File();
                    assertThat(first).isEqualTo(second);
                }
            }
        """.trimIndent()

        lint().files(
            java(code),
            java(GOOGLE_TRUTH_STUB),
            java(ASSERTION_STUB),
        ).run().expectClean()
    }

    @Test
    fun `test google truth assertion detected`() {
        val code = """
            package error

            import java.io.File
            import com.google.common.truth.Truth.assertThat

            class TestingTesting {
                fun testingTest() {
                    val first = File()
                    val second = File()
                    assertThat(first).isEqualTo(second)
                }
            }
        """.trimIndent()

        lint().files(
            kotlin(code),
            java(GOOGLE_TRUTH_STUB),
            java(ASSERTION_STUB),
        ).run().expect("""src/error/TestingTesting.kt:10: Warning: Use asserk assertions [GoogleTruthUse]
        assertThat(first).isEqualTo(second)
        ~~~~~~~~~~~~~~~~~
0 errors, 1 warnings""")
    }

    companion object {
        private const val GOOGLE_TRUTH_STUB = """
            package com.google.common.truth;

            public class Truth {
                public static Assertion assertThat(Object actual) {
                    return new Assertion();
                }
            }
        """

        private const val ASSERTION_STUB = """
            package com.google.common.truth; 

            public class Assertion {
                public void isEqualTo(Object expected) {
                }
            }
        """
    }
}