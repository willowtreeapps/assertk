package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class AnyTest {
    val subject = BasicObject("test")
    val nullableSubject: BasicObject? = BasicObject("test")

    @Test fun extracts_kClass() {
        assertEquals(BasicObject::class, assert(subject as TestObject).kClass().actual)
    }

    @Test fun extracts_toStringFun() {
        assertEquals("test", assert(subject).toStringFun().actual)
    }

    @Test fun extracts_hashCodeFun() {
        assertEquals(42, assert(subject).hashCodeFun().actual)
    }

    @Test fun isEqualTo_equal_objects_passes() {
        val equal = BasicObject("test")
        assert(subject).isEqualTo(equal)
    }

    @Test fun isEqualTo_non_equal_objects_fails() {
        val nonEqual = BasicObject("not test")
        val error = assertFails {
            assert(subject).isEqualTo(nonEqual)
        }
        assertEquals("expected:<[not ]test> but was:<[]test>", error.message)
    }

    @Test fun isNotEqualTo_non_equal_objects_passes() {
        val nonEqual = BasicObject("not test")
        assert(subject).isNotEqualTo(nonEqual)
    }

    @Test fun isNotEqualTo_equal_objects_fails() {
        val equal = BasicObject("test")
        val error = assertFails {
            assert(subject).isNotEqualTo(equal)
        }
        assertEquals("expected to not be equal to:<test>", error.message)
    }

    @Test fun isSameAs_same_objects_passes() {
        assert(subject).isSameAs(subject)
    }

    @Test fun isSameAs_different_objects_fails() {
        val nonSame = BasicObject("test")
        val error = assertFails("") {
            assert(subject).isSameAs(nonSame)
        }
        assertEquals("expected:<test> and:<test> to refer to the same object", error.message)
    }

    @Test fun isNotSameAs_non_same_objects_passes() {
        val nonSame = BasicObject("test")
        assert(subject).isNotSameAs(nonSame)
    }

    @Test fun isNotSameAs_same_objects_fails() {
        val error = assertFails {
            assert(subject).isNotSameAs(subject)
        }
        assertEquals("expected:<test> to not refer to the same object", error.message)
    }

    @Test fun isIn_one_equal_item_passes() {
        val isIn = BasicObject("test")
        assert(subject).isIn(isIn)
    }

    @Test fun isIn_one_non_equal_item_fails() {
        val isOut1 = BasicObject("not test1")
        val error = assertFails {
            assert(subject).isIn(isOut1)
        }
        assertEquals("expected:<[not test1]> to contain:<test>", error.message)
    }

    @Test fun isIn_one_equal_item_in_may_passes() {
        val isOut1 = BasicObject("not test1")
        val isIn = BasicObject("test")
        val isOut2 = BasicObject("not test2")
        assert(subject).isIn(isOut1, isIn, isOut2)
    }

    @Test fun isIn_no_equal_items_in_may_fails() {
        val isOut1 = BasicObject("not test1")
        val isOut2 = BasicObject("not test2")
        val error = assertFails {
            assert(subject).isIn(isOut1, isOut2)
        }
        assertEquals("expected:<[not test1, not test2]> to contain:<test>", error.message)
    }

    @Test fun isNotIn_one_non_equal_item_passes() {
        val isOut1 = BasicObject("not test1")
        assert(subject).isNotIn(isOut1)
    }

    @Test fun isNotIn_one_equal_item_fails() {
        val isIn = BasicObject("test")
        val error = assertFails {
            assert(subject).isNotIn(isIn)
        }
        assertEquals("expected:<[test]> to not contain:<test>", error.message)
    }

    @Test fun isNotIn_no_equal_items_in_many_passes() {
        val isOut1 = BasicObject("not test1")
        val isOut2 = BasicObject("not test2")
        assert(subject).isNotIn(isOut1, isOut2)
    }

    @Test fun isNotIn_one_equal_item_in_many_fails() {
        val isOut1 = BasicObject("not test1")
        val isIn = BasicObject("test")
        val isOut2 = BasicObject("not test2")
        val error = assertFails {
            assert(subject).isNotIn(isOut1, isIn, isOut2)
        }
        assertEquals("expected:<[not test1, test, not test2]> to not contain:<test>", error.message)
    }

    private val testObject = BasicObject("test", 99, 3.14)

    @Test fun isEqualToWithGivenProperties_regular_equals_fail() {
        assertFails {
            assert(subject).isEqualTo(testObject)
        }
    }

    @Test fun isEqualToWithGivenProperties_extract_prop_passes() {
        assert(subject).isEqualToWithGivenProperties(
            testObject,
            BasicObject::str,
            BasicObject::double
        )
    }

    @Test fun isEqualToWithGivenProperties_extract_prop_includes_name_in_failure_message() {
        val error = assertFails {
            assert(subject).isEqualToWithGivenProperties(testObject, BasicObject::int)
        }
        assertEquals("expected [int]:<[99]> but was:<[42]> (test)", error.message)
    }


    @Test fun prop_passes() {
        assert(subject).prop("str") { it.str }.isEqualTo("test")
    }

    @Test fun prop_includes_name_in_failure_message() {
        val error = assertFails {
            assert(subject).prop("str") { it.str }.isEmpty()
        }
        assertEquals("expected [str] to be empty but was:<\"test\"> (test)", error.message)
    }

    @Test fun nested_prop_include_names_in_failure_message() {
        val error = assertFails {
            assert(subject).prop("other") { it.other }.prop("str") { it?.str }.isNotNull()
        }
        assertEquals("expected [other.str] to not be null (test)", error.message)
    }

    @Test fun isNull_null_passes() {
        assert(null as String?).isNull()
    }

    @Test fun isNull_non_null_fails() {
        val error = assertFails {
            assert(nullableSubject).isNull()
        }
        assertEquals("expected to be null but was:<test>", error.message)
    }

    @Test fun isNotNull_non_null_passes() {
        assert(nullableSubject).isNotNull()
    }

    @Test fun isNotNull_null_fails() {
        val error = assertFails {
            assert(null as String?).isNotNull()
        }
        assertEquals("expected to not be null", error.message)
    }

    @Test fun isNotNull_non_null_and_equal_object_passes() {
        assert(nullableSubject).isNotNull { isEqualTo(subject) }
    }

    @Test fun isNotNull_non_null_and_non_equal_object_fails() {
        val unequal = BasicObject("not test")
        val error = assertFails {
            assert(nullableSubject).isNotNull { isEqualTo(unequal) }
        }
        assertEquals("expected:<[not ]test> but was:<[]test>", error.message)
    }

    @Test fun isNotNull_null_and_equal_object_fails() {
        val error = assertFails {
            assert(null as String?).isNotNull { isEqualTo(null) }
        }
        assertEquals("expected to not be null", error.message)
    }

    @Test fun hasClass_same_class_passes() {
        assert(subject).hasClass(BasicObject::class)
    }

    @Test fun hasClass_parent_class_fails() {
        val error = assertFails {
            assert(subject).hasClass(TestObject::class)
        }
        assertEquals(
            "expected to have class:<${TestObject::class}> but was:<${BasicObject::class}>",
            error.message
        )
    }

    @Test fun doesNotHaveClass_parent_class_passes() {
        assert(subject).doesNotHaveClass(TestObject::class)
    }

    @Test fun doesNotHaveClass_same_class_fails() {
        val error = assertFails {
            assert(subject).doesNotHaveClass(BasicObject::class)
        }
        assertEquals("expected to not have class:<${BasicObject::class}>", error.message)
    }

    @Test fun isInstanceOf_kclass_same_class_passes() {
        assert(subject as TestObject).isInstanceOf(BasicObject::class)
    }

    @Test fun isInstanceOf_kclass_parent_class_passes() {
        assert(subject).isInstanceOf(TestObject::class)
    }

    @Test fun isInstanceOf_kclass_different_class_fails() {
        val error = assertFails {
            assert(subject).isInstanceOf(DifferentObject::class)
        }
        assertEquals(
            "expected to be instance of:<${DifferentObject::class}> but had class:<${BasicObject::class}>",
            error.message
        )
    }

    @Test fun isInstanceOf_kclass_run_block_when_passes() {
        val error = assertFails {
            assert(subject as TestObject).isInstanceOf(BasicObject::class) {
                prop("str", BasicObject::str).isEqualTo("wrong")
            }
        }
        assertEquals("expected [str]:<\"[wrong]\"> but was:<\"[test]\"> (test)", error.message)
    }

    @Test fun isInstanceOf_kclass_doesnt_run_block_when_fails() {
        val error = assertFails {
            assert(subject as TestObject).isInstanceOf(DifferentObject::class) {
                isNull()
            }
        }
        assertEquals(
            "expected to be instance of:<${DifferentObject::class}> but had class:<${BasicObject::class}>",
            error.message
        )
    }

    @Test fun isNotInstanceOf_kclass_different_class_passes() {
        assert(subject).isNotInstanceOf(DifferentObject::class)
    }

    @Test fun isNotInstanceOf_kclass_same_class_fails() {
        val error = assertFails {
            assert(subject).isNotInstanceOf(BasicObject::class)
        }
        assertEquals("expected to not be instance of:<${BasicObject::class}>", error.message)
    }

    @Test fun isNotInstanceOf_kclass_parent_class_fails() {
        val error = assertFails {
            assert(subject).isNotInstanceOf(TestObject::class)
        }
        assertEquals("expected to not be instance of:<${TestObject::class}>", error.message)
    }

    companion object {
        open class TestObject

        class BasicObject(
            val str: String,
            val int: Int = 42,
            val double: Double = 3.14,
            val other: BasicObject? = null
        ) : TestObject() {
            override fun toString(): String = str

            override fun equals(other: Any?): Boolean =
                (other is BasicObject) && (str == other.str && int == other.int && double == other.double && this.other == other.other)

            override fun hashCode(): Int = 42
        }

        class DifferentObject : TestObject()
    }
}
