pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "assertk-project"

//includeBuild("build-src")
include(
    ":assertk",
    ":assertk-coroutines",
)