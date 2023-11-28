import org.jetbrains.dokka.gradle.DokkaTask
import java.util.Locale

plugins {
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
}

group = rootProject.group
version = rootProject.version

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        configureEach {
            // group js and native docs into buckets
            displayName.set(platform.get().name)
            // Set source links to github
            sourceLink {
                localDirectory.set(file("src"))
                remoteUrl.set(java.net.URL("https://github.com/willowtreeapps/assertk/tree/v${project.version}/${project.name}/src"))
                remoteLineSuffix.set("#L")
            }
        }
    }
}

val dokkaCommon by tasks.registering(DokkaTask::class) {
    outputDirectory.set(layout.buildDirectory.dir("javadoc/common"))
    dokkaSourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting
    }
}
val dokkaJavadocCommonJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(dokkaCommon)
}

publishing {
    publications.all {
        if (this is MavenPublication) {
            artifact(dokkaJavadocCommonJar)

            val siteUrl = "https://github.com/willowtreeapps/assertk"
            val gitUrl = "https://github.com/willowtreeapps/assertk.git"

            pom {
                name.set(project.name)
                description.set("Assertions for Kotlin inspired by assertj")
                url.set(siteUrl)

                scm {
                    url.set(siteUrl)
                    connection.set(gitUrl)
                    developerConnection.set(gitUrl)
                }

                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("evant")
                        name.set("Eva Tatarka")
                    }
                }
            }
        }
    }

    // create task to publish all apple (macos, ios, tvos, watchos) artifacts
    @Suppress("UNUSED_VARIABLE")
    val publishApple by tasks.registering {
        publications.all {
            if (name.contains(Regex("macos|ios|tvos|watchos"))) {
                dependsOn("publish${name.replaceFirstChar(Char::titlecase)}PublicationToSonatypeRepository")
            }
        }
    }
}

signing {
    setRequired {
        findProperty("signing.keyId") != null
    }
    publishing.publications.all { sign(this) }
}

// TODO: remove after https://youtrack.jetbrains.com/issue/KT-46466 is fixed
project.tasks.withType(AbstractPublishToMaven::class.java).configureEach {
    dependsOn(project.tasks.withType(Sign::class.java))
}
