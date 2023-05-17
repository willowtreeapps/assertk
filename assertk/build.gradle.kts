plugins {
    id("assertk.multiplatform")
    id("assertk.publish")
}

val compileTemplates by tasks.registering(TemplateTask::class) {
    inputDir.set(file("src/template"))
    outputDir.set(file("$buildDir/generated/template"))
}

val compileTestTemplates by tasks.registering(TemplateTask::class) {
    inputDir.set(file("src/testTemplate"))
    outputDir.set(file("$buildDir/generated/testTemplate"))
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
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.coroutines.test)
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
    }
}