import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.common
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.wasm

plugins {
    id("assertk.multiplatform")
    id("assertk.publish")
}

val compileTemplates by tasks.registering(TemplateTask::class) {
    inputDir.set(file("src/template"))
    outputDir.set(layout.buildDirectory.dir("generated/template"))
}

val compileTestTemplates by tasks.registering(TemplateTask::class) {
    inputDir.set(file("src/testTemplate"))
    outputDir.set(layout.buildDirectory.dir("generated/testTemplate"))
}

kotlin {
    jvm {
        withJava()
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        group("coroutines") {
            withNative()
            withJs()
            withJvm()
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.opentest4k)
            }
            kotlin.srcDir(compileTemplates)
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
            kotlin.srcDir(compileTestTemplates)
        }
        jvmMain {
            dependencies {
                compileOnly(kotlin("reflect"))
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("reflect"))
            }
        }

        val coroutinesTest by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}