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