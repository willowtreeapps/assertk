import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    id("org.jetbrains.dokka")
    alias(libs.plugins.nexus.publish)
}

repositories {
    mavenCentral()
}

group = "com.willowtreeapps.assertk"
version = libs.versions.assertk.get()

nexusPublishing {
    repositories {
        sonatype()
    }
}

val dokkaHtmlMultiModule by tasks.getting(DokkaMultiModuleTask::class)

val generateDocs by tasks.creating(Copy::class) {
    from(dokkaHtmlMultiModule.outputDirectory)
    into("docs")
}

plugins.withType<NodeJsRootPlugin> {
    extensions.getByType<NodeJsRootExtension>().apply {
        nodeVersion = "20.0.0"
    }
}
