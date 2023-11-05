package assertk.android.lint.checks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement

class TestFrameworkAssertionDetector : Detector(), Detector.UastScanner {
    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(
        UCallExpression::class.java,
    )

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                val psiMethod = node.resolve()
                val isJunit4Assertion = context.evaluator.isMemberInClass(psiMethod, "org.junit.Assert")
                val isJunit5Assertion = context.evaluator.isMemberInClass(psiMethod, "org.junit.jupiter.api.Assertions")
                val isKotlinTestAssertion = context.evaluator.isMemberInClass(psiMethod, "kotlin.test.AssertionsKt")

                if (isJunit4Assertion || isJunit5Assertion || isKotlinTestAssertion) {
                    context.report(ISSUE, node, context.getLocation(node), "Use asserk assertions")
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