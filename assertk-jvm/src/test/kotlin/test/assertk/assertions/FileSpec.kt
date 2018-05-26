package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import java.io.File
import java.nio.file.Files
import kotlin.test.*

class FileSpec_a_file_On_exists() {
    val file = File.createTempFile("exists", "txt")

    @Test
    fun it_given_a_file_exists_should_pass_a_successful_test() {
        assert(file).exists()
    }

    @Test
    fun it_given_a_file_doesnt_exist_should_fail_an_unsuccessful_test() {
        file.delete()
        val error = assertFails {
            assert(file).exists()
        }
        assertEquals("expected to exist", error.message)
    }
}

class FileSpec_a_file_On_isDirectory() {
    val directory = Files.createTempDirectory("isDirectory").toFile()
    val file = File.createTempFile("file", "txt", directory)

    @Test
    fun it_given_a_directory_should_pass_a_successful_test() {
        assert(directory).isDirectory()
    }

    @Test
    fun it_given_a_file_should_fail_an_unsuccessful_test() {
        val error = assertFails {
            assert(file).isDirectory()
        }
        assertEquals("expected to be a directory", error.message)
    }
}

class FileSpec_a_file_On_isFile() {
    val directory = Files.createTempDirectory("isDirectory").toFile()
    val file = File.createTempFile("file", "txt", directory)

    @Test
    fun it_given_a_file_should_pass_a_successful_test() {
        assert(file).isFile()
    }

    @Test
    fun it_given_a_directory_should_fail_an_unsuccessful_test() {
        val error = assertFails {
            assert(directory).isFile()
        }
        assertEquals("expected to be a file", error.message)
    }
}

class FileSpec_a_file_On_isNotHidden() {
    val file = File.createTempFile("file", "txt")

    @Test
    fun it_given_a_file_is_not_hidden_should_pass_a_successful_test() {
        assert(file).isNotHidden()
    }

    @Test
    fun it_given_a_file_isnt_hidden_should_fail_an_unsuccessful_test() {
        val error = assertFails {
            assert(file).isHidden()
        }
        assertEquals("expected to be hidden", error.message)
    }
}

class FileSpec_a_file_On_hasName() {
    val file = File("assertKt/file.txt")
    val directory = File("assertKt/directory")

    @Test
    fun it_given_a_files_name_should_pass_a_successful_test() {
        assert(file).hasName("file.txt")
    }

    @Test
    fun it_given_a_directorys_name_should_pass_a_successful_test() {
        assert(directory).hasName("directory")
    }

    @Test
    fun it_given_files_name_should_fail_an_unsuccessful_test() {
        val error = assertFails {
            assert(file).hasName("file")
        }
        assertEquals("expected [name]:<\"file[]\"> but was:<\"file[.txt]\"> (assertKt/file.txt)", error.message)
    }

    @Test
    fun it_given_directorys_name_should_fail_an_unsuccessful_test() {
        val error = assertFails {
            assert(directory).hasName("assertKt")
        }
        assertEquals(
            "expected [name]:<\"[assertKt]\"> but was:<\"[${directory.name}]\"> (assertKt/directory)",
            error.message
        )
    }
}

class FileSpec_a_file_On_hasPath() {
    val file = File("assertKt/file.txt")

    @Test
    fun it_given_a_files_path_should_pass_a_successful_test() {
        assert(file).hasPath("assertKt/file.txt")
    }

    @Test
    fun it_given_a_files_path_should_fail_an_unsuccessful_test() {
        val error = assertFails {
            assert(file).hasPath("/directory")
        }
        assertEquals(
            "expected [path]:<\"[/directory]\"> but was:<\"[${file.path}]\"> (assertKt/file.txt)",
            error.message
        )
    }
}

class FileSpec_a_file_On_hasParent() {
    val file = File("assertKt/file.txt")

    @Test
    fun it_given_a_files_parent_should_pass_a_successful_test() {
        assert(file).hasParent("assertKt")
    }

    @Test
    fun it_given_a_files_parent_should_fail_an_unsuccessful_test() {
        val error = assertFails {
            assert(file).hasParent("directory")
        }
        assertEquals(
            "expected [parent]:<\"[directory]\"> but was:<\"[${file.parent}]\"> (assertKt/file.txt)",
            error.message
        )
    }
}

