package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.*

private var regularFile: Path? = null
private var directory: Path? = null
private var regularFileWithText: Path? = null
private var doesNotExist: Path? = null

class PathTest {

    @BeforeTest
    fun beforeGroup() {
        regularFile = createTempFile()
        directory = createTempDir()
        regularFileWithText = createTempFileWithText()
        doesNotExist = Path.of("/tmp/does_not_exist")
    }

    @AfterTest
    fun afterGroup() {
        regularFile?.let(Files::deleteIfExists)
        directory?.let(Files::deleteIfExists)
        regularFileWithText?.let(Files::deleteIfExists)
    }

    //region isRegularFile
    @Test fun isRegularFile_value_regularFile_passes() {
        assertThat(regularFile!!).isRegularFile()
    }

    @Test fun isRegularFile_value_directory_fails() {
        val error = assertFails {
            assertThat(directory!!).isRegularFile()
        }
        assertEquals("expected <$directory> to be a regular file, but it is not", error.message)
    }
    //endregion

    //region isDirectory
    @Test fun isDirectory_value_not_directory_fails() {
        val error = assertFails {
            assertThat(regularFile!!).isDirectory()
        }
        assertEquals("expected <$regularFile> to be a directory, but it is not", error.message)
    }

    @Test fun isDirectory_value_directory_passes() {
        assertThat(directory!!).isDirectory()
    }
    //endregion

    //region isHidden
    @Test fun isHidden_value_regular_file_not_hidden_fails() {
        val error = assertFails {
            assertThat(regularFile!!).isHidden()
        }
        assertEquals("expected <$regularFile> to be hidden, but it is not", error.message)
    }

    @Test fun isHidden_value_directory_not_hidden_fails() {
        val error = assertFails {
            assertThat(directory!!).isHidden()
        }
        assertEquals("expected <$directory> to be hidden, but it is not", error.message)
    }
    //endregion

    //region isReadable
    @Test fun isReadable_value_readable_file_passes() {
        assertThat(regularFile!!).isReadable()
    }

    @Test fun isReadable_value_readable_directory_passes() {
        assertThat(directory!!).isReadable()
    }
    //endregion

    //region isSymbolicLink
    @Test fun isSymbolicLink_value_regular_file_fails() {
        val error = assertFails {
            assertThat(regularFile!!).isSymbolicLink()
        }
        assertEquals("expected <$regularFile> to be a symbolic link, but it is not", error.message)
    }

    @Test fun isSymbolicLink_value_directory_fails() {
        val error = assertFails {
            assertThat(directory!!).isSymbolicLink()
        }
        assertEquals("expected <$directory> to be a symbolic link, but it is not", error.message)
    }
    //endregion

    //region isWritable
    @Test fun isWritable_value_writable_file_passes() {
        assertThat(regularFile!!).isWritable()
    }

    @Test fun isWritable_value_writable_directory_passes() {
        assertThat(directory!!).isWritable()
    }
    //endregion

    //region isSameFileAs
    @Test fun isSameFileAs_value_same_file_passes() {
        assertThat(regularFile!!).isSameFileAs(regularFile!!)
    }

    @Test fun isSameFileAs_value_same_directory_passes() {
        assertThat(directory!!).isSameFileAs(directory!!)
    }

    @Test fun isSameFileAs_value_same_file_different_path_passes() {
        assertThat(regularFile!!).isSameFileAs(regularFile!!.toAbsolutePath())
    }

    @Test fun isSameFileAs_value_same_directory_different_path_passes() {
        assertThat(directory!!).isSameFileAs(directory!!.toAbsolutePath())
    }
    //endregion

    //region exists
    @Test fun exists_value_regularFile_passes() {
        assertThat(regularFile!!).exists()
    }

    @Test fun exists_value_directory_passes() {
        assertThat(directory!!).exists()
    }

    @Test fun exists_value_not_exists_fails() {
        val error = assertFails {
            assertThat(doesNotExist!!).exists()
        }
        assertEquals("expected <$doesNotExist> to exist, but it does not", error.message)
    }
    //endregion

    //region lines
    @Test fun lines_correct_string_passes() {
        assertThat(regularFileWithText!!).lines().containsExactly("a", "b")
    }
    //endregion

    //region bytes
    @Test fun bytes_value_correct_byte_array_passes() {
        assertThat(regularFile!!).bytes().isEqualTo(ByteArray(10))
    }
    //endregion
}

private fun createTempDir() = Files.createTempDirectory("tempDir")
private fun createTempFile() = Files.createTempFile("tempFile", "").apply { toFile().writeBytes(ByteArray(10)) }
private fun createTempFileWithText() = Files.createTempFile("tempFileWithText", "").apply { toFile().writeText("a\nb", Charsets.UTF_8) }
