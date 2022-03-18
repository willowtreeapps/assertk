package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import test.assertk.opentestPackageName
import java.lang.Exception
import kotlin.reflect.KCallable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

class JavaAnyTest {
    val p: String = JavaAnyTest::class.java.name
    val subject = BasicObject("test")

    //region jClass
    @Test fun extracts_jClass() {
        assertEquals(BasicObject::class.java, assertThat(subject as TestObject).jClass().valueOrFail)
    }
    //endregion

    //region isInstanceOf
    @Test fun isInstanceOf_jclass_same_class_passes() {
        assertThat(subject).isInstanceOf(BasicObject::class.java)
    }

    @Test fun isInstanceOf_jclass_parent_class_passes() {
        assertThat(subject).isInstanceOf(TestObject::class.java)
    }

    @Test fun isInstanceOf_jclass_different_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isInstanceOf(DifferentObject::class.java)
        }
        assertEquals(
            "expected to be instance of:<$p\$DifferentObject> but had class:<$p\$BasicObject>",
            error.message
        )
    }

    @Test fun isInstanceOf_jclass_run_block_when_passes() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject as TestObject)
                .isInstanceOf(BasicObject::class.java)
                .prop("str", BasicObject::str)
                .isEqualTo("wrong")
        }
        assertEquals("expected [str]:<\"[wrong]\"> but was:<\"[test]\"> (test)", error.message)
    }

    @Test fun isInstanceOf_jclass_doesnt_run_block_when_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject as TestObject)
                .isInstanceOf(DifferentObject::class.java)
                .isNull()
        }
        assertEquals(
            "expected to be instance of:<$p\$DifferentObject> but had class:<$p\$BasicObject>",
            error.message
        )
    }
    //endregion

    //region isNotInstanceOf
    @Test fun isNotInstanceOf_jclass_different_class_passess() {
        assertThat(subject).isNotInstanceOf(DifferentObject::class.java)
    }

    @Test fun isNotInstanceOf_jclass_same_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isNotInstanceOf(BasicObject::class.java)
        }
        assertEquals("expected to not be instance of:<$p\$BasicObject>", error.message)
    }

    @Test fun isNotInstanceOf_jclass_parent_class_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(subject).isNotInstanceOf(TestObject::class.java)
        }
        assertEquals("expected to not be instance of:<$p\$TestObject>", error.message)
    }
    //endregion

    //region prop
    @Test fun prop_callable_extract_prop_passes() {
        @Suppress("DEPRECATION")
        assertThat(subject).prop(BasicObject::str as KCallable<String>).isEqualTo("test")
    }

    @Test fun prop_callable_extract_prop_includes_name_in_failure_message() {
        val error = assertFailsWith<AssertionError> {
            @Suppress("DEPRECATION")
            assertThat(subject).prop(BasicObject::str  as KCallable<String>).isEmpty()
        }
        assertEquals("expected [str] to be empty but was:<\"test\"> (test)", error.message)
    }

    @Test fun prop_callable_includes_error_message_when_fails() {
        val error = assertFails {
            @Suppress("DEPRECATION")
            assertThat(subject).prop(BasicObject::failing as KCallable<String>).isEmpty()
        }
        assertEquals("sorry!", error.message)
    }
    //endregion

    //region isDataClassEqualTo
    @Test fun isDataClassEqualTo_equal_data_classes_passes() {
        assertThat(DataClass(InnerDataClass("test"), 1, 'a'))
            .isDataClassEqualTo(DataClass(InnerDataClass("test"), 1, 'a'))
    }

    @Test fun isDataClassEqualTo_reports_all_properties_that_differ_on_failure() {
        val error = assertFailsWith<AssertionError> {
            assertThat(DataClass(InnerDataClass("test"), 1, 'a'))
                .isDataClassEqualTo(DataClass(InnerDataClass("wrong"), 1, 'b'))
        }
        assertEquals(
            """The following assertions failed (2 failures)
            |${"\t"}${opentestPackageName}AssertionFailedError: expected [one.inner]:<"[wrong]"> but was:<"[test]"> (DataClass(one=InnerDataClass(inner=test), two=1, three=a))
            |${"\t"}${opentestPackageName}AssertionFailedError: expected [three]:<'[b]'> but was:<'[a]'> (DataClass(one=InnerDataClass(inner=test), two=1, three=a))
        """.trimMargin().lines(), error.message!!.lines()
        )
    }

    @Test fun isDataClassEqualTo_fails_on_null_property_in_actual() {
        val error = assertFailsWith<AssertionError> {
            assertThat(DataClass(null, 1, 'a'))
                .isDataClassEqualTo(DataClass(InnerDataClass("wrong"), 1, 'b'))
        }
        assertEquals(
            """The following assertions failed (2 failures)
            |${"\t"}${opentestPackageName}AssertionFailedError: expected [one]:<InnerDataClass(inner=wrong)> but was:<null> (DataClass(one=null, two=1, three=a))
            |${"\t"}${opentestPackageName}AssertionFailedError: expected [three]:<'[b]'> but was:<'[a]'> (DataClass(one=null, two=1, three=a))
        """.trimMargin().lines(), error.message!!.lines()
        )
    }

    @Test fun isDataClassEqualTo_fails_on_null_property_in_expected() {
        val error = assertFailsWith<AssertionError> {
            assertThat(DataClass(InnerDataClass("test"), 1, 'a'))
                .isDataClassEqualTo(DataClass(null, 1, 'b'))
        }
        assertEquals(
            """The following assertions failed (2 failures)
            |${"\t"}${opentestPackageName}AssertionFailedError: expected [one]:<null> but was:<InnerDataClass(inner=test)> (DataClass(one=InnerDataClass(inner=test), two=1, three=a))
            |${"\t"}${opentestPackageName}AssertionFailedError: expected [three]:<'[b]'> but was:<'[a]'> (DataClass(one=InnerDataClass(inner=test), two=1, three=a))
        """.trimMargin().lines(), error.message!!.lines()
        )
    }
    //endregion

    //region isEqualToIgnoringGivenProperties
    @Test fun isEqualToIgnoringGivenProperties_passes() {
        assertThat(BasicObject("Rarity")).isEqualToIgnoringGivenProperties(BasicObject("notRarity"), BasicObject::str, BasicObject::other, BasicObject::failing)
    }

    @Test fun isEqualToIgnoringGivenProperties_fails() {
        assertFailsWith<AssertionError> {
            assertThat(BasicObject("Rarity", int = 42)).isEqualToIgnoringGivenProperties(BasicObject("notRarity", int = 1337), BasicObject::str, BasicObject::failing)
        }
    }

    @Test fun isEqualToIgnoringGivenProperties_passes_even_when_private_property_differs() {
        assertThat(BasicObject(str = "Rarity", private = 1)).isEqualToIgnoringGivenProperties(BasicObject(str = "notRarity", private = 2), BasicObject::str, BasicObject::other, BasicObject::failing)
    }
    //endregion

    open class TestObject

    class BasicObject(
        val str: String,
        val int: Int = 42,
        val double: Double = 3.14,
        val other: BasicObject? = null,
        private val private: Int = 0,
    ) : TestObject() {
        val failing: String get() = throw Exception("sorry!")

        override fun toString(): String = str

        override fun equals(other: Any?): Boolean =
            (other is BasicObject) && (str == other.str && int == other.int && double == other.double)

        override fun hashCode(): Int = 42
    }

    class DifferentObject : TestObject()

    data class DataClass(val one: InnerDataClass?, val two: Int, val three: Char)

    data class InnerDataClass(val inner: String)
}