class FileSpec_a_file_On_hasExtension() {
    val file = File("file.txt")

    @Test
    fun it_given_a_files_extension_should_pass_a_successful_test() {
        assert(file).hasExtension("txt")
    }

    @Test
    fun it_given_a_files_extension_should_fail_an_unsuccessful_test() {
        val error = assertFails {
            assert(file).hasExtension("png")
        }
        assertEquals("expected [extension]:<\"[png]\"> but was:<\"[${file.extension}]\"> (file.txt)", error.message)
    }
}

class FileSpec_a_file_On_hasText() {
    val file = File.createTempFile("file_contains", ".txt")
    val text =
        "The Answer to the Great Question... Of Life, the Universe and Everything... Is... Forty-two,' said Deep Thought, with infinite majesty and calm."

    @BeforeTest
    fun setup() {
        file.writeText(text)
    }

    @Test
    fun it_given_a_files_text_should_pass_a_successful_test() {
        assert(file).hasText(text)
    }

    @Test
    fun it_given_a_files_text_should_fail_an_unsuccessful_test() {
        val error = assertFails {
            assert(file).hasText("Forty-two!")
        }
        assertTrue(error.message!!.startsWith("expected [text]:<\"[Forty-two!]\"> but was:<\"[$text]\">"))
        assertTrue(error.message!!.contains("file_contains"))
    }
}

class FileSpec_a_file_On_containsText() {
    val file = File.createTempFile("file_contains", ".txt")
    val text =
        "The Answer to the Great Question... Of Life, the Universe and Everything... Is... Forty-two,' said Deep Thought, with infinite majesty and calm."

    @BeforeTest
    fun setup() {
        file.writeText(text)
    }

    @Test
    fun it_given_a_files_text_should_pass_a_successful_test() {
        assert(file).text().contains("Forty-two")
    }

    @Test
    fun it_given_a_files_text_should_fail_an_unsuccessful_test() {
        val error = assertFails {
            assert(file).text().contains("Forty-two!")
        }
        assertTrue(error.message!!.startsWith("expected [text] to contain:<\"Forty-two!\"> but was:<\"$text\">"))
        assertTrue(error.message!!.contains("file_contains"))
    }
}

class FileSpec_a_file_On_matchesText() {
    val file = File.createTempFile("file_contains", ".txt")
    val text = "Matches"
    @BeforeTest
    fun setup() {
        file.writeText(text)
    }

    @Test
    fun it_given_a_files_regexp_should_pass_a_successful_test() {
        assert(file).text().matches(".a...e.".toRegex())
    }

    @Test
    fun it_given_a_files_regexp_should_fail_an_unsuccessful_test() {
        val incorrectRegexp = ".*d".toRegex()
        val error = assertFails {
            assert(file).text().matches(incorrectRegexp)
        }
        assertTrue(error.message!!.startsWith("expected [text] to match:</$incorrectRegexp/> but was:<\"$text\">"))
        assertTrue(error.message!!.contains("file_contains"))
    }
}

class FileSpec_a_file_On_hasDirectChild() {
    val directory = Files.createTempDirectory("isDirectory").toFile()
    val file = File.createTempFile("file", ".txt", directory)

    @Test
    fun it_given_a_nonempty_directory_with_direct_child_should_pass_a_successful_test() {
        assert(directory).hasDirectChild(file)
    }

    @Test
    fun it_given_a_nonempty_directory_with_no_exists_child__should_fail_an_unsuccessful_test() {
        val newFile = File.createTempFile("file", ".txt")
        val error = assertFails {
            assert(directory).hasDirectChild(newFile)
        }
        assertEquals("expected to have direct child <$newFile>", error.message)
    }

    @Test
    fun it_given_a_empty_directory_should_fail_an_unsuccessful_test() {
        directory.listFiles().forEach { it.delete() }
        val error = assertFails {
            assert(directory).hasDirectChild(file)
        }
        assertEquals("expected to have direct child <$file>", error.message)
    }
}
