package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import java.io.File
import java.nio.charset.Charset

/**
 * Returns an assert on the file's name.
 */
fun Assert<File>.name() = prop("name", File::getName)

/**
 * Returns an assert on the file's path.
 */
fun Assert<File>.path() = prop("path", File::getPath)

/**
 * Returns an assert on the file's parent.
 */
fun Assert<File>.parent() = prop("parent", File::getParent)

/**
 * Returns an assert on the file's extension.
 */
fun Assert<File>.extension() = prop("extension", File::extension)

/**
 * Returns an assert on the file's contents as text.
 */
fun Assert<File>.text(charset: Charset = Charsets.UTF_8) = prop("text", { it.readText(charset) })

/**
 * Returns an assert on the file's contents as bytes.
 */
fun Assert<File>.bytes() = prop("bytes", File::readBytes)

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
    name().isEqualTo(expected)
}

/**
 * Asserts the file has the expected path.
 */
fun Assert<File>.hasPath(expected: String) {
    path().isEqualTo(expected)
}

/**
 * Asserts the file has the expected parent path.
 */
fun Assert<File>.hasParent(expected: String) {
    parent().isEqualTo(expected)
}

/**
 * Asserts the file has the expected extension.
 */
fun Assert<File>.hasExtension(expected: String) {
    extension().isEqualTo(expected)
}

/**
 * Asserts the file contains exactly the expected text (and nothing else).
 * @param charset The character set of the file, default is [Charsets.UTF_8]
 * @see [hasBytes]
 */
fun Assert<File>.hasText(expected: String, charset: Charset = Charsets.UTF_8) {
    text(charset).isEqualTo(expected)
}

/**
 * Asserts the file has the expected direct child.
 */
fun Assert<File>.hasDirectChild(expected: File) {
    if (actual.listFiles()?.contains(expected) == true) return
    expected("to have direct child ${show(expected)}")
}
