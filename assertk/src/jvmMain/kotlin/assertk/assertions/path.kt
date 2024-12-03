package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path

/**
 * Assert on file lines
 *
 * @param charset charset to use when reading file
 */
fun Assert<Path>.lines(charset: Charset = Charsets.UTF_8): Assert<List<String>> =
    transform { actual -> Files.readAllLines(actual, charset) }

/** Assert on file bytes */
fun Assert<Path>.bytes(): Assert<ByteArray> =
    transform { actual -> Files.readAllBytes(actual) }

/** Assert that the path is a regular file.
 *
 * @param options indicating how symbolic links are handled
 */
@Suppress("SpreadOperator") // https://github.com/arturbosch/detekt/issues/391
fun Assert<Path>.isRegularFile(vararg options: LinkOption) = given { actual ->
    if (!Files.isRegularFile(actual, *options)) {
        expected("${show(actual)} to be a regular file, but it is not")
    }
}

/** Assert that the path is a directory.
 *
 * @param options indicating how symbolic links are handled
 */
@Suppress("SpreadOperator") // https://github.com/arturbosch/detekt/issues/391
fun Assert<Path>.isDirectory(vararg options: LinkOption) = given { actual ->
    if (!Files.isDirectory(actual, *options)) {
        expected("${show(actual)} to be a directory, but it is not")
    }
}

/** Assert that the path is an executable. */
fun Assert<Path>.isExecutable() = given { actual ->
    if (!Files.isExecutable(actual)) {
        expected("${show(actual)} to be an executable, but it is not")
    }
}

/** Assert that the path is hidden. */
fun Assert<Path>.isHidden() = given { actual ->
    if (!Files.isHidden(actual)) {
        expected("${show(actual)} to be hidden, but it is not")
    }
}

/** Assert that the path is readable. */
fun Assert<Path>.isReadable() = given { actual ->
    if (!Files.isReadable(actual)) {
        expected("${show(actual)} to be readable, but it is not")
    }
}

/** Assert that the path is a symbolic link. */
fun Assert<Path>.isSymbolicLink() = given { actual ->
    if (!Files.isSymbolicLink(actual)) {
        expected("${show(actual)} to be a symbolic link, but it is not")
    }
}

/** Assert that the path is writable link. */
fun Assert<Path>.isWritable() = given { actual ->
    if (!Files.isWritable(actual)) {
        expected("${show(actual)} to be writable, but it is not")
    }
}

/**
 * Assert that the path points to the same object as the other path.
 *
 * @param expected the path which the actual is compared to.
 */
fun Assert<Path>.isSameFileAs(expected: Path) = given { actual ->
    if (!Files.isSameFile(actual, expected)) {
        expected("${show(actual)} to be the same file as ${show(actual)} but is not")
    }
}

/**
 * Assert that the path exists.
 *
 * @param options indicating how symbolic links are handled
 */
@Suppress("SpreadOperator") // https://github.com/arturbosch/detekt/issues/391
fun Assert<Path>.exists(vararg options: LinkOption) = given { actual ->
    if (!Files.exists(actual, *options)) {
        expected("${show(actual)} to exist, but it does not")
    }
}

/**
 * Assert that the path does not exist.
 *
 * @param options indicating how symbolic links are handled
 */
@Suppress("SpreadOperator") // https://github.com/arturbosch/detekt/issues/391
fun Assert<Path>.doesNotExist(vararg options: LinkOption) = given { actual ->
    if (!Files.notExists(actual, *options)) {
        expected("${show(actual)} does not exist, but it exists")
    }
}
