plugins {
    id("org.jetbrains.dokka")
    alias(libs.plugins.git.publish)
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

val dokkaHtmlMultiModule by tasks.getting

gitPublish {
    repoUri.set("git@github.com:willowtreeapps/assertk.git")
    branch.set("gh-pages")
    contents {
        from(dokkaHtmlMultiModule)
        into("javadoc")
    }
}