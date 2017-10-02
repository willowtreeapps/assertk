package assertk.assertions

import assertk.Assert
import assertk.assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import java.io.File
import java.nio.charset.Charset

/**
 * Asserts the file exists.
 */
fun Assert<File>.exists() {
    if (actual.exists()) return
    expected("to exist")
}

/**
 * Asserts the file is a directory.
 * @see [isFile]
 */
fun Assert<File>.isDirectory() {
    if (actual.isDirectory) return
    expected("to be a directory")
}

/**
 * Asserts the file is a simple file (not a directory).
 * @see [isDirectory]
 */
fun Assert<File>.isFile() {
    if (actual.isFile) return
    expected("to be a file")
}

/**
 * Asserts the file is hidden.
 * @see [isNotHidden]
 */
fun Assert<File>.isHidden() {
    if (actual.isHidden) return
    expected("to be hidden")
}

/**
 * Asserts the file is not hidden.
 * @see [isHidden]
 */
fun Assert<File>.isNotHidden() {
    if (!actual.isHidden) return
    expected("to not be hidden")
}

/**
 * Asserts the file has the expected name.
 */
fun Assert<File>.hasName(expected: String) {
    assert(actual.name, "name").isEqualTo(expected)
}

/**
 * Asserts the file has the expected path.
 */
fun Assert<File>.hasPath(expected: String) {
    assert(actual.path, "path").isEqualTo(expected)
}

/**
 * Asserts the file has the expected parent path.
 */
fun Assert<File>.hasParent(expected: String) {
    assert(actual.parent, "parent").isEqualTo(expected)
}

/**
 * Asserts the file has the expected extension.
 */
fun Assert<File>.hasExtension(expected: String) {
    assert(actual.extension, "extension").isEqualTo(expected)
}

/**
 * Asserts the file contains exactly the expected text (and nothing else).
 * @param charset The character set of the file, default is [Charsets.UTF_8]
 * @see [containsText]
 * @see [matchesText]
 */
fun Assert<File>.hasText(expected: String, charset: Charset = Charsets.UTF_8) {
    val text = actual.readText(charset)
    assert(text, "text").isEqualTo(expected)
}

/**
 * Asserts the file contains the expected text, it may contain other things.
 * @param charset The character set of the file, default is [Charsets.UTF_8]
 * @see [hasText]
 * @see [matchesText]
 */
fun Assert<File>.containsText(expected: String, charset: Charset = Charsets.UTF_8) {
    val text = actual.readText(charset)
    assert(text, "text").contains(expected)
}

/**
 * Asserts the file's text matches the expected regular expression.
 * @param charset The character set of the file, default is [Charsets.UTF_8]
 * @see [hasText]
 * @see [matchesText]
 */
fun Assert<File>.matchesText(expected: Regex, charset: Charset = Charsets.UTF_8) {
    val text = actual.readText(charset)
    assert(text, "text").matches(expected)
}

/**
 * Asserts the file has the expected direct child.
 */
fun Assert<File>.hasDirectChild(expected: File) {
    if (actual.listFiles()?.contains(expected) == true) return
    expected("to have direct child ${show(expected)}")
}
