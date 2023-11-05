package assertk.android.lint.checks

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

class GoogleTruthDetector : Detector(), Detector.UastScanner {
    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(
        UCallExpression::class.java,
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        override fun visitCallExpression(node: UCallExpression) {
            // Avoid enforcing assertk use in java
            // sources for mixed language codebases
            if (isJava(node.javaPsi)) return

            val psiMethod = node.resolve()

            if (context.evaluator.isMemberInClass(psiMethod, "com.google.common.truth.Truth")) {
                context.report(
                    ISSUE,
                    node,
                    context.getLocation(node),
                    "Use asserk assertions"
                )
            }
        }
    }

    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "GoogleTruthUse",
            briefDescription = "Google Truth assertions are called",
            explanation = """
                    Google Truth assertions should not be used in Kotlin tests. Use assertk instead.
                    """,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            enabledByDefault = false,
            implementation = Implementation(
                GoogleTruthDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
