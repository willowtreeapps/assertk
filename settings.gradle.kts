pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "assertk-project"

//includeBuild("build-src")
include(
    ":assertk",
    ":assertk-android-lint",
    ":assertk-coroutines",
)
