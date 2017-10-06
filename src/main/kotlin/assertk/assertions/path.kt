package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path

fun Assert<Path>.isRegularFile(vararg options: LinkOption) {
    if(!Files.isRegularFile(actual, *options)) {
        expected("${show(actual)} to be a regular file, but it is not")
    }
}

fun Assert<Path>.isDirectory(vararg options: LinkOption) {
    if(!Files.isDirectory(actual, *options)) {
        expected("${show(actual)} to be a directory, but it is not")
    }
}

fun Assert<Path>.isExecutable() {
    if(!Files.isExecutable(actual)) {
        expected("${show(actual)} to be an executable, but it is not")
    }
}

fun Assert<Path>.isHidden() {
    if(!Files.isHidden(actual)) {
        expected("${show(actual)} to be hidden, but it is not")
    }
}

fun Assert<Path>.isReadable() {
    if (!Files.isReadable(actual)) {
        expected("${show(actual)} to be readable, but it is not")
    }
}

fun Assert<Path>.isSymbolicLink() {
    if (!Files.isSymbolicLink(actual)) {
        expected("${show(actual)} to be a symbolic link, but it is not")
    }
}

fun Assert<Path>.isWritable() {
    if (!Files.isWritable(actual)) {
        expected("${show(actual)} to be writable, but it is not")
    }
}

fun Assert<Path>.isSameFileAs(expected: Path) {
    if (!Files.isSameFile(actual, expected)) {
        expected("${show(actual)} to be the same file as ${show(actual)} but is not")
    }
}