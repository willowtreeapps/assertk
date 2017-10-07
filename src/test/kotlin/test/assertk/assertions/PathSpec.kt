package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.nio.file.Paths

class PathSpec: Spek({

    given("a regular file") {
        val regularFile = Paths.get("LICENSE")

        it("is a regular file") {
            assert( regularFile ).isRegularFile()
        }

        it("is not a folder") {
            assertThatThrownBy {
                assert( regularFile ).isDirectory()
            }.hasMessage("expected <LICENSE> to be a directory, but it is not")
        }

//        it("is not an executable") {
//            assertThatThrownBy {
//                assert( regularFile ).isExecutable()
//            }.hasMessage("Foo")
//        }

        it("is not hidden") {
            assertThatThrownBy {
                assert( regularFile ).isHidden()
            }.hasMessage("expected <LICENSE> to be hidden, but it is not")
        }

        it("is readable") {
            assert( regularFile ).isReadable()
        }

        it("is not a symbolic link") {
            assertThatThrownBy {
                assert( regularFile ).isSymbolicLink()
            }.hasMessage("expected <LICENSE> to be a symbolic link, but it is not")
        }

        it("is writeable") {
            assert( regularFile ).isWritable()
        }

        it("is same file as itself") {
            assert( regularFile ).isSameFileAs( Paths.get("LICENSE") )
        }

        it("is same file as itself even when the path is different") {
            assert( regularFile ).isSameFileAs( Paths.get("LICENSE").toAbsolutePath() )
            assert( regularFile ).isSameFileAs( Paths.get("src/../LICENSE") )
        }
    }

    given("a directory") {
        val directory = Paths.get("src")

        it("is not a regular file") {
            assertThatThrownBy {
                assert( directory ).isRegularFile()
            }.hasMessage("expected <src> to be a regular file, but it is not")
        }

        it("is a folder") {
            assert( directory ).isDirectory()
        }

        it("is not hidden") {
            assertThatThrownBy {
                assert( directory ).isHidden()
            }.hasMessage("expected <src> to be hidden, but it is not")
        }

        it("is readable") {
            assert( directory ).isReadable()
        }

        it("is not a symbolic link") {
            assertThatThrownBy {
                assert( directory ).isSymbolicLink()
            }.hasMessage("expected <src> to be a symbolic link, but it is not")
        }

        it("is writeable") {
            assert( directory ).isWritable()
        }

        it("is same file as itself") {
            assert( directory ).isSameFileAs( Paths.get("src") )
        }

        it("is same file as itself even when the path is different") {
            assert( directory ).isSameFileAs( Paths.get("src").toAbsolutePath() )
            assert( directory ).isSameFileAs( Paths.get("src/../src") )
        }
    }
})