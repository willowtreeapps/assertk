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
fun Assert<File>.havingName() = having("name", File::getName)

@Deprecated(
    message = "Function name has been renamed to havingName",
    replaceWith = ReplaceWith("havingName()"),
    level = DeprecationLevel.WARNING
)
fun Assert<File>.name() = havingName()

/**
 * Returns an assert on the file's path.
 */
fun Assert<File>.havingPath() = having("path", File::getPath)

@Deprecated(
    message = "Function path has been renamed to havingPath",
    replaceWith = ReplaceWith("havingPath()"),
    level = DeprecationLevel.WARNING
)
fun Assert<File>.path() = havingPath()

/**
 * Returns an assert on the file's parent.
 */
fun Assert<File>.havingParent() = having("parent", File::getParent)

@Deprecated(
    message = "Function parent has been renamed to havingParent",
    replaceWith = ReplaceWith("havingParent()"),
    level = DeprecationLevel.WARNING
)
fun Assert<File>.parent() = havingParent()

/**
 * Returns an assert on the file's extension.
 */
fun Assert<File>.havingExtension() = having("extension", File::extension)

@Deprecated(
    message = "Function extension has been renamed to havingExtension",
    replaceWith = ReplaceWith("havingExtension()"),
    level = DeprecationLevel.WARNING
)
fun Assert<File>.extension() = havingExtension()

/**
 * Returns an assert on the file's contents as text.
 */
fun Assert<File>.havingText(charset: Charset = Charsets.UTF_8) = having("text") { it.readText(charset) }

@Deprecated(
    message = "Function text has been renamed to havingText",
    replaceWith = ReplaceWith("havingText()"),
    level = DeprecationLevel.WARNING
)
fun Assert<File>.text(charset: Charset = Charsets.UTF_8) = havingText()

/**
 * Returns an assert on the file's contents as bytes.
 */
fun Assert<File>.havingBytes() = having("bytes", File::readBytes)

@Deprecated(
    message = "Function bytes has been renamed to havingBytes",
    replaceWith = ReplaceWith("havingBytes()"),
    level = DeprecationLevel.WARNING
)
fun Assert<File>.bytes() = havingBytes()

/**
 * Asserts the file exists.
 */
fun Assert<File>.exists() = given { actual ->
    if (actual.exists()) return
    expected("to exist")
}

/**
 * Asserts the file does not exists.
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
    havingName().isEqualTo(expected)
}

/**
 * Asserts the file has the expected path.
 */
fun Assert<File>.hasPath(expected: String) {
    havingPath().isEqualTo(File(expected).path)
}

/**
 * Asserts the file has the expected parent path.
 */
fun Assert<File>.hasParent(expected: String) {
    havingParent().isEqualTo(File(expected).path)
}

/**
 * Asserts the file has the expected extension.
 */
fun Assert<File>.hasExtension(expected: String) {
    havingExtension().isEqualTo(expected)
}

/**
 * Asserts the file contains exactly the expected text (and nothing else).
 * @param charset The character set of the file, default is [Charsets.UTF_8]
 * @see [hasBytes]
 */
fun Assert<File>.hasText(expected: String, charset: Charset = Charsets.UTF_8) {
    havingText(charset).isEqualTo(expected)
}

/**
 * Asserts the file has the expected direct child.
 */
fun Assert<File>.hasDirectChild(expected: File) = given { actual ->
    if (actual.listFiles()?.contains(expected) == true) return
    expected("to have direct child ${show(expected)}")
}
