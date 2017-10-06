package test.assertk.assertions

import assertk.assert
import assertk.assertions.hasNotSameContentAs
import assertk.assertions.hasSameContentAs
import org.assertj.core.api.Assertions.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*

class InputStreamSpec: Spek ({

    given("an empty stream") {

        it("has the same content as another empty stream") {
            assert(emptyStream()).hasSameContentAs(emptyStream())
        }

        it("has the same content as another empty stream (using hasNotSameContentAs)") {
            assertThatThrownBy {
                assert( emptyStream() ).hasNotSameContentAs( emptyStream() )
            }.hasMessage("expected stream not to be equal, but they were equal")
        }

        it("has not the same content as any other non empty stream") {
            assertThatThrownBy {
                assert(emptyStream()).hasSameContentAs(nonEmptyStream())
            }.hasMessage("expected to have the same size, but actual has size -1 which is smaller then the expected stream")
        }

        it("has not the same content as any other non emnpty stream (using hasNotSameContentAs)"){
            assert( emptyStream() ).hasNotSameContentAs( nonEmptyStream() )
        }
    }

    given("a non empty stream (using a byte array)") {

        val byteArray = randomByteArray()
        val stream = { ByteArrayInputStream(byteArray) }

        it("has the same content as another stream using a copy of the byte array") {
            val copyArray = byteArray.copyOf()
            val otherStream = ByteArrayInputStream(copyArray)

            assert( stream() ).hasSameContentAs( otherStream )
        }

        it("has the same content as another stream using a copy of the byte array (using hasNotSameContentAs)") {
            val copyArray = byteArray.copyOf()
            val otherStream = ByteArrayInputStream(copyArray)

            assertThatThrownBy {
                assert( stream() ).hasNotSameContentAs( otherStream )
            }.hasMessage("expected stream not to be equal, but they were equal")
        }

        it("has not the same content as another stream using another byte array") {
            val otherStream = ByteArrayInputStream(randomByteArray())

            assertThatThrownBy {
                assert( stream() ).hasSameContentAs( otherStream )
            }.hasMessageStartingWith("expected to have the same size, ")
        }

        it("has not the same content as another stream using another byte array (using hasNotSameContentAs)") {
            val otherStream = ByteArrayInputStream(randomByteArray())

            assert( stream() ).hasNotSameContentAs( otherStream )
        }
    }
})


internal fun emptyStream(): InputStream {
    return ByteArrayInputStream(ByteArray(0))
}

internal fun nonEmptyStream(): InputStream {
    return ByteArrayInputStream(randomByteArray())
}

internal fun randomByteArray(): ByteArray {
    val rnd = Random()
    val array = ByteArray(rnd.nextInt(100) + 1)
    rnd.nextBytes(array)
    return array
}
