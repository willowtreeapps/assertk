package assertk.assertions

import assertk.Assert
import assertk.assertions.support.ListDiffer
import assertk.assertions.support.expected
import assertk.assertions.support.show
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path

/** Assert that the path is a regular file.
 *
 * @param options indicating how symbolic links are handled
 */
fun Assert<Path>.isRegularFile(vararg options: LinkOption) {
    if (!Files.isRegularFile(actual, *options)) {
        expected("${show(actual)} to be a regular file, but it is not")
    }
}

/** Assert that the path is a directory.
 *
 * @param options indicating how symbolic links are handled
 */
fun Assert<Path>.isDirectory(vararg options: LinkOption) {
    if (!Files.isDirectory(actual, *options)) {
        expected("${show(actual)} to be a directory, but it is not")
    }
}

/** Assert that the path is an executable. */
fun Assert<Path>.isExecutable() {
    if (!Files.isExecutable(actual)) {
        expected("${show(actual)} to be an executable, but it is not")
    }
}

/** Assert that the path is hidden. */
fun Assert<Path>.isHidden() {
    if (!Files.isHidden(actual)) {
        expected("${show(actual)} to be hidden, but it is not")
    }
}

/** Assert that the path is readable. */
fun Assert<Path>.isReadable() {
    if (!Files.isReadable(actual)) {
        expected("${show(actual)} to be readable, but it is not")
    }
}

/** Assert that the path is a symbolic link. */
fun Assert<Path>.isSymbolicLink() {
    if (!Files.isSymbolicLink(actual)) {
        expected("${show(actual)} to be a symbolic link, but it is not")
    }
}

/** Assert that the path is writable link. */
fun Assert<Path>.isWritable() {
    if (!Files.isWritable(actual)) {
        expected("${show(actual)} to be writable, but it is not")
    }
}

/**
 * Assert that the path points to the same object as the other path.
 *
 * @param expected the path which the actual is compared to.
 */
fun Assert<Path>.isSameFileAs(expected: Path) {
    if (!Files.isSameFile(actual, expected)) {
        expected("${show(actual)} to be the same file as ${show(actual)} but is not")
    }
}

/**
 * Assert that Path contains exactly expected lines
 *
 * @param expected list of string to compare file contents
 * @param charset charset to use when reading files
 */
fun Assert<Path>.lines(expected: List<String>, charset: Charset = Charsets.UTF_8) {
    val lines = Files.readAllLines(actual, charset)
    if (lines == expected) return

    val diff = ListDiffer.diff(expected, lines)
        .filterNot { it is ListDiffer.Edit.Eq }

    expected(diff.joinToString(prefix = "<$actual> to contain exactly:\n", separator = "\n") { edit ->
        when (edit) {
            is ListDiffer.Edit.Del -> " at line:${edit.oldIndex} expected:${show(edit.oldValue)}"
            is ListDiffer.Edit.Ins -> " at line:${edit.newIndex} unexpected:${show(edit.newValue)}"
            else -> throw IllegalStateException()
        }
    })
}

/**
 * Assert that Path contains exact bytes
 *
 * @param expected byte array to compare with path contents
 */
fun Assert<Path>.bytes(expected: ByteArray) {
    val bytes = Files.readAllBytes(actual)!!
    if(!bytes.contentEquals(expected)) {
        expected("path <$actual> to contain bytes:${show(expected)} but was:${show(bytes)}")
    }
}