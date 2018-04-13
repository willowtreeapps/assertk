package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class JavaAnyTest {
    val p: String = JavaAnyTest::class.java.name
    val subject = BasicObject("test")

    @Test fun extracts_jClass() {
        assertEquals(BasicObject::class.java, assert(subject as TestObject).jClass().actual)
    }

    @Test fun isInstanceOf_jclass_same_class_passes() {
        assert(subject).isInstanceOf(BasicObject::class.java)
    }

    @Test fun isInstanceOf_jclass_parent_class_passes() {
        assert(subject).isInstanceOf(TestObject::class.java)
    }

    @Test fun isInstanceOf_jclass_different_class_fails() {
        val error = assertFails {
            assert(subject).isInstanceOf(DifferentObject::class.java)
        }
        assertEquals("expected to be instance of:<$p\$DifferentObject> but had class:<$p\$BasicObject>", error.message)
    }

    @Test fun isNotInstanceOf_jclass_different_class_passess() {
        assert(subject).isNotInstanceOf(DifferentObject::class.java)
    }

    @Test fun isNotInstanceOf_jclass_same_class_fails() {
        val error = assertFails {
            assert(subject).isNotInstanceOf(BasicObject::class.java)
        }
        assertEquals("expected to not be instance of:<$p\$BasicObject>", error.message)
    }

    @Test fun isNotInstanceOf_jclass_parent_class_fails() {
        val error = assertFails {
            assert(subject).isNotInstanceOf(TestObject::class.java)
        }
        assertEquals("expected to not be instance of:<$p\$TestObject>", error.message)
    }

    @Test fun prop_callable_extract_prop_passes() {
        assert(subject).prop(BasicObject::str).isEqualTo("test")
    }

    @Test fun prop_callable_extract_prop_includes_name_in_failure_message() {
        val error = assertFails {
            assert(subject).prop(BasicObject::str).isEmpty()
        }
        assertEquals("expected [str] to be empty but was:<\"test\"> (test)", error.message)
    }

    open class TestObject

    class BasicObject(
        val str: String,
        val int: Int = 42,
        val double: Double = 3.14
    ) : TestObject() {
        override fun toString(): String = str

        override fun equals(other: Any?): Boolean =
            (other is BasicObject) && (str == other.str && int == other.int && double == other.double)

        override fun hashCode(): Int = 42
    }

    class DifferentObject : TestObject()
}

