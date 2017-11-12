package test.assertk

import assertk.assert
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.key
import assertk.assertions.prop
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class LensAssertSpec : Spek({

    given("a basic data object") {
        val subject = BasicDataObject("test", Nested(1))

        on("prop(fn, name)") {
            it("should pass a successful test") {
                assert(subject).prop("arg1") { it.arg1 }.isEqualTo("test")
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).prop("arg1") { it.arg1 }.isEqualTo("wrong")
                }.hasMessage("expected [arg1]:<\"[wrong]\"> but was:<\"[test]\"> (BasicDataObject(arg1=test, arg2=Nested(arg1=1)))")
            }

            it("should pass a successful nested prop test") {
                assert(subject).prop("arg2") { it.arg2 }.prop("arg1") { it.arg1 }.isEqualTo(1)
            }

            it("should fail an unsuccessful nested prop test") {
                Assertions.assertThatThrownBy {
                    assert(subject).prop("arg2") { it.arg2 }.prop("arg1") { it.arg1 }.isEqualTo(0)
                }.hasMessage("expected [arg2.arg1]:<[0]> but was:<[1]> (BasicDataObject(arg1=test, arg2=Nested(arg1=1)))")
            }
        }

        on("prop(callable)") {
            it("should pass a successful test") {
                assert(subject).prop(BasicDataObject::arg1).isEqualTo("test")
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).prop(BasicDataObject::arg1).isEqualTo("wrong")
                }.hasMessage("expected [arg1]:<\"[wrong]\"> but was:<\"[test]\"> (BasicDataObject(arg1=test, arg2=Nested(arg1=1)))")
            }

            it("should pass a successful nested prop test") {
                assert(subject).prop(BasicDataObject::arg2).prop(Nested::arg1).isEqualTo(1)
            }

            it("should fail an unsuccessful nested prop test") {
                Assertions.assertThatThrownBy {
                    assert(subject).prop(BasicDataObject::arg2).prop(Nested::arg1).isEqualTo(0)
                }.hasMessage("expected [arg2.arg1]:<[0]> but was:<[1]> (BasicDataObject(arg1=test, arg2=Nested(arg1=1)))")
            }
        }
    }

    given("a list of lists") {
        val subject = listOf(listOf("one"), listOf("two"))

        on("index()") {
            it("should pass a successful test") {
                assert(subject, name = "subject").index(0).isEqualTo(listOf("one"))
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject, name = "subject").index(0).isEqualTo(listOf("wrong"))
                }.hasMessage("expected [subject[0]]:<[\"[wrong]\"]> but was:<[\"[one]\"]> ([[\"one\"], [\"two\"]])")
            }

            it("should pass a successful nested list test") {
                assert(subject).index(1).index(0).isEqualTo("two")
            }

            it("should fail an unsuccessful nested prop test") {
                Assertions.assertThatThrownBy {
                    assert(subject, name = "subject").index(1).index(0).isEqualTo("wrong")
                }.hasMessage("expected [subject[1][0]]:<\"[wrong]\"> but was:<\"[two]\"> ([[\"one\"], [\"two\"]])")
            }
        }
    }

    given("a map of maps") {
        val subject = mapOf("one" to mapOf("two" to 2))

        on("key()") {
            it("should pass a successful test") {
                assert(subject, name = "subject").key("one").isEqualTo(mapOf("two" to 2))
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject, name = "subject").key("one").isEqualTo(mapOf("wrong" to 2))
                }.hasMessage("expected [subject[\"one\"]]:<{\"[wrong]\"=2}> but was:<{\"[two]\"=2}> ({\"one\"={\"two\"=2}})")
            }

            it("should pass a successful nested map test") {
                assert(subject).key("one").key("two").isEqualTo(2)
            }

            it("should fail an unsuccessful nested prop test") {
                Assertions.assertThatThrownBy {
                    assert(subject, name = "subject").key("one").key("two").isEqualTo(0)
                }.hasMessage("expected [subject[\"one\"][\"two\"]]:<[0]> but was:<[2]> ({\"one\"={\"two\"=2}})")
            }
        }
    }

    given("a complex structure") {
        val subject = mapOf("key" to listOf(BasicDataObject("test", Nested(1))))

        on("lookup") {
            it("should pass a successful test") {
                assert(subject, name = "subject")
                        .key("key")
                        .index(0)
                        .prop(BasicDataObject::arg2)
                        .prop(Nested::arg1)
                        .isEqualTo(1)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject, name = "subject")
                            .key("key")
                            .index(0)
                            .prop(BasicDataObject::arg2)
                            .prop(Nested::arg1)
                            .isEqualTo(0)
                }.hasMessage("expected [subject[\"key\"][0].arg2.arg1]:<[0]> but was:<[1]> ({\"key\"=[BasicDataObject(arg1=test, arg2=Nested(arg1=1))]})")
            }
        }
    }
}) {
    data class BasicDataObject(val arg1: String, val arg2: Nested)

    data class Nested(val arg1: Int)
}