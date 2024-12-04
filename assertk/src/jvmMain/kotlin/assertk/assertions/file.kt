package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import java.io.File
import java.nio.charset.Charset
import kotlin.io.extension
import kotlin.io.readBytes

/**
 * Returns an assert on the file's name.
 */
fun Assert<File>.name() = having("name", File::getName)

/**
 * Returns an assert on the file's path.
 */
fun Assert<File>.path() = having("path", File::getPath)

/**
 * Returns an assert on the file's parent.
 */
fun Assert<File>.parent() = having("parent", File::getParent)

/**
 * Returns an assert on the file's extension.
 */
fun Assert<File>.extension() = having("extension", File::extension)

/**
 * Returns an assert on the file's contents as text.
 */
fun Assert<File>.text(charset: Charset = Charsets.UTF_8) = having("text") { it.readText(charset) }

/**
 * Returns an assert on the file's contents as bytes.
 */
fun Assert<File>.bytes() = having("bytes", File::readBytes)

/**
 * Asserts the file exists.
 */
fun Assert<File>.exists() = given { actual ->
    if (actual.exists()) return
    expected("to exist")
}

/**
 * Asserts the file does not exist.
 */
fun Assert<File>.doesNotExist() = given { actual ->
    if (!actual.exists()) return
    expected("not to exist")
}

/**
 * Asserts the file is a directory.
 * @see [isFile]
 */
fun Assert<File>.isDirectory() = given { actual ->
    if (actual.isDirectory) return
    expected("to be a directory")
}

/**
 * Asserts the file is a simple file (not a directory).
 * @see [isDirectory]
 */
fun Assert<File>.isFile() = given { actual ->
    if (actual.isFile) return
    expected("to be a file")
}

/** Assert that the file is an executable. */
fun Assert<File>.canExecute() = given { actual ->
    if (actual.canExecute()) return
    expected("to be executable")
}

/**
 * Asserts the file is hidden.
 * @see [isNotHidden]
 */
fun Assert<File>.isHidden() = given { actual ->
    if (actual.isHidden) return
    expected("to be hidden")
}

/**
 * Asserts the file is not hidden.
 * @see [isHidden]
 */
fun Assert<File>.isNotHidden() = given { actual ->
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
    path().isEqualTo(File(expected).path)
}

/**
 * Asserts the file has the expected parent path.
 */
fun Assert<File>.hasParent(expected: String) {
    parent().isEqualTo(File(expected).path)
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
fun Assert<File>.hasDirectChild(expected: File) = given { actual ->
    if (actual.listFiles()?.contains(expected) == true) return
    expected("to have direct child ${show(expected)}")
}
