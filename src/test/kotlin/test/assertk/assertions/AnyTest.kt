package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class AnyTest {
    val p = AnyTest::class.java.`package`.name

    @Nested inner class `basic object` {
        val subject = BasicObject("test")

        @Nested inner class `props` {
            @Test fun `extracts kClass()`() {
                assertEquals(BasicObject::class, assert(subject as TestObject).kClass().actual)
            }

            @Test fun `extracts jClass()`() {
                assertEquals(BasicObject::class.java, assert(subject as TestObject).jClass().actual)
            }

            @Test fun `extracts toStringFun()`() {
                assertEquals("test", assert(subject).toStringFun().actual)
            }

            @Test fun `extracts hashCodeFun()`() {
                assertEquals(42, assert(subject).hashCodeFun().actual)
            }
        }

        @Nested inner class `isEqualTo()` {
            val equal = BasicObject("test")
            val nonEqual = BasicObject("not test")

            @Test fun `equal objects passes`() {
                assert(subject).isEqualTo(equal)
            }

            @Test fun `non-equal objects fails`() {
                val error = assertFails {
                    assert(subject).isEqualTo(nonEqual)
                }
                assertEquals("expected:<[not ]test> but was:<[]test>", error.message)
            }

            @Test fun `failures have a stacktrace that starts at this test`() {
                val importantStacktrace = assertFails {
                    assert(subject).isEqualTo(nonEqual)
                }.stackTrace.dropWhile { it.className.startsWith("or.assertj") }

                assertTrue(importantStacktrace[0].toString().contains("test.assertk.assertions.AnyTest"))
            }
        }

        @Nested inner class `isNotEqualTo()` {
            val equal = BasicObject("test")
            val nonEqual = BasicObject("not test")

            @Test fun `non-equal objects passes`() {
                assert(subject).isNotEqualTo(nonEqual)
            }

            @Test fun `equal objects fails`() {
                val error = assertFails {
                    assert(subject).isNotEqualTo(equal)
                }
                assertEquals("expected to not be equal to:<test>", error.message)
            }
        }

        @Nested inner class `isSameAs()` {
            val nonSame = BasicObject("test")

            @Test fun `same objects passes`() {
                assert(subject).isSameAs(subject)
            }

            @Test fun `different objects fails`() {
                val error = assertFails("") {
                    assert(subject).isSameAs(nonSame)
                }
                assertEquals("expected:<test> and:<test> to refer to the same object", error.message)
            }
        }

        @Nested inner class `isNotSameAs()` {
            val nonSame = BasicObject("test")

            @Test fun `non-same objects passes`() {
                assert(subject).isNotSameAs(nonSame)
            }

            @Test fun `same objects fails`() {
                val error = assertFails {
                    assert(subject).isNotSameAs(subject)
                }
                assertEquals("expected:<test> to not refer to the same object", error.message)
            }
        }

        @Nested inner class `isInstanceOf(kclass)` {
            @Test fun `same class passes`() {
                assert(subject).isInstanceOf(BasicObject::class)
            }

            @Test fun `parent class passes`() {
                assert(subject).isInstanceOf(TestObject::class)
            }

            @Test fun `different class fails`() {
                val error = assertFails {
                    assert(subject).isInstanceOf(DifferentObject::class)
                }
                assertEquals("expected to be instance of:<$p.DifferentObject> but had class:<$p.BasicObject>", error.message)
            }
        }

        @Nested inner class `isInstanceOf(jclass)` {
            @Test fun `same class passes`() {
                assert(subject).isInstanceOf(BasicObject::class.java)
            }

            @Test fun `parent class passes`() {
                assert(subject).isInstanceOf(TestObject::class.java)
            }

            @Test fun `different class fails`() {
                val error = assertFails {
                    assert(subject).isInstanceOf(DifferentObject::class.java)
                }
                assertEquals("expected to be instance of:<$p.DifferentObject> but had class:<$p.BasicObject>", error.message)
            }
        }

        @Nested inner class `isNotInstanceOf(kclass)` {
            @Test fun `different class passes`() {
                assert(subject).isNotInstanceOf(DifferentObject::class)
            }

            @Test fun `same class fails`() {
                val error = assertFails {
                    assert(subject).isNotInstanceOf(BasicObject::class)
                }
                assertEquals("expected to not be instance of:<$p.BasicObject>", error.message)
            }

            @Test fun `parent class fails`() {
                val error = assertFails {
                    assert(subject).isNotInstanceOf(TestObject::class)
                }
                assertEquals("expected to not be instance of:<$p.TestObject>", error.message)
            }
        }

        @Nested inner class `isNotInstanceOf(jclass)` {
            @Test fun `different class passess`() {
                assert(subject).isNotInstanceOf(DifferentObject::class.java)
            }

            @Test fun `same class fails`() {
                val error = assertFails {
                    assert(subject).isNotInstanceOf(BasicObject::class.java)
                }
                assertEquals("expected to not be instance of:<$p.BasicObject>", error.message)
            }

            @Test fun `parent class fails`() {
                val error = assertFails {
                    assert(subject).isNotInstanceOf(TestObject::class.java)
                }
                assertEquals("expected to not be instance of:<$p.TestObject>", error.message)
            }
        }

        @Nested inner class `isIn()` {
            val isIn = BasicObject("test")
            val isOut1 = BasicObject("not test1")
            val isOut2 = BasicObject("not test2")

            @Test fun `one equal item passes`() {
                assert(subject).isIn(isIn)
            }

            @Test fun `one non-equal item fails`() {
                val error = assertFails {
                    assert(subject).isIn(isOut1)
                }
                assertEquals("expected:<[not test1]> to contain:<test>", error.message)
            }

            @Test fun `one equal item in may passes`() {
                assert(subject).isIn(isOut1, isIn, isOut2)
            }

            @Test fun `no equal items in may fails`() {
                val error = assertFails {
                    assert(subject).isIn(isOut1, isOut2)
                }
                assertEquals("expected:<[not test1, not test2]> to contain:<test>", error.message)
            }
        }

        @Nested inner class `isNotIn()` {
            val isIn = BasicObject("test")
            val isOut1 = BasicObject("not test1")
            val isOut2 = BasicObject("not test2")

            @Test fun `one non-equal item passes`() {
                assert(subject).isNotIn(isOut1)
            }

            @Test fun `one equal item fails`() {
                val error = assertFails {
                    assert(subject).isNotIn(isIn)
                }
                assertEquals("expected:<[test]> to not contain:<test>", error.message)
            }

            @Test fun `no equal items in many passes`() {
                assert(subject).isNotIn(isOut1, isOut2)
            }

            @Test fun `one equal item in many fails`() {
                val error = assertFails {
                    assert(subject).isNotIn(isOut1, isIn, isOut2)
                }
                assertEquals("expected:<[not test1, test, not test2]> to not contain:<test>", error.message)
            }
        }

        @Nested inner class `prop(name extract)` {
            @Test fun `extract prop passes`() {
                assert(subject).prop("str") { it.str }.isEqualTo("test")
            }

            @Test fun `extract prop includes name in failure message`() {
                val error = assertFails {
                    assert(subject).prop("str") { it.str }.isEmpty()
                }
                assertEquals("expected [str] to be empty but was:<\"test\"> (test)", error.message)
            }
        }

        @Nested inner class `prop(callable)` {
            @Test fun `extract prop passes`() {
                assert(subject).prop(BasicObject::str).isEqualTo("test")
            }

            @Test fun `extract prop includes name in failure message`() {
                val error = assertFails {
                    assert(subject).prop(BasicObject::str).isEmpty()
                }
                assertEquals("expected [str] to be empty but was:<\"test\"> (test)", error.message)
            }
        }
    }

    @Nested inner class `nullable object` {
        val subject: BasicObject? = BasicObject("test")

        @Nested inner class `isNull()` {
            @Test fun `null passes`() {
                assert(null as String?).isNull()
            }

            @Test fun `non-null fails`() {
                val error = assertFails {
                    assert(subject).isNull()
                }
                assertEquals("expected to be null but was:<test>", error.message)
            }
        }

        @Nested inner class `isNotNull()` {
            val unequal = BasicObject("not test")

            @Test fun `non-null passes`() {
                assert(subject).isNotNull()
            }

            @Test fun `null fails`() {
                val error = assertFails {
                    assert(null as String?).isNotNull()
                }
                assertEquals("expected to not be null", error.message)
            }

            @Test fun `non-null and equal object passes`() {
                assert(subject).isNotNull { it.isEqualTo(subject) }
            }

            @Test fun `non-null and non-equal object fails`() {
                val error = assertFails {
                    assert(subject).isNotNull { it.isEqualTo(unequal) }
                }
                assertEquals("expected:<[not ]test> but was:<[]test>", error.message)
            }

            @Test fun `null and equal object fails`() {
                val error = assertFails {
                    assert(null as String?).isNotNull { it.isEqualTo(null) }
                }
                assertEquals("expected to not be null", error.message)
            }
        }
    }
}

open class TestObject

class BasicObject(val str: String) : TestObject() {
    override fun toString(): String = str
    override fun equals(other: Any?): Boolean = (other is BasicObject) && (str == other.str)
    override fun hashCode(): Int = 42
}

class DifferentObject : TestObject()
