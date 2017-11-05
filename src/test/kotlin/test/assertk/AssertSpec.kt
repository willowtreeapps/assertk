package test.assertk

import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class AssertSpec : Spek({
    val ASSERT_SPEC = AssertSpec::class.qualifiedName

    given("a basic object") {

        val subject = BasicObject("test")

        on("isEqualTo()") {
            val equal = BasicObject("test")
            val nonEqual = BasicObject("not test")

            it("should pass a successful test") {
                assert(subject).isEqualTo(equal)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).isEqualTo(nonEqual)
                }.hasMessage("expected:<[not ]test> but was:<[]test>")
            }

            it("should fail with a stacktrace that starts at this test") {
                val error = Assertions.catchThrowable {
                    assert(subject).isEqualTo(nonEqual)
                }
                val importantStacktrace = error.stackTrace
                        .dropWhile { it.className.startsWith("or.assertj") }

                Assertions.assertThat(importantStacktrace[0].toString()).contains("test.assertk.AssertSpec")
            }
        }

        on("isNotEqualTo()") {
            val equal = BasicObject("test")
            val nonEqual = BasicObject("not test")

            it("should pass a successful test") {
                assert(subject).isNotEqualTo(nonEqual)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).isNotEqualTo(equal)
                }.hasMessage("expected:<test> not to be equal to:<test>")
            }
        }

        on("isSameAs()") {
            val nonSame = BasicObject("test")

            it("should pass a successful test") {
                assert(subject).isSameAs(subject)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).isSameAs(nonSame)
                }.hasMessage("expected:<test> and:<test> to refer to the same object")
            }
        }

        on("isNotSameAs()") {
            val nonSame = BasicObject("test")

            it("should pass a successful test") {
                assert(subject).isNotSameAs(nonSame)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).isNotSameAs(subject)
                }.hasMessage("expected:<test> to not refer to the same object")
            }
        }

        on("hasClass(KClass)") {
            it("should pass a successful test") {
                assert(subject as TestObject).hasClass(BasicObject::class)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).hasClass(DifferentObject::class)
                }.hasMessage("expected to have class:<$ASSERT_SPEC\$DifferentObject> but was:<$ASSERT_SPEC\$BasicObject>")
            }
        }

        on("hasClass(Class)") {
            it("should pass a successful test") {
                assert(subject as TestObject).hasClass(BasicObject::class.java)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).hasClass(DifferentObject::class.java)
                }.hasMessage("expected to have class:<$ASSERT_SPEC\$DifferentObject> but was:<$ASSERT_SPEC\$BasicObject>")
            }
        }

        on("doesNotHaveClass(KClass)") {
            it("should pass a successful test") {
                assert(subject as TestObject).doesNotHaveClass(DifferentObject::class)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).doesNotHaveClass(BasicObject::class)
                }.hasMessage("expected to not have class:<$ASSERT_SPEC\$BasicObject>")
            }
        }

        on("doesNotHaveClass(Class)") {
            it("should pass a successful test") {
                assert(subject as TestObject).doesNotHaveClass(DifferentObject::class.java)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).doesNotHaveClass(BasicObject::class.java)
                }.hasMessage("expected to not have class:<$ASSERT_SPEC\$BasicObject>")
            }
        }

        on("isInstanceOf(KClass)") {
            it("should pass a successful test") {
                assert(subject as TestObject).isInstanceOf(BasicObject::class)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).isInstanceOf(DifferentObject::class)
                }.hasMessage("expected to be instance of:<$ASSERT_SPEC\$DifferentObject> but had class:<$ASSERT_SPEC\$BasicObject>")
            }
        }

        on("isInstanceOf(Class)") {
            it("should pass a successful test") {
                assert(subject as TestObject).isInstanceOf(BasicObject::class.java)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).isInstanceOf(DifferentObject::class.java)
                }.hasMessage("expected to be instance of:<$ASSERT_SPEC\$DifferentObject> but had class:<$ASSERT_SPEC\$BasicObject>")
            }
        }

        on("isNotInstanceOf(KClass)") {
            it("should pass a successful test") {
                assert(subject as TestObject).isNotInstanceOf(DifferentObject::class)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).isNotInstanceOf(BasicObject::class)
                }.hasMessage("expected to not be instance of:<$ASSERT_SPEC\$BasicObject>")
            }
        }

        on("isNotInstanceOf(Class)") {
            it("should pass a successful test") {
                assert(subject as TestObject).isNotInstanceOf(DifferentObject::class.java)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).isNotInstanceOf(BasicObject::class.java)
                }.hasMessage("expected to not be instance of:<$ASSERT_SPEC\$BasicObject>")
            }
        }

        on("isIn()") {
            val isIn = BasicObject("test")
            val isOut1 = BasicObject("not test1")
            val isOut2 = BasicObject("not test2")

            it("should pass a successful test with one item") {
                assert(subject).isIn(isIn)
            }

            it("should fail an unsuccessful test with one item") {
                Assertions.assertThatThrownBy {
                    assert(subject).isIn(isOut1)
                }.hasMessage("expected:<[not test1]> to contain:<test>")
            }

            it("should pass a successful test with two items") {
                assert(subject).isIn(isOut1, isIn)
            }

            it("should fail an unsuccessful test with two items") {
                Assertions.assertThatThrownBy {
                    assert(subject).isIn(isOut1, isOut2)
                }.hasMessage("expected:<[not test1, not test2]> to contain:<test>")

            }
        }

        on("isNotIn()") {
            val isIn = BasicObject("test")
            val isOut1 = BasicObject("not test1")
            val isOut2 = BasicObject("not test2")

            it("should pass a successful test with one item") {
                assert(subject).isNotIn(isOut1)
            }

            it("should fail an unsuccessful test with one item") {
                Assertions.assertThatThrownBy {
                    assert(subject).isNotIn(isIn)
                }.hasMessage("expected:<[test]> to not contain:<test>")
            }

            it("should pass a successful test with two items") {
                assert(subject).isNotIn(isOut1, isOut2)
            }

            it("should fail an unsuccessful test with two items") {
                Assertions.assertThatThrownBy {
                    assert(subject).isNotIn(isOut1, isIn)
                }.hasMessage("expected:<[not test1, test]> to not contain:<test>")
            }
        }

        on("hasToString()") {
            it("should pass a successful test") {
                assert(subject).hasToString("test")
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).hasToString("not test")
                }.hasMessage("expected toString() to be:<\"not test\"> but was:<\"test\">")
            }
        }

        on("hasHashCode()") {
            it("should pass a successful test") {
                assert(subject).hasHashCode(42)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).hasHashCode(9)
                }.hasMessage("expected hashCode() to be:<9> but was:<42>")
            }
        }
    }

    given("a nullable object") {

        val subject: BasicObject? = BasicObject("test")

        on("isNull()") {
            it("should pass a successful test") {
                assert(null as String?).isNull()
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).isNull()
                }.hasMessage("expected null but was:<test>")
            }
        }

        on("isNotNull()") {
            val unequal = BasicObject("not test")

            it("should pass a successful test") {
                assert(subject).isNotNull()
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(null as String?).isNotNull()
                }.hasMessage("expected to not be null")
            }

            it("should pass a successful test with an additional assertion") {
                assert(subject).isNotNull { it.isEqualTo(subject) }
            }

            it("should fail an unsuccessful test because of an additional assertion") {
                Assertions.assertThatThrownBy {
                    assert(subject).isNotNull { it.isEqualTo(unequal) }
                }.hasMessage("expected:<[not ]test> but was:<[]test>")
            }

            it("should fail an unsuccessful test because of null but not run additional assertion") {
                Assertions.assertThatThrownBy {
                    assert(null as String?).isNotNull { it.isEqualTo(unequal) }
                }.hasMessage("expected to not be null")
            }
        }
    }

}) {
    open class TestObject

    class BasicObject(val str: String) : TestObject() {
        override fun toString(): String = str
        override fun equals(other: Any?): Boolean = (other is BasicObject) && (str == other.str)
        override fun hashCode(): Int = 42
    }

    class DifferentObject : TestObject()
}

