package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

class AnyTest {
    val subject = BasicObject("test")
    val nullableSubject: BasicObject? = BasicObject("test")

    @Test fun extracts_kClass() {
        assertEquals(BasicObject::class, assertThat(subject as TestObject).kClass().valueOrFail)
    }

    @Test fun extracts_toStringFun() {
        assertEquals("test", assertThat(subject).toStringFun().valueOrFail)
    }

    @Test fun extracts_hashCodeFun() {
        assertEquals(42, assertThat(subject).hashCodeFun().valueOrFail)
    }

    @Test fun isEqualTo_equal_objects_passes() {
        val equal = BasicObject("test")
        assertThat(subject).isEqualTo(equal)
    }

    @Test fun isEqualTo_non_equal_objects_fails() {
        val nonEqual = BasicObject("not test")
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isEqualTo(nonEqual)
        }
        assertEquals("expected:<[not ]test> but was:<[]test>", error.message)
    }

    @Test fun isEqualTo_will_compile_comparing_various_types() {
        assertThat(1).isEqualTo(1)
        assertThat(1 as Int?).isEqualTo(1)
        assertThat(1).isEqualTo(1 as Int?)
        val obj = DifferentObject()
        assertThat(obj).isEqualTo(obj)
        assertThat(obj as TestObject).isEqualTo(obj)
        assertThat(obj).isEqualTo(obj as TestObject)
        assertFailsWith<AssertionError> {
            assertThat(1).isEqualTo("string")
        }
    }

    @Test fun isEqualTo_will_report_different_types_with_the_same_display_representation() {
        val error = assertFailsWith<AssertionError> {
            assertThat(Actual("test")).isEqualTo(Expected("test"))
        }
        assertEquals("expected:<test> with type:<${Expected::class}> but was type:<${Actual::class}> with the same string representation", error.message)
    }

    @Test fun isEqualTo_will_report_that_same_types_with_the_same_display_representation_do_not_compare_equal() {
        val error = assertFailsWith<AssertionError> {
            assertThat(Actual("test")).isEqualTo(Actual("test"))
        }
        assertEquals("expected:<test> with type:<${Actual::class}> did not compare equal to the same type with the same string representation", error.message)
    }

    @Test fun isNotEqualTo_non_equal_objects_passes() {
        val nonEqual = BasicObject("not test")
        assertThat(subject).isNotEqualTo(nonEqual)
    }

    @Test fun isNotEqualTo_equal_objects_fails() {
        val equal = BasicObject("test")
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isNotEqualTo(equal)
        }
        assertEquals("expected to not be equal to:<test>", error.message)
    }

    @Test fun isSameInstanceAs_same_objects_passes() {
        assertThat(subject).isSameInstanceAs(subject)
    }

    @Test fun isSameInstanceAs_different_objects_fails() {
        val nonSame = BasicObject("test")
        val error = assertFails("") {
            assertThat(subject).isSameInstanceAs(nonSame)
        }
        assertEquals("expected:<test> and:<test> to refer to the same object", error.message)
    }

    @Test fun isNotSameInstanceAs_non_same_objects_passes() {
        val nonSame = BasicObject("test")
        assertThat(subject).isNotSameInstanceAs(nonSame)
    }

    @Test fun isNotSameInstanceAs_same_objects_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isNotSameInstanceAs(subject)
        }
        assertEquals("expected:<test> to not refer to the same object", error.message)
    }

    @Test fun isIn_one_equal_item_passes() {
        val isIn = BasicObject("test")
        assertThat(subject).isIn(isIn)
    }

    @Test fun isIn_one_non_equal_item_fails() {
        val isOut1 = BasicObject("not test1")
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isIn(isOut1)
        }
        assertEquals("expected:<[not test1]> to contain:<test>", error.message)
    }

    @Test fun isIn_one_equal_item_in_may_passes() {
        val isOut1 = BasicObject("not test1")
        val isIn = BasicObject("test")
        val isOut2 = BasicObject("not test2")
        assertThat(subject).isIn(isOut1, isIn, isOut2)
    }

    @Test fun isIn_no_equal_items_in_may_fails() {
        val isOut1 = BasicObject("not test1")
        val isOut2 = BasicObject("not test2")
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isIn(isOut1, isOut2)
        }
        assertEquals("expected:<[not test1, not test2]> to contain:<test>", error.message)
    }

    @Test fun isNotIn_one_non_equal_item_passes() {
        val isOut1 = BasicObject("not test1")
        assertThat(subject).isNotIn(isOut1)
    }

    @Test fun isNotIn_one_equal_item_fails() {
        val isIn = BasicObject("test")
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isNotIn(isIn)
        }
        assertEquals("expected:<[test]> to not contain:<test>", error.message)
    }

    @Test fun isNotIn_no_equal_items_in_many_passes() {
        val isOut1 = BasicObject("not test1")
        val isOut2 = BasicObject("not test2")
        assertThat(subject).isNotIn(isOut1, isOut2)
    }

    @Test fun isNotIn_one_equal_item_in_many_fails() {
        val isOut1 = BasicObject("not test1")
        val isIn = BasicObject("test")
        val isOut2 = BasicObject("not test2")
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isNotIn(isOut1, isIn, isOut2)
        }
        assertEquals("expected:<[not test1, test, not test2]> to not contain:<test>", error.message)
    }

    @Test fun hasToString_equal_string_passes() {
        assertThat(subject).hasToString("test")
    }

    @Test fun hasToString_not_equal_string_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).hasToString("not test")
        }
        assertEquals("expected [toString]:<\"[not ]test\"> but was:<\"[]test\"> (test)", error.message)
    }

    @Test fun hasHashCode_equal_value_passes() {
        assertThat(subject).hasHashCode(42)
    }

    @Test fun hasHashCode_not_equal_value_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).hasHashCode(1337)
        }
        assertEquals("expected [hashCode]:<[1337]> but was:<[42]> (test)", error.message)
    }


    private val testObject = BasicObject("test", 99, 3.14)

    @Test fun isEqualToWithGivenProperties_regular_equals_fail() {
        assertFailsWith<AssertionError> {
            assertThat(subject).isEqualTo(testObject)
        }
    }

    @Test fun isEqualToWithGivenProperties_extract_prop_passes() {
        assertThat(subject).isEqualToWithGivenProperties(
            testObject,
            BasicObject::str,
            BasicObject::double,
            BasicObject::other
        )
    }

    @Test fun isEqualToWithGivenProperties_extract_prop_includes_name_in_failure_message() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isEqualToWithGivenProperties(testObject, BasicObject::int)
        }
        assertEquals("expected [int]:<[99]> but was:<[42]> (test)", error.message)
    }

    //region prop
    @Test fun prop_passes() {
        assertThat(subject).prop("str") { it.str }.isEqualTo("test")
    }

    @Test fun prop_includes_name_in_failure_message() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).prop("str") { it.str }.isEmpty()
        }
        assertEquals("expected [str] to be empty but was:<\"test\"> (test)", error.message)
    }

    @Test fun nested_prop_include_names_in_failure_message() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).prop("other") { it.other }.prop("str") { it?.str }.isNotNull()
        }
        assertEquals("expected [other.str] to not be null (test)", error.message)
    }

    @Test fun prop_property1_extract_prop_passes() {
        assertThat(subject).prop(BasicObject::str).isEqualTo("test")
    }

    @Test fun prop_property1_extract_prop_includes_name_in_failure_message() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).prop(BasicObject::str).isEmpty()
        }
        assertEquals("expected [str] to be empty but was:<\"test\"> (test)", error.message)
    }

    @Test fun prop_property1_includes_error_message_when_fails() {
        val error = assertFails {
            assertThat(subject).prop(BasicObject::failing).isEmpty()
        }
        assertEquals("sorry!", error.message)
    }

    @Test fun prop_callable_function_passes() {
        assertThat(subject).prop(BasicObject::funcA).isEqualTo("A")
    }

    @Test fun prop_callable_function_includes_name_in_failure_message() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).prop(BasicObject::funcA).isEqualTo(14)
        }
        assertEquals("expected [funcA]:<[14]> but was:<[\"A\"]> (test)", error.message)
    }

    @Test fun nested_prop_callable_function_include_names_in_failure_message() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).prop(BasicObject::funcB).prop(BasicObject::funcA).isNull()
        }
        assertEquals("expected [funcB.funcA] to be null but was:<\"A\"> (test)", error.message)
    }
    //endregion

    @Test fun isNull_null_passes() {
        assertThat(null as String?).isNull()
    }

    @Test fun isNull_non_null_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(nullableSubject).isNull()
        }
        assertEquals("expected to be null but was:<test>", error.message)
    }

    @Test fun isNotNull_non_null_passes() {
        assertThat(nullableSubject).isNotNull()
    }

    @Test fun isNotNull_null_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(null as String?).isNotNull()
        }
        assertEquals("expected to not be null", error.message)
    }

    @Test fun isNotNull_non_null_and_equal_object_passes() {
        assertThat(nullableSubject).isNotNull().isEqualTo(subject)
    }

    @Test fun isNotNull_non_null_and_non_equal_object_fails() {
        val unequal = BasicObject("not test")
        val error = assertFailsWith<AssertionError> {
            assertThat(nullableSubject).isNotNull().isEqualTo(unequal)
        }
        assertEquals("expected:<[not ]test> but was:<[]test>", error.message)
    }

    @Test fun isNotNull_null_and_equal_object_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(null as String?).isNotNull().isEqualTo(null)
        }
        assertEquals("expected to not be null", error.message)
    }

    @Test fun hasClass_same_class_passes() {
        assertThat(subject).hasClass(BasicObject::class)
    }

    @Test fun hasClass_reified_same_class_passes() {
        assertThat(subject).hasClass<BasicObject>()
    }

    @Test fun hasClass_parent_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).hasClass(TestObject::class)
        }
        assertEquals(
            "expected to have class:<${TestObject::class}> but was:<${BasicObject::class}>",
            error.message
        )
    }

    @Test fun hasClass_reified_parent_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).hasClass<TestObject>()
        }
        assertEquals(
            "expected to have class:<${TestObject::class}> but was:<${BasicObject::class}>",
            error.message
        )
    }

    @Test fun doesNotHaveClass_parent_class_passes() {
        assertThat(subject).doesNotHaveClass(TestObject::class)
    }

    @Test fun doesNotHaveClass_reified_parent_class_passes() {
        assertThat(subject).doesNotHaveClass<TestObject>()
    }

    @Test fun doesNotHaveClass_same_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).doesNotHaveClass(BasicObject::class)
        }
        assertEquals("expected to not have class:<${BasicObject::class}>", error.message)
    }

    @Test fun doesNotHaveClass_reified_same_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).doesNotHaveClass<BasicObject>()
        }
        assertEquals("expected to not have class:<${BasicObject::class}>", error.message)
    }

    @Test fun isInstanceOf_kclass_same_class_passes() {
        assertThat(subject as TestObject).isInstanceOf(BasicObject::class)
    }

    @Test fun isInstanceOf_reified_kclass_same_class_passes() {
        assertThat(subject as TestObject).isInstanceOf<BasicObject>()
    }

    @Test fun isInstanceOf_kclass_parent_class_passes() {
        assertThat(subject).isInstanceOf(TestObject::class)
    }

    @Test fun isInstanceOf_reified_kclass_parent_class_passes() {
        assertThat(subject).isInstanceOf<TestObject>()
    }

    @Test fun isInstanceOf_kclass_different_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isInstanceOf(DifferentObject::class)
        }
        assertEquals(
            "expected to be instance of:<${DifferentObject::class}> but had class:<${BasicObject::class}>",
            error.message
        )
    }

    @Test fun isInstanceOf_reified_kclass_different_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isInstanceOf<DifferentObject>()
        }
        assertEquals(
            "expected to be instance of:<${DifferentObject::class}> but had class:<${BasicObject::class}>",
            error.message
        )
    }

    @Test fun isInstanceOf_kclass_run_block_when_passes() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject as TestObject).isInstanceOf(BasicObject::class).prop("str", BasicObject::str)
                .isEqualTo("wrong")
        }
        assertEquals("expected [str]:<\"[wrong]\"> but was:<\"[test]\"> (test)", error.message)
    }

    @Test fun isInstanceOf_reified_kclass_run_block_when_passes() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject as TestObject).isInstanceOf<BasicObject>().prop("str", BasicObject::str)
                .isEqualTo("wrong")
        }
        assertEquals("expected [str]:<\"[wrong]\"> but was:<\"[test]\"> (test)", error.message)
    }

    @Test fun isInstanceOf_kclass_doesnt_run_block_when_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject as TestObject).isInstanceOf(DifferentObject::class).isNull()
        }
        assertEquals(
            "expected to be instance of:<${DifferentObject::class}> but had class:<${BasicObject::class}>",
            error.message
        )
    }

    @Test fun isInstanceOf_reified_kclass_doesnt_run_block_when_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject as TestObject).isInstanceOf<DifferentObject>().isNull()
        }
        assertEquals(
            "expected to be instance of:<${DifferentObject::class}> but had class:<${BasicObject::class}>",
            error.message
        )
    }

    @Test fun isNotInstanceOf_kclass_different_class_passes() {
        assertThat(subject).isNotInstanceOf(DifferentObject::class)
    }

    @Test fun isNotInstanceOf_reified_kclass_different_class_passes() {
        assertThat(subject).isNotInstanceOf<DifferentObject>()
    }

    @Test fun isNotInstanceOf_kclass_same_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isNotInstanceOf(BasicObject::class)
        }
        assertEquals("expected to not be instance of:<${BasicObject::class}>", error.message)
    }

    @Test fun isNotInstanceOf_reified_kclass_same_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isNotInstanceOf<BasicObject>()
        }
        assertEquals("expected to not be instance of:<${BasicObject::class}>", error.message)
    }

    @Test fun isNotInstanceOf_kclass_parent_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isNotInstanceOf(TestObject::class)
        }
        assertEquals("expected to not be instance of:<${TestObject::class}>", error.message)
    }

    @Test fun isNotInstanceOf_reifeid_kclass_parent_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isNotInstanceOf<TestObject>()
        }
        assertEquals("expected to not be instance of:<${TestObject::class}>", error.message)
    }

    @Test fun corresponds_equivalent_passes() {
        assertThat(subject).corresponds(BasicObject("different")) { _, _ -> true }
    }

    @Test fun corresponds_not_equivalent_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).corresponds(BasicObject("test")) { _, _ -> false }
        }
        assertEquals("expected:<test> but was:<test>", error.message)
    }

    @Test fun doesNotCorrespond_equivalent_passes() {
        assertThat(subject).doesNotCorrespond(BasicObject("different")) { _, _ -> false }
    }

    @Test fun doesNotCorrespond_not_equivalent_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).doesNotCorrespond(BasicObject("different")) { _, _ -> true }
        }
        assertEquals("expected:<different> not to be equal to:<test>", error.message)
    }

    companion object {
        open class TestObject

        class BasicObject(
            val str: String,
            val int: Int = 42,
            val double: Double = 3.14,
            val other: BasicObject? = null
        ) : TestObject() {
            val failing: String get() = throw Exception("sorry!")

            fun funcA(): String = "A"
            fun funcB(): BasicObject = this

            override fun toString(): String = str

            override fun equals(other: Any?): Boolean =
                (other is BasicObject) && (str == other.str && int == other.int && double == other.double && this.other == other.other)

            override fun hashCode(): Int = 42
        }

        class DifferentObject : TestObject()

        class Expected(val value: String) {
            override fun toString() = value
        }
        class Actual(val value: String) {
            override fun toString() = value
        }
    }
}
