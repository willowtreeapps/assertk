package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import test.assertk.Assertions.Companion.assertThatThrownBy
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

private var regularFile: Path? = null
private var directory: Path? = null

class PathSpec_a_regular_file {

    @BeforeTest
    fun beforeGroup() {
        regularFile = createTempFile()
        directory = createTempDir()
    }

    @AfterTest
    fun afterGroup() {
        regularFile?.let { Files.deleteIfExists(it) }
        directory?.let { Files.deleteIfExists(it) }
    }

    @Test
    fun it_is_a_regular_file() {
        assert(regularFile!!).isRegularFile()
    }

    @Test
    fun it_is_not_a_folder() {
        assertThatThrownBy {
            assert(regularFile!!).isDirectory()
        }.hasMessage("expected <$regularFile> to be a directory, but it is not")
    }

    @Test
    fun it_is_not_hidden() {
        assertThatThrownBy {
            assert(regularFile!!).isHidden()
        }.hasMessage("expected <$regularFile> to be hidden, but it is not")
    }

    @Test
    fun it_is_readable() {
        assert(regularFile!!).isReadable()
    }

    @Test
    fun it_is_not_a_symbolic_link() {
        assertThatThrownBy {
            assert(regularFile!!).isSymbolicLink()
        }.hasMessage("expected <$regularFile> to be a symbolic link, but it is not")
    }

    @Test
    fun it_is_writeable() {
        assert(regularFile!!).isWritable()
    }

    @Test
    fun it_is_same_file_as_itself() {
        assert(regularFile!!).isSameFileAs(regularFile!!)
    }

    @Test
    fun it_is_same_file_as_itself_even_when_the_path_is_different() {
        assert(regularFile!!).isSameFileAs(regularFile!!.toAbsolutePath())
    }
}

class PathSpec_a_directory {

    @BeforeTest
    fun beforeGroup() {
        regularFile = createTempFile()
        directory = createTempDir()
    }

    @AfterTest
    fun afterGroup() {
        regularFile?.let { Files.deleteIfExists(it) }
        directory?.let { Files.deleteIfExists(it) }
    }

    @Test
    fun it_is_not_a_regular_file() {
        assertThatThrownBy {
            assert(directory!!).isRegularFile()
        }.hasMessage("expected <$directory> to be a regular file, but it is not")
    }

    @Test
    fun it_is_a_folder() {
        assert(directory!!).isDirectory()
    }

    @Test
    fun it_is_not_hidden() {
        assertThatThrownBy {
            assert(directory!!).isHidden()
        }.hasMessage("expected <$directory> to be hidden, but it is not")
    }

    @Test
    fun it_is_readable() {
        assert(directory!!).isReadable()
    }

    @Test
    fun it_is_not_a_symbolic_link() {
        assertThatThrownBy {
            assert(directory!!).isSymbolicLink()
        }.hasMessage("expected <$directory> to be a symbolic link, but it is not")
    }

    @Test
    fun it_is_writeable() {
        assert(directory!!).isWritable()
    }

    @Test
    fun it_is_same_file_as_itself() {
        assert(directory!!).isSameFileAs(directory!!)
    }

    @Test
    fun it_is_same_file_as_itself_even_when_the_path_is_different() {
        assert(directory!!).isSameFileAs(directory!!.toAbsolutePath())
        assert(directory!!).isSameFileAs(directory!!.toAbsolutePath())
    }
}


private fun createTempDir() = Files.createTempDirectory("tempDir")
private fun createTempFile() = Files.createTempFile("tempFile", "").apply { toFile().writeBytes(ByteArray(10)) }
