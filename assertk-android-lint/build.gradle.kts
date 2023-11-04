plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.lint.library"
    compileSdk = 34

    defaultConfig {
        minSdk = 19
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint {
        checkDependencies = true
    }
}

dependencies {
    implementation(project(":assertk-android-lint-checks"))
    lintPublish(project(":assertk-android-lint-checks"))
}
