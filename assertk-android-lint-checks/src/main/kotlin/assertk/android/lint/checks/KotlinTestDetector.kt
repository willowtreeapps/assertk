package assertk.android.lint.checks

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity

class KotlinTestDetector : Detector(), Detector.UastScanner {

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
                KotlinTestDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}