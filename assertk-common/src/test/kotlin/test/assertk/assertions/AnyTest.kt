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

class AnyTest_basicObject_props {

    @Test
    fun extracts_kClass() {
        assertEquals(BasicObject::class, assert(subject as TestObject).kClass().actual)
    }

    @Test
    fun extracts_toStringFun() {
        assertEquals("test", assert(subject).toStringFun().actual)
    }

    @Test
    fun extracts_hashCodeFun() {
        assertEquals(42, assert(subject).hashCodeFun().actual)
    }
}

class AnyTest_BasicObject_isEqualTo {
    val equal = BasicObject("test")
    val nonEqual = BasicObject("not test")

    @Test
    fun equal_objects_passes() {
        assert(subject).isEqualTo(equal)
    }

    @Test
    fun non_equal_objects_fails() {
        val error = assertFails {
            assert(subject).isEqualTo(nonEqual)
        }
        assertEquals("expected:<[not ]test> but was:<[]test>", error.message)
    }

}

class AnyTest_BasicObject_isNotEqualTo {
    val equal = BasicObject("test")
    val nonEqual = BasicObject("not test")

    @Test
    fun non_equal_objects_passes() {
        assert(subject).isNotEqualTo(nonEqual)
    }

    @Test
    fun equal_objects_fails() {
        val error = assertFails {
            assert(subject).isNotEqualTo(equal)
        }
        assertEquals("expected to not be equal to:<test>", error.message)
    }
}

class AnyTest_BasicObject_isSameAs {
    val nonSame = BasicObject("test")

    @Test
    fun same_objects_passes() {
        assert(subject).isSameAs(subject)
    }

    @Test
    fun different_objects_fails() {
        val error = assertFails("") {
            assert(subject).isSameAs(nonSame)
        }
        assertEquals("expected:<test> and:<test> to refer to the same object", error.message)
    }
}

class AnyTest_BasicObject_isNotSameAs {
    val nonSame = BasicObject("test")

    @Test
    fun non_same_objects_passes() {
        assert(subject).isNotSameAs(nonSame)
    }

    @Test
    fun same_objects_fails() {
        val error = assertFails {
            assert(subject).isNotSameAs(subject)
        }
        assertEquals("expected:<test> to not refer to the same object", error.message)
    }
}

class AnyTest_BasicObject_isIn {
    val isIn = BasicObject("test")
    val isOut1 = BasicObject("not test1")
    val isOut2 = BasicObject("not test2")

    @Test
    fun one_equal_item_passes() {
        assert(subject).isIn(isIn)
    }

    @Test
    fun one_non_equal_item_fails() {
        val error = assertFails {
            assert(subject).isIn(isOut1)
        }
        assertEquals("expected:<[not test1]> to contain:<test>", error.message)
    }

    @Test
    fun one_equal_item_in_may_passes() {
        assert(subject).isIn(isOut1, isIn, isOut2)
    }

    @Test
    fun no_equal_items_in_may_fails() {
        val error = assertFails {
            assert(subject).isIn(isOut1, isOut2)
        }
        assertEquals("expected:<[not test1, not test2]> to contain:<test>", error.message)
    }
}

class AnyTest_BasicObject_isNotIn {
    val isIn = BasicObject("test")
    val isOut1 = BasicObject("not test1")
    val isOut2 = BasicObject("not test2")

    @Test
    fun one_non_equal_item_passes() {
        assert(subject).isNotIn(isOut1)
    }

    @Test
    fun one_equal_item_fails() {
        val error = assertFails {
            assert(subject).isNotIn(isIn)
        }
        assertEquals("expected:<[test]> to not contain:<test>", error.message)
    }

    @Test
    fun no_equal_items_in_many_passes() {
        assert(subject).isNotIn(isOut1, isOut2)
    }

    @Test
    fun one_equal_item_in_many_fails() {
        val error = assertFails {
            assert(subject).isNotIn(isOut1, isIn, isOut2)
        }
        assertEquals("expected:<[not test1, test, not test2]> to not contain:<test>", error.message)
    }
}

class AnyTest_BasicObject_propNameExtract {
    @Test
    fun extract_prop_passes() {
        assert(subject).prop("str") { it.str }.isEqualTo("test")
    }

    @Test
    fun extract_prop_includes_name_in_failure_message() {
        val error = assertFails {
            assert(subject).prop("str") { it.str }.isEmpty()
        }
        assertEquals("expected [str] to be empty but was:<\"test\"> (test)", error.message)
    }
}

