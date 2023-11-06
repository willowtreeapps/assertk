plugins {
    id("java-library")
    kotlin("jvm")
    alias(libs.plugins.android.lint)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDirs("src/main/kotlin")
        }
        test {
            kotlin.srcDirs("src/test/kotlin")
        }
    }
}

lint {
    htmlReport = true
    textReport = true
    absolutePaths = false
    ignoreTestSources = true
    warningsAsErrors = true
}

dependencies {
    compileOnly(libs.bundles.lint.api)
    testImplementation(libs.bundles.lint.tests)
}
