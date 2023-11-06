package assertk.android.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import org.junit.Test

class AssertJDetectorTest : LintDetectorTest() {
    override fun getDetector() = AssertJDetector()
    override fun getIssues() = listOf(
        AssertJDetector.ISSUE.setEnabledByDefault(true),
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
    fun `test assertj assertion not reported in java source`() {
        val code = """
            package error;

            import java.io.File;
            import org.assertj.core.api.Assertions.assertThat;

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
            java(ASSERTJ_STUB),
            java(ASSERTION_STUB),
        ).run().expectClean()
    }

    @Test
    fun `test assertj assertion detected`() {
        val code = """
            package error

            import java.io.File
            import org.assertj.core.api.Assertions.assertThat

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
            java(ASSERTJ_STUB),
            java(ASSERTION_STUB),
        ).run().expect("""src/error/TestingTesting.kt:10: Warning: Use asserk assertions [AssertJUse]
        assertThat(first).isEqualTo(second)
        ~~~~~~~~~~~~~~~~~
0 errors, 1 warnings""")
    }

     companion object {
         private const val ASSERTJ_STUB = """
            package org.assertj.core.api;

            public class Assertions {
                public static Assertion assertThat(Object actual) {
                    return new Assertion();
                }
            }
        """

         private const val ASSERTION_STUB = """
            package org.assertj.core.api; 

            public class Assertion {
                public void isEqualTo(Object expected) {
                }
            }
        """
    }
}