package me.tatarka.assertk

import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

class AssertThrownSpec : Spek({
    given("a thrown exception") {

        val subject = TestException()

        on("catch") {
            it("should catch and return a thrown exception") {
                Assertions.assertThat(catch { throw subject })
                        .isInstanceOf(TestException::class.java)
                        .hasMessage("test")
            }

            it("should return null when no exception is thrown") {
                Assertions.assertThat(catch {}).isNull()
            }
        }
    }
}) {
    class TestException : Exception("test")
}

