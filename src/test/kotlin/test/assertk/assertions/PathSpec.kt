package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.nio.file.Files
import java.nio.file.Path

class PathSpec : Spek({
    var regularFile: Path? = null
    var directory: Path? = null

    beforeGroup {
        regularFile = createTempFile()
        directory = createTempDir()
    }

    afterGroup {
        regularFile?.let { Files.deleteIfExists(it) }
        directory?.let { Files.deleteIfExists(it) }
    }

    given("a regular file") {

        it("is a regular file") {
            assert(regularFile!!).isRegularFile()
        }

        it("is not a folder") {
            assertThatThrownBy {
                assert(regularFile!!).isDirectory()
            }.hasMessage("expected <$regularFile> to be a directory, but it is not")
        }

        it("is not hidden") {
            assertThatThrownBy {
                assert(regularFile!!).isHidden()
            }.hasMessage("expected <$regularFile> to be hidden, but it is not")
        }

        it("is readable") {
            assert(regularFile!!).isReadable()
        }

        it("is not a symbolic link") {
            assertThatThrownBy {
                assert(regularFile!!).isSymbolicLink()
            }.hasMessage("expected <$regularFile> to be a symbolic link, but it is not")
        }

        it("is writeable") {
            assert(regularFile!!).isWritable()
        }

        it("is same file as itself") {
            assert(regularFile!!).isSameFileAs(regularFile!!)
        }

        it("is same file as itself even when the path is different") {
            assert(regularFile!!).isSameFileAs(regularFile!!.toAbsolutePath())
        }
    }

    given("a directory") {

        it("is not a regular file") {
            assertThatThrownBy {
                assert(directory!!).isRegularFile()
            }.hasMessage("expected <$directory> to be a regular file, but it is not")
        }

        it("is a folder") {
            assert(directory!!).isDirectory()
        }

        it("is not hidden") {
            assertThatThrownBy {
                assert(directory!!).isHidden()
            }.hasMessage("expected <$directory> to be hidden, but it is not")
        }

        it("is readable") {
            assert(directory!!).isReadable()
        }

        it("is not a symbolic link") {
            assertThatThrownBy {
                assert(directory!!).isSymbolicLink()
            }.hasMessage("expected <$directory> to be a symbolic link, but it is not")
        }

        it("is writeable") {
            assert(directory!!).isWritable()
        }

        it("is same file as itself") {
            assert(directory!!).isSameFileAs(directory!!)
        }

        it("is same file as itself even when the path is different") {
            assert(directory!!).isSameFileAs(directory!!.toAbsolutePath())
            assert(directory!!).isSameFileAs(directory!!.toAbsolutePath())
        }
    }
})


private fun createTempDir() = Files.createTempDirectory("tempDir")
private fun createTempFile() = Files.createTempFile("tempFile", "").apply { toFile().writeBytes(ByteArray(10)) }