class AnyTest_NullableObject_isNull {
    @Test
    fun null_passes() {
        assert(null as String?).isNull()
    }

    @Test
    fun non_null_fails() {
        val error = assertFails {
            assert(subject).isNull()
        }
        assertEquals("expected to be null but was:<test>", error.message)
    }
}

class AnyTest_NullableObject_isNotNull {
    val unequal = BasicObject("not test")

    @Test
    fun non_null_passes() {
        assert(subject).isNotNull()
    }

    @Test
    fun null_fails() {
        val error = assertFails {
            assert(null as String?).isNotNull()
        }
        assertEquals("expected to not be null", error.message)
    }

    @Test
    fun non_null_and_equal_object_passes() {
        assert(subject).isNotNull { it.isEqualTo(subject) }
    }

    @Test
    fun non_null_and_non_equal_object_fails() {
        val error = assertFails {
            assert(subject).isNotNull { it.isEqualTo(unequal) }
        }
        assertEquals("expected:<[not ]test> but was:<[]test>", error.message)
    }

    @Test
    fun null_and_equal_object_fails() {
        val error = assertFails {
            assert(null as String?).isNotNull { it.isEqualTo(null) }
        }
        assertEquals("expected to not be null", error.message)
    }
}


class AnyTest_nullable_object {
    @Test
    fun isNull__null_passes() {
        assert(null as String?).isNull()
    }

    @Test
    fun isNull__non_null_fails() {
        val error = assertFails {
            assert(AnyTest.nullableSubject).isNull()
        }
        assertEquals("expected to be null but was:<test>", error.message)
    }

    @Test
    fun isNotNull__non_null_passes() {
        assert(AnyTest.nullableSubject).isNotNull()
    }

    @Test
    fun isNotNull__null_fails() {
        val error = assertFails {
            assert(null as String?).isNotNull()
        }
        assertEquals("expected to not be null", error.message)
    }

    @Test
    fun isNotNull__non_null_and_equal_object_passes() {
        assert(AnyTest.nullableSubject).isNotNull { it.isEqualTo(AnyTest.nullableSubject) }
    }

    @Test
    fun isNotNull__non_null_and_non_equal_object_fails() {
        val error = assertFails {
            assert(AnyTest.nullableSubject).isNotNull { it.isEqualTo(AnyTest.unequal) }
        }
        assertEquals("expected:<[not ]test> but was:<[]test>", error.message)
    }

    @Test
    fun isNotNull__null_and_equal_object_fails() {
        val error = assertFails {
            assert(null as String?).isNotNull { it.isEqualTo(null) }
        }
        assertEquals("expected to not be null", error.message)
    }
}

class AnyTest_basic_object {

    @Test
    fun isInstanceOf_kclass_same_class_passes() {
        assert(subject).isInstanceOf(BasicObject::class)
    }

    @Test
    fun isInstanceOf_kclass_parent_class_passes() {
        assert(subject).isInstanceOf(TestObject::class)
    }

    @Test
    fun isInstanceOf_kclass_different_class_fails() {
        val error = assertFails {
            assert(subject).isInstanceOf(AnyTest.DifferentObject::class)
        }
        assertEquals("expected to be instance of:<${DifferentObject::class}> but had class:<${BasicObject::class}>", error.message)
    }

    @Test
    fun isNotInstanceOf_kclass_different_class_passes() {
        assert(subject).isNotInstanceOf(AnyTest.DifferentObject::class)
    }

    @Test
    fun isNotInstanceOf_kclass_same_class_fails() {
        val error = assertFails {
            assert(subject).isNotInstanceOf(BasicObject::class)
        }
        assertEquals("expected to not be instance of:<${BasicObject::class}>", error.message)
    }

    @Test
    fun isNotInstanceOf_kclass_parent_class_fails() {
        val error = assertFails {
            assert(subject).isNotInstanceOf(TestObject::class)
        }
        assertEquals("expected to not be instance of:<${TestObject::class}>", error.message)
    }

}

object AnyTest {
    open class TestObject

    class BasicObject(val str: String) : TestObject() {
        override fun toString(): String = str
        override fun equals(other: Any?): Boolean = (other is BasicObject) && (str == other.str)
        override fun hashCode(): Int = 42
    }

    class DifferentObject : TestObject()

    val subject = BasicObject("test")

    val nullableSubject: BasicObject? = BasicObject("test")
    val unequal = BasicObject("not test")
}
