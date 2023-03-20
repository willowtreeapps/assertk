pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

rootProject.name = "assertk-project"

//includeBuild("build-src")
include(
    ":assertk",
    ":assertk-coroutines",
)