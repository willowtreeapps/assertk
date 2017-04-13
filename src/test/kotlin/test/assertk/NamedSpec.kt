package test.assertk

import assertk.assert
import assertk.assertions.isEqualTo
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class NamedSpec : Spek({
    class BasicObject(val str: String) {
        override fun toString(): String = str
    }

    given("an assert") {
        val subject = BasicObject("yes")

        on("nothing") {
            it("should fail with just the default error message") {
                Assertions.assertThatThrownBy {
                    assert(subject).isEqualTo(BasicObject("no"))
                }.hasMessage("expected:<[no]> but was:<[yes]>")
            }
        }

        on("named(String)") {
            it("should fail prefixed with the 'named' message") {
                Assertions.assertThatThrownBy {
                    assert("test", subject).isEqualTo(BasicObject("no"))
                }.hasMessage("expected [test]:<[no]> but was:<[yes]>")
            }
        }
    }
})