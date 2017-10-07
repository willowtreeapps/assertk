package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.io.File
import java.nio.file.Files

class FileSpec : Spek({

    given("a file") {

        on("exists()") {
            val file = File.createTempFile("exists", "txt")

            it("given a file exists, should pass a successful test") {
                assert(file).exists()
            }

            it("given a file doesn't exist, should fail an unsuccessful test") {
                file.delete()
                Assertions.assertThatThrownBy {
                    assert(file).exists()
                }.hasMessage("expected to exist")
            }
        }

        on("isDirectory()") {
            val directory = Files.createTempDirectory("isDirectory").toFile()
            val file = File.createTempFile("file", "txt", directory)

            it("given a directory, should pass a successful test") {
                assert(directory).isDirectory()
            }

            it("given a file, should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(file).isDirectory()
                }.hasMessage("expected to be a directory")
            }
        }

        on("isFile()") {
            val directory = Files.createTempDirectory("isDirectory").toFile()
            val file = File.createTempFile("file", "txt", directory)

            it("given a file, should pass a successful test") {
                assert(file).isFile()
            }

            it("given a directory, should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(directory).isFile()
                }.hasMessage("expected to be a file")
            }
        }

        on("isNotHidden()") {
            val file = File.createTempFile("file", "txt")

            it("given a file is not hidden, should pass a successful test") {
                assert(file).isNotHidden()
            }

            it("given a file isn't hidden, should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(file).isHidden()
                }.hasMessage("expected to be hidden")
            }
        }

        on("hasName()") {
            val file = File("assertKt/file.txt")
            val directory = File("assertKt/directory")

            it("given a file's name, should pass a successful test") {
                assert(file).hasName("file.txt")
            }

            it("given a directory's name, should pass a successful test") {
                assert(directory).hasName("directory")
            }

            it("given file's name, should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(file).hasName("file")
                }.hasMessage("expected [name]:<\"file[]\"> but was:<\"file[.txt]\">")
            }

            it("given directory's name, should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(directory).hasName("assertKt")
                }.hasMessage("expected [name]:<\"[assertKt]\"> but was:<\"[${directory.name}]\">")
            }
        }

        on("hasPath()") {
            val file = File("assertKt/file.txt")

            it("given a file's path, should pass a successful test") {
                assert(file).hasPath("assertKt/file.txt")
            }

            it("given a file's path, should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(file).hasPath("/directory")
                }.hasMessage("expected [path]:<\"[/directory]\"> but was:<\"[${file.path}]\">")
            }
        }

        on("hasParent()") {
            val file = File("assertKt/file.txt")

            it("given a file's parent, should pass a successful test") {
                assert(file).hasParent("assertKt")
            }

            it("given a file's parent, should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(file).hasPath("directory")
                }.hasMessage("expected [path]:<\"[directory]\"> but was:<\"[${file.path}]\">")
            }
        }

        on("hasExtension()") {
            val file = File("file.txt")

            it("given a file's extension, should pass a successful test") {
                assert(file).hasExtension("txt")
            }

            it("given a file's extension, should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(file).hasExtension("png")
                }.hasMessage("expected [extension]:<\"[png]\"> but was:<\"[${file.extension}]\">")
            }
        }

        on("hasText()") {
            val file = File.createTempFile("file_contains", "txt")
            val text = "The Answer to the Great Question... Of Life, the Universe and Everything... Is... Forty-two,' said Deep Thought, with infinite majesty and calm."
            file.writeText(text)

            it("given a file's text, should pass a successful test") {
                assert(file).hasText(text)
            }

            it("given a file's text, should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(file).hasText("Forty-two!")
                }.hasMessage("expected [text]:<\"[Forty-two!]\"> but was:<\"[$text]\">")
            }
        }

        on("containsText()") {
            val file = File.createTempFile("file_contains", "txt")
            val text = "The Answer to the Great Question... Of Life, the Universe and Everything... Is... Forty-two,' said Deep Thought, with infinite majesty and calm."
            file.writeText(text)

            it("given a file's text, should pass a successful test") {
                assert(file).containsText("Forty-two")
            }

            it("given a file's text, should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(file).containsText("Forty-two!")
                }.hasMessage("expected [text] to contain:<\"Forty-two!\"> but was:<\"$text\">")
            }
        }

        on("matchesText()") {
            val file = File.createTempFile("file_contains", "txt")
            val text = "Matches"
            file.writeText(text)

            it("given a file's regexp, should pass a successful test") {
                assert(file).matchesText(".a...e.".toRegex())
            }

            it("given a file's regexp, should fail an unsuccessful test") {
                val incorrectRegexp = ".*d".toRegex()
                Assertions.assertThatThrownBy {
                    assert(file).matchesText(incorrectRegexp)
                }.hasMessage("expected [text] to match:</$incorrectRegexp/> but was:<\"$text\">")
            }
        }

        on("hasDirectChild()") {
            val directory = Files.createTempDirectory("isDirectory").toFile()
            val file = File.createTempFile("file", "txt", directory)

            it("given a non-empty directory with direct child, should pass a successful test") {
                assert(directory).hasDirectChild(file)
            }

            it("given a non-empty directory with no exists child,  should fail an unsuccessful test") {
                val newFile = File.createTempFile("file", "txt")
                Assertions.assertThatThrownBy {
                    assert(directory).hasDirectChild(newFile)
                }.hasMessage("expected to have direct child <$newFile>")
            }

            it("given a empty directory, should fail an unsuccessful test") {
                directory.listFiles().forEach { it.delete() }
                Assertions.assertThatThrownBy {
                    assert(directory).hasDirectChild(file)
                }.hasMessage("expected to have direct child <$file>")
            }
        }
    }
})
