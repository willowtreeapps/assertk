import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.maven
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
    kotlin("multiplatform")
    id("io.gitlab.arturbosch.detekt")
}

val libs = the<LibrariesForLibs>()

repositories {
    mavenCentral()
}

// special SNAPSHOT support
if (libs.versions.assertk.get().endsWith("SNAPSHOT")) {
    // only add SNAPSHOT repository on SNAPSHOT versions
    repositories {
        maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
    }
    // Don't cache SNAPSHOT deps
    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}

val nativeTargets = arrayOf(
    "linuxX64", "linuxArm64",
    "macosX64", "macosArm64",
    "mingwX64",
    "iosArm32", "iosArm64", "iosX64", "iosSimulatorArm64",
    "tvosArm64", "tvosX64", "tvosSimulatorArm64",
    "watchosArm32", "watchosArm64", "watchosX86", "watchosX64", "watchosSimulatorArm64", "watchosDeviceArm64",
    "androidNativeArm32", "androidNativeArm64", "androidNativeX86", "androidNativeX64",
)

kotlin {
    jvm()
    js(IR) {
        nodejs()
        // suppress noisy 'Reflection is not supported in JavaScript target'
        for (compilation in arrayOf("main", "test")) {
            compilations.getByName(compilation).kotlinOptions {
                suppressWarnings = true
            }
        }
    }

    for (target in nativeTargets) {
        targets.add(presets.getByName(target).createTarget(target))
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val nativeMain = create("nativeMain") {
            dependsOn(commonMain)
        }
        val nativeTest = create("nativeTest") {
            dependsOn(commonTest)
        }
        for (sourceSet in nativeTargets) {
            getByName("${sourceSet}Main") {
                dependsOn(nativeMain)
            }
            getByName("${sourceSet}Test") {
                dependsOn(nativeTest)
            }
        }
    }
}


// Detekt setup
val detektMain by tasks.registering(Detekt::class) {
    setSource(files(kotlin.sourceSets
        .filter { it.name.endsWith("Main") }
        .map { it.kotlin }))
    include("**/*.kt")
    config.from(files(project.rootDir.resolve("detekt.yml")))
    buildUponDefaultConfig = true
}

val detektTest by tasks.registering(Detekt::class) {
    setSource(files(kotlin.sourceSets
        .filter { it.name.endsWith("Test") }
        .map { it.kotlin }))
    include("**/*.kt")
    config.from(files(project.rootDir.resolve("detekt-test.yml")))
    buildUponDefaultConfig = true
}

val detekt by tasks.getting {
    dependsOn(detektMain, detektTest)
}
