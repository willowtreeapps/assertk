package test.me.tatarka.assertk

import me.tatarka.assertk.assertions.isEqualTo
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

class NamedSpec : Spek({
    class BasicObject(val str: String) {
        override fun toString(): String = str
    }

    given("an assert") {
        val test = me.tatarka.assertk.assert(BasicObject("yes"))

        on("nothing") {
            it("should fail with just the default error message") {
                Assertions.assertThatThrownBy {
                    test.isEqualTo(BasicObject("no"))
                }.hasMessage("expected:<[no]> but was:<[yes]>")
            }
        }

        on("named(String)") {
            it("should fail prefixed with the 'named' message") {
                Assertions.assertThatThrownBy {
                    test.named("test").isEqualTo(BasicObject("no"))
                }.hasMessage("expected [test]:<[no]> but was:<[yes]>")
            }
        }
    }
})