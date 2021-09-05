plugins {
    id("assertk.multiplatform")
    id("assertk.publish")
}

kotlin {

    targets.all {
        compilations.findByName("test")?.kotlinOptions {
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":assertk"))
                implementation(libs.kotlin.coroutines)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        jsTest {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}