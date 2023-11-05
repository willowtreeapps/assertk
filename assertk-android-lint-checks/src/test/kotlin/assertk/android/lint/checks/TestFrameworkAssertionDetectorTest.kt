package assertk.android.lint.checks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import org.junit.Test

class TestFrameworkAssertionDetectorTest : LintDetectorTest() {
    override fun getDetector() = TestFrameworkAssertionDetector()
    override fun getIssues() = listOf(
        TestFrameworkAssertionDetector.ISSUE,
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
    fun `test junit 4 assertion not reported for java source`() {
        val code = """
            package error;

            import java.io.File;
            import org.junit.Assert.assertEquals;

            class TestingTesting {
                fun testingTest() {
                    val first = File();
                    val second = File();
                    assertEquals(first, second);
                }
            }
        """.trimIndent()

        lint().files(
            java(code),
            java(JUNIT_4_ASSERT_STUB),
        ).run().expectClean()
    }

    @Test
    fun `test junit 4 assertion detected`() {
        val code = """
            package error

            import java.io.File
            import org.junit.Assert.assertEquals

            class TestingTesting {
                fun testingTest() {
                    val first = File()
                    val second = File()
                    assertEquals(first, second)
                }
            }
        """.trimIndent()

        lint().files(
            kotlin(code),
            java(JUNIT_4_ASSERT_STUB),
        ).run().expect("""src/error/TestingTesting.kt:10: Warning: Use asserk assertions [TestFrameworkAssertionUse]
        assertEquals(first, second)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~~
0 errors, 1 warnings""")
    }

    @Test
    fun `test junit 5 assertion detected`() {
        val code = """
            package error

            import java.io.File
            import org.junit.jupiter.api.Assertions.assertEquals

            class TestingTesting {
                fun testingTest() {
                    val first = File()
                    val second = File()
                    assertEquals(first, second)
                }
            }
        """.trimIndent()

        lint().files(
            kotlin(code),
            java(JUNIT_5_ASSERT_STUB),
        ).run().expect("""src/error/TestingTesting.kt:10: Warning: Use asserk assertions [TestFrameworkAssertionUse]
        assertEquals(first, second)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~~
0 errors, 1 warnings""")
    }

    @Test
    fun `test kotlin assertion detected`() {
        val code = """
            package error

            import java.io.File
            import kotlin.test.assertEquals

            class TestingTesting {
                fun testingTest() {
                    val first = File()
                    val second = File()
                    assertEquals(first, second)
                }
            }
        """.trimIndent()

        lint().files(
            kotlin(code),
            kotlin(KOTLIN_TEST_ASSERT_STUB),
        ).run().expect("""src/error/TestingTesting.kt:10: Warning: Use asserk assertions [TestFrameworkAssertionUse]
        assertEquals(first, second)
        ~~~~~~~~~~~~~~~~~~~~~~~~~~~
0 errors, 1 warnings""")
    }

    companion object {
        private const val JUNIT_4_ASSERT_STUB = """
            package org.junit;
            
            public class Assert {
                public static void assertEquals(Object expected, Object actual) {
                }
            }
        """

        private const val JUNIT_5_ASSERT_STUB = """
            package org.junit.jupiter.api;
            
            public class Assertions {
                public static void assertEquals(Object expected, Object actual) {
                }
            }
        """

        private const val KOTLIN_TEST_ASSERT_STUB = """
            @file:kotlin.jvm.JvmName("AssertionsKt")

            package kotlin.test
            
            fun <T> assertEquals(expected: T, actual: T) {
            }
        """
    }
}