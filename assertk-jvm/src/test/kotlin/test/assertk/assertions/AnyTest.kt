package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import test.assertk.assertions.AnyTest.BasicObject
import test.assertk.assertions.AnyTest.DifferentObject
import test.assertk.assertions.AnyTest.TestObject
import test.assertk.assertions.AnyTest.subject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

val p: String = AnyTest::class.java.name

class JavaAnyTest_basic_object {

    @Test
    fun props_extracts_jClass__() {
        assertEquals(BasicObject::class.java, assert(subject as TestObject).jClass().actual)
    }

    @Test
    fun isInstanceOf_jclass_same_class_passes() {
        assert(subject).isInstanceOf(BasicObject::class.java)
    }

    @Test
    fun isInstanceOf_jclass_parent_class_passes() {
        assert(subject).isInstanceOf(TestObject::class.java)
    }

    @Test
    fun isInstanceOf_jclass_different_class_fails() {
        val error = assertFails {
            assert(subject).isInstanceOf(DifferentObject::class.java)
        }
        assertEquals("expected to be instance of:<$p\$DifferentObject> but had class:<$p\$BasicObject>", error.message)
    }

    @Test
    fun isNotInstanceOf_jclass_different_class_passess() {
        assert(subject).isNotInstanceOf(DifferentObject::class.java)
    }

    @Test
    fun isNotInstanceOf_jclass_same_class_fails() {
        val error = assertFails {
            assert(subject).isNotInstanceOf(BasicObject::class.java)
        }
        assertEquals("expected to not be instance of:<$p\$BasicObject>", error.message)
    }

    @Test
    fun isNotInstanceOf_jclass_parent_class_fails() {
        val error = assertFails {
            assert(subject).isNotInstanceOf(TestObject::class.java)
        }
        assertEquals("expected to not be instance of:<$p\$TestObject>", error.message)
    }

    @Test
    fun prop_callable_extract_prop_passes() {
        assert(subject).prop(BasicObject::str).isEqualTo("test")
    }

    @Test
    fun prop_callable_extract_prop_includes_name_in_failure_message() {
        val error = assertFails {
            assert(subject).prop(BasicObject::str).isEmpty()
        }
        assertEquals("expected [str] to be empty but was:<\"test\"> (test)", error.message)
    }
}

