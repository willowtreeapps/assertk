import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.dokka.gradle)
    implementation(libs.detekt.gradle)
    implementation(libs.kotlin.gradle)
    // hack to access version catalogue https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

tasks.withType<KotlinJvmCompile> {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}
