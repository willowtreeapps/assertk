import io.gitlab.arturbosch.detekt.Detekt
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

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

kotlin {
    jvm()
    js {
        nodejs()
        // suppress noisy 'Reflection is not supported in JavaScript target'
        for (compilation in arrayOf("main", "test")) {
            compilations.getByName(compilation).kotlinOptions {
                suppressWarnings = true
            }
        }
    }
    // TODO Remove conditional once coroutines ships a version with WASI target.
    val hasWasmWasi = project.path != ":assertk-coroutines"
    if (hasWasmWasi) {
        @OptIn(ExperimentalWasmDsl::class)
        wasmWasi {
            nodejs()
        }
    }

    linuxX64()
    linuxArm64()
    macosX64()
    macosArm64()
    mingwX64()
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    tvosArm64()
    tvosX64()
    tvosSimulatorArm64()
    watchosArm32()
    watchosArm64()
    watchosX64()
    watchosSimulatorArm64()
    watchosDeviceArm64()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        nodejs()
    }

    applyDefaultHierarchyTemplate()

    // The above does not yet create a common Wasm source set, so build our own.
    // https://youtrack.jetbrains.com/issue/KT-61988
    sourceSets {
        val wasmMain by creating
        val wasmTest by creating
        named("wasmJsMain") { dependsOn(wasmMain) }
        named("wasmJsTest") { dependsOn(wasmTest) }
        if (hasWasmWasi) {
            named("wasmWasiMain") { dependsOn(wasmMain) }
            named("wasmWasiTest") { dependsOn(wasmTest) }
        }
    }
}

tasks.withType<KotlinJvmCompile> {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

// Run only the native tests
val nativeTest by tasks.registering {
    kotlin.targets.all {
        if (this is KotlinNativeTargetWithTests<*>) {
            dependsOn("${name}Test")
        }
    }
}

// Only build apple targets
val buildApple by tasks.registering {
    kotlin.targets.all {
        if (targetName.contains(Regex("macos|ios|tvos|watchos"))) {
            compilations.forEach {
                dependsOn(it.compileAllTaskName)
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

// Node doesn't publish alpha versions for windows
if(!Os.isFamily(Os.FAMILY_WINDOWS)) {
    rootProject.the<NodeJsRootExtension>().apply {
        nodeVersion = "22.0.0-v8-canary20231127cbafc81f11"
        nodeDownloadBaseUrl = "https://nodejs.org/download/v8-canary"
    }
}

rootProject.tasks.withType<KotlinNpmInstallTask>().configureEach {
    args.add("--ignore-engines")
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
