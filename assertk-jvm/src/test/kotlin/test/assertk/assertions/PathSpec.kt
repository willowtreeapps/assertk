package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.*

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
        val error = assertFails {
            assert(regularFile!!).isDirectory()
        }
        assertEquals("expected <$regularFile> to be a directory, but it is not", error.message)
    }

    @Test
    fun it_is_not_hidden() {
        val error = assertFails {
            assert(regularFile!!).isHidden()
        }
        assertEquals("expected <$regularFile> to be hidden, but it is not", error.message)
    }

    @Test
    fun it_is_readable() {
        assert(regularFile!!).isReadable()
    }

    @Test
    fun it_is_not_a_symbolic_link() {
        val error = assertFails {
            assert(regularFile!!).isSymbolicLink()
        }
        assertEquals("expected <$regularFile> to be a symbolic link, but it is not", error.message)
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
        val error = assertFails {
            assert(directory!!).isRegularFile()
        }
        assertEquals("expected <$directory> to be a regular file, but it is not", error.message)
    }

    @Test
    fun it_is_a_folder() {
        assert(directory!!).isDirectory()
    }

    @Test
    fun it_is_not_hidden() {
        val error = assertFails {
            assert(directory!!).isHidden()
        }
        assertEquals("expected <$directory> to be hidden, but it is not", error.message)
    }

    @Test
    fun it_is_readable() {
        assert(directory!!).isReadable()
    }

    @Test
    fun it_is_not_a_symbolic_link() {
        val error = assertFails {
            assert(directory!!).isSymbolicLink()
        }
        assertEquals("expected <$directory> to be a symbolic link, but it is not", error.message)
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
