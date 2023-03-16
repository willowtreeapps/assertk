plugins {
    id("assertk.multiplatform")
    id("assertk.publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":assertk"))
                implementation(libs.kotlin.coroutines)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}