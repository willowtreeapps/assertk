plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "assertk.android.lint"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    lint {
        checkDependencies = true
    }
}

dependencies {
    implementation(project(":assertk-android-lint-checks"))
    lintPublish(project(":assertk-android-lint-checks"))
}
