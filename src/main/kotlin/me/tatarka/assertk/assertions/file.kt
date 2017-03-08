import me.tatarka.assertk.Assert
import me.tatarka.assertk.assertions.contains
import me.tatarka.assertk.assertions.isEqualTo
import me.tatarka.assertk.assertions.matches
import java.io.File
import java.nio.charset.Charset

// File
fun Assert<File>.exists() {
    if (actual.exists()) return
    expected("to exist")
}

fun Assert<File>.isDirectory() {
    if (actual.isDirectory) return
    expected("to be a directory")
}

fun Assert<File>.isFile() {
    if (actual.isFile) return
    expected("to be a file")
}

fun Assert<File>.isHidden() {
    if (actual.isHidden) return
    expected("to be hidden")
}

fun Assert<File>.isNotHidden() {
    if (!actual.isHidden) return
    expected("to not be hidden")
}

fun Assert<File>.hasName(expected: String) {
    assert(actual.name).named("name").isEqualTo(expected)
}

fun Assert<File>.hasPath(expected: String) {
    assert(actual.path).named("path").isEqualTo(expected)
}

fun Assert<File>.hasParent(expected: String) {
    assert(actual.parent).named("parent").isEqualTo(expected)
}

fun Assert<File>.hasExtension(expected: String) {
    assert(actual.extension).named("extension").isEqualTo(expected)
}

fun Assert<File>.hasText(expected: String, charset: Charset = Charsets.UTF_8) {
    val text = actual.readText(charset)
    assert(text).named("text").isEqualTo(expected)
}

fun Assert<File>.containsText(expected: String, charset: Charset = Charsets.UTF_8) {
    val text = actual.readText(charset)
    assert(text).named("text").contains(expected)
}

fun Assert<File>.matchesText(expected: Regex, charset: Charset = Charsets.UTF_8) {
    val text = actual.readText(charset)
    assert(text).named("text").matches(expected)
}

fun Assert<File>.hasDirectChild(expected: File) {
    if (actual.listFiles()?.contains(expected) ?: false) return
    expected("to have direct child ${show(expected)}")
}
