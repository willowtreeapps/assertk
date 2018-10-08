package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.*

private var regularFile: Path? = null
private var directory: Path? = null
private var regularFileWithText: Path? = null

class PathTest {

    @BeforeTest
    fun beforeGroup() {
        regularFile = createTempFile()
        directory = createTempDir()
        regularFileWithText = createTempFileWithText()
    }

    @AfterTest
    fun afterGroup() {
        regularFile?.let(Files::deleteIfExists)
        directory?.let(Files::deleteIfExists)
        regularFileWithText?.let(Files::deleteIfExists)
    }

    //region isRegularFile
    @Test fun isRegularFile_value_regularFile_passes() {
        assert(regularFile!!).isRegularFile()
    }

    @Test fun isRegularFile_value_directory_fails() {
        val error = assertFails {
            assert(directory!!).isRegularFile()
        }
        assertEquals("expected <$directory> to be a regular file, but it is not", error.message)
    }
    //endregion

    //region isDirectory
    @Test fun isDirectory_value_not_directory_fails() {
        val error = assertFails {
            assert(regularFile!!).isDirectory()
        }
        assertEquals("expected <$regularFile> to be a directory, but it is not", error.message)
    }

    @Test fun isDirectory_value_directory_passes() {
        assert(directory!!).isDirectory()
    }
    //endregion

    //region isHidden
    @Test fun isHidden_value_regular_file_not_hidden_fails() {
        val error = assertFails {
            assert(regularFile!!).isHidden()
        }
        assertEquals("expected <$regularFile> to be hidden, but it is not", error.message)
    }

    @Test fun isHidden_value_directory_not_hidden_fails() {
        val error = assertFails {
            assert(directory!!).isHidden()
        }
        assertEquals("expected <$directory> to be hidden, but it is not", error.message)
    }
    //endregion

    //region isReadable
    @Test fun isReadable_value_readable_file_passes() {
        assert(regularFile!!).isReadable()
    }

    @Test fun isReadable_value_readable_directory_passes() {
        assert(directory!!).isReadable()
    }
    //endregion

    //region isSymbolicLink
    @Test fun isSymbolicLink_value_regular_file_fails() {
        val error = assertFails {
            assert(regularFile!!).isSymbolicLink()
        }
        assertEquals("expected <$regularFile> to be a symbolic link, but it is not", error.message)
    }

    @Test fun isSymbolicLink_value_directory_fails() {
        val error = assertFails {
            assert(directory!!).isSymbolicLink()
        }
        assertEquals("expected <$directory> to be a symbolic link, but it is not", error.message)
    }
    //endregion

    //region isWritable
    @Test fun isWritable_value_writable_file_passes() {
        assert(regularFile!!).isWritable()
    }

    @Test fun isWritable_value_writable_directory_passes() {
        assert(directory!!).isWritable()
    }
    //endregion

    //region isSameFileAs
    @Test fun isSameFileAs_value_same_file_passes() {
        assert(regularFile!!).isSameFileAs(regularFile!!)
    }

    @Test fun isSameFileAs_value_same_directory_passes() {
        assert(directory!!).isSameFileAs(directory!!)
    }

    @Test fun isSameFileAs_value_same_file_different_path_passes() {
        assert(regularFile!!).isSameFileAs(regularFile!!.toAbsolutePath())
    }

    @Test fun isSameFileAs_value_same_directory_different_path_passes() {
        assert(directory!!).isSameFileAs(directory!!.toAbsolutePath())
    }
    //endregion

    //region lines
    @Test fun lines_correct_string_passes() {
        assert(regularFileWithText!!).lines().containsExactly("a", "b")
    }
    //endregion

    //region bytes
    @Test fun bytes_value_correct_byte_array_passes() {
        assert(regularFile!!).bytes().isEqualTo(ByteArray(10))
    }
    //endregion
}

private fun createTempDir() = Files.createTempDirectory("tempDir")
private fun createTempFile() = Files.createTempFile("tempFile", "").apply { toFile().writeBytes(ByteArray(10)) }
private fun createTempFileWithText() = Files.createTempFile("tempFileWithText", "").apply { toFile().writeText("a\nb", Charsets.UTF_8) }
