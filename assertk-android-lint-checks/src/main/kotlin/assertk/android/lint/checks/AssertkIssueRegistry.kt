package assertk.android.lint.checks

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API

/*
 * The list of issues that will be checked when running <code>lint</code>.
 */
class AssertkIssueRegistry : IssueRegistry() {
    override val issues = listOf(
        TestFrameworkAssertionDetector.ISSUE,
        AssertJDetector.ISSUE,
        GoogleTruthDetector.ISSUE,
    )

    override val api: Int
        get() = CURRENT_API

    override val minApi: Int
        get() = 8 // works with Studio 4.1 or later; see com.android.tools.lint.detector.api.Api / ApiKt

    // Requires lint API 30.0+; if you're still building for something
    // older, just remove this property.
    override val vendor: Vendor = Vendor(
        vendorName = "asserk",
        feedbackUrl = "https://github.com/willowtreeapps/assertk/issues",
        contact = "https://github.com/willowtreeapps/assertk",
    )
}
