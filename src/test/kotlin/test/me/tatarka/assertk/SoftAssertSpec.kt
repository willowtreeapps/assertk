package test.me.tatarka.assertk

import me.tatarka.assertk.assertions.hasLength
import me.tatarka.assertk.assertions.isEqualTo
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class SoftAssertSpec : Spek({
    given("a string") {
        val subject = "Test"

        on("multiple assertions") {
            it("should pass both successful tests") {
                me.tatarka.assertk.assert(subject) {
                    it.isEqualTo("Test")
                    it.hasLength(4)
                }
            }

            it("it should fail both failing tests") {
                Assertions.assertThatThrownBy {
                    me.tatarka.assertk.assert(subject) {
                        it.isEqualTo("wrong")
                        it.hasLength(0)
                    }
                }.hasMessage("""The following 2 assertions failed:
- expected:<"[wrong]"> but was:<"[Test]">
- expected [length]:<[0]> but was:<[4]>""")
            }
        }
    }
})
