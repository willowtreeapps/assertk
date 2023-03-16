plugins {
    id("assertk.multiplatform")
    id("assertk.publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":assertk"))
                implementation(libs.kotlinx.coroutines)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}