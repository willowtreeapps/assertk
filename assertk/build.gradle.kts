import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.TEST_COMPILATION_NAME
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.common

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

        val coroutinesTest by creating {
            dependsOn(commonTest.get())
            dependencies {
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
        targets.configureEach {
            if (platformType != common && name != "wasmWasi") {
                compilations.getByName(TEST_COMPILATION_NAME)
                    .defaultSourceSet
                    .dependsOn(coroutinesTest)
            }
        }
    }
}
