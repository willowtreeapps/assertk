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

class Junit4Detector : Detector(), Detector.UastScanner {
    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(
        UCallExpression::class.java,
    )

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                if (context.evaluator.isMemberInClass(node.resolve(), "org.junit.Assert")) {
                    context.report(
                        ISSUE, node, context.getLocation(node),
                        "Use asserk assertions"
                    )
                }
            }
        }
    }

    companion object {
        /**
         * Issue describing the problem and pointing to the detector
         * implementation.
         */
        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "SampleId",
            briefDescription = "Lint Mentions",

            explanation = """
                    This check highlights string literals in code which mentions the word `lint`. \
                    Blah blah blah.

                    Another paragraph here.
                    """,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                Junit4Detector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}