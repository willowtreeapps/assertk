package test.assertk

import assertk.assert
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.key
import assertk.assertions.prop
import test.assertk.LensAssertSpec.BasicDataObject
import test.assertk.LensAssertSpec.Nested
import kotlin.test.Test

class LensAssertSpec_a_basic_data_object_On_prop_callable {

    private val subject = BasicDataObject("test", Nested(1))


    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).prop(BasicDataObject::arg1).isEqualTo("test")
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).prop(BasicDataObject::arg1).isEqualTo("wrong")
        }.hasMessage("expected [arg1]:<\"[wrong]\"> but was:<\"[test]\"> (BasicDataObject(arg1=test, arg2=Nested(arg1=1)))")
    }

    @Test
    fun it_should_pass_a_successful_nested_prop_test() {
        assert(subject).prop(BasicDataObject::arg2).prop(Nested::arg1).isEqualTo(1)
    }

    @Test
    fun it_should_fail_an_unsuccessful_nested_prop_test() {
        Assertions.assertThatThrownBy {
            assert(subject).prop(BasicDataObject::arg2).prop(Nested::arg1).isEqualTo(0)
        }.hasMessage("expected [arg2.arg1]:<[0]> but was:<[1]> (BasicDataObject(arg1=test, arg2=Nested(arg1=1)))")
    }
}

class LensAssertSpec_a_complex_structure_On_lookup {

    private val subject = mapOf("key" to listOf(BasicDataObject("test", Nested(1))))

    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject, name = "subject")
                .key("key")
                .index(0)
                .prop(BasicDataObject::arg2)
                .prop(Nested::arg1)
                .isEqualTo(1)
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
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
