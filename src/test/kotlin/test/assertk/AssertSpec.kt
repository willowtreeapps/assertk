package test.assertk

import assertk.Assert
import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*

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
                }.hasMessage("expected to not be equal to:<test>")
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
                assert(subject as TestObject).kClass().isEqualTo(BasicObject::class)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).kClass().isEqualTo(DifferentObject::class)
                }.hasMessageStartingWith("expected [class]:")
                        .hasMessageContaining("[Different]Object> but was:")
                        .hasMessageContaining("[Basic]Object>")
            }
        }

        on("hasClass(Class)") {
            it("should pass a successful test") {
                assert(subject as TestObject).jClass().isEqualTo(BasicObject::class.java)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).jClass().isEqualTo(DifferentObject::class.java)
                }.hasMessageStartingWith("expected [class]:")
                        .hasMessageContaining("[Different]Object> but was:")
                        .hasMessageContaining("[Basic]Object>")
            }
        }

        on("doesNotHaveClass(KClass)") {
            it("should pass a successful test") {
                assert(subject as TestObject).kClass().isNotEqualTo(DifferentObject::class)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).kClass().isNotEqualTo(BasicObject::class)
                }.hasMessage("expected [class] to not be equal to:<class $ASSERT_SPEC\$BasicObject> (test)")
            }
        }

        on("doesNotHaveClass(Class)") {
            it("should pass a successful test") {
                assert(subject as TestObject).jClass().isNotEqualTo(DifferentObject::class.java)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject as TestObject).jClass().isNotEqualTo(BasicObject::class.java)
                }.hasMessage("expected [class] to not be equal to:<$ASSERT_SPEC\$BasicObject> (test)")
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
                assert(subject).toStringFun().isEqualTo("test")
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).toStringFun().isEqualTo("not test")
                }.hasMessage("expected [toString]:<\"[not ]test\"> but was:<\"[]test\"> (test)")
            }
        }

        on("hasHashCode()") {
            it("should pass a successful test") {
                assert(subject).hashCodeFun().isEqualTo(42)
            }

            it("should fail an unsuccessful test") {
                Assertions.assertThatThrownBy {
                    assert(subject).hashCodeFun().isEqualTo(9)
                }.hasMessage("expected [hashCode]:<[9]> but was:<[42]> (test)")
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
                }.hasMessage("expected to be null but was:<test>")
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
                assert(subject).isNotNull { it.isRelaxedEqualTo(subject) }
            }

            it("should fail an unsuccessful test because of an additional assertion") {
                Assertions.assertThatThrownBy {
                    assert(subject).isNotNull { it.isEqualTo(unequal) }
                }.hasMessage("expected:<[not ]test> but was:<[]test>")
            }

            it("should fail an unsuccessful test because of null but not run additional assertion") {
                Assertions.assertThatThrownBy {
                    assert(null as String?).isNotNull { it.isRelaxedEqualTo(unequal) }
                }.hasMessage("expected to not be null")
            }

            it("shiud handle equality on objects of different types") {
                val p2=Point2(2,3)
                val p3=Point3(2,3,0)
                assert(p3).isRelaxedEqualTo(p2)
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

    class Point2(val x: Int, val y: Int)

    class Point3(val x: Int, val y: Int, val z: Int) {
        override fun equals(other: Any?): Boolean {
            return when (other) {
                is Point3 -> (x == other.x && y == other.y && z == other.z)
                is Point2 -> (x == other.x && y == other.y && z == 0 /*or whatever your origin is*/)
                else -> false
            }
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            result = 31 * result + z
            return result
        }


    }
}

