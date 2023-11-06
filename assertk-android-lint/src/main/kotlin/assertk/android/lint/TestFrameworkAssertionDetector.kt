package assertk.android.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.isJava
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement

class TestFrameworkAssertionDetector : Detector(), Detector.UastScanner {
    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(
        UCallExpression::class.java,
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        private val frameworkAssertionClasses = listOf(
            "org.junit.Assert", // junit 4
            "org.junit.jupiter.api.Assertions", // junit 5
            "kotlin.test.AssertionsKt", // kotlin.test
        )

        override fun visitCallExpression(node: UCallExpression) {
            // Avoid enforcing assertk use in java
            // sources for mixed language codebases
            if (isJava(node.javaPsi)) return

            val psiMethod = node.resolve()

            for (assertionClass in frameworkAssertionClasses) {
                if (context.evaluator.isMemberInClass(psiMethod, assertionClass)) {
                    context.report(
                        ISSUE,
                        node,
                        context.getLocation(node),
                        "Use asserk assertions"
                    )

                    return
                }
            }
        }
    }

    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "TestFrameworkAssertionUse",
            briefDescription = "Test framework assertion is called",

            explanation = """
                    Test frameworks like junit and kotlin test ship with built-in test assertions. However, these assertion mechanisms shouldn't be used if fluent assertion libraries are on the classpath.
                    """,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                TestFrameworkAssertionDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}