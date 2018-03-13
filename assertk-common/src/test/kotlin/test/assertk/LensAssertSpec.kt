package test.assertk

import assertk.assert
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.key
import assertk.assertions.prop
import test.assertk.LensAssertSpec.BasicDataObject
import test.assertk.LensAssertSpec.Nested
import kotlin.test.Test

class LensAssertSpec_a_basic_data_object_On_prop_fn_name {
    private val subject = BasicDataObject("test", Nested(1))

    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject).prop("arg1") { it.arg1 }.isEqualTo("test")
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject).prop("arg1") { it.arg1 }.isEqualTo("wrong")
        }.hasMessage("expected [arg1]:<\"[wrong]\"> but was:<\"[test]\"> (BasicDataObject(arg1=test, arg2=Nested(arg1=1)))")
    }

    @Test
    fun it_should_pass_a_successful_nested_prop_test() {
        assert(subject).prop("arg2") { it.arg2 }.prop("arg1") { it.arg1 }.isEqualTo(1)
    }

    @Test
    fun it_should_fail_an_unsuccessful_nested_prop_test() {
        Assertions.assertThatThrownBy {
            assert(subject).prop("arg2") { it.arg2 }.prop("arg1") { it.arg1 }.isEqualTo(0)
        }.hasMessage("expected [arg2.arg1]:<[0]> but was:<[1]> (BasicDataObject(arg1=test, arg2=Nested(arg1=1)))")
    }
}


class LensAssertSpec_a_list_of_lists_On_index() {
    val subject = listOf(listOf("one"), listOf("two"))

    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject, name = "subject").index(0) { it.isEqualTo(listOf("one")) }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject, name = "subject").index(0) { it.isEqualTo(listOf("wrong")) }
        }.hasMessage("expected [subject[0]]:<[\"[wrong]\"]> but was:<[\"[one]\"]> ([[\"one\"], [\"two\"]])")
    }

    @Test
    fun should_fail_out_of_range() {
        Assertions.assertThatThrownBy {
            assert(subject, name = "subject").index(-1) { it.isEqualTo(listOf("one")) }
        }.hasMessage("expected [subject] index to be in range:[0-2) but was:<-1>")
    }

    @Test
    fun it_should_pass_a_successful_nested_list_test() {
        assert(subject).index(1) { it.index(0) { it.isEqualTo("two") } }
    }

    @Test
    fun it_should_fail_an_unsuccessful_nested_prop_test() {
        Assertions.assertThatThrownBy {
            assert(subject, name = "subject").index(1) { it.index(0) { it.isEqualTo("wrong") } }
        }.hasMessage("expected [subject[1][0]]:<\"[wrong]\"> but was:<\"[two]\"> ([[\"one\"], [\"two\"]])")
    }
}


class LensAssertSpec_a_map_of_maps_On_key() {

    val subject = mapOf("one" to mapOf("two" to 2))

    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject, name = "subject").key("one") { it.isEqualTo(mapOf("two" to 2)) }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        Assertions.assertThatThrownBy {
            assert(subject, name = "subject").key("one") { it.isEqualTo(mapOf("wrong" to 2)) }
        }.hasMessage("expected [subject[\"one\"]]:<{\"[wrong]\"=2}> but was:<{\"[two]\"=2}> ({\"one\"={\"two\"=2}})")
    }

    @Test
    fun it_should_pass_a_successful_nested_map_test() {
        assert(subject).key("one") { it.key("two") { it.isEqualTo(2) } }
    }

    @Test
    fun should_fail_with_missing_key() {
        Assertions.assertThatThrownBy {
            assert(subject, name = "subject").key("wrong") { it.isEqualTo(mapOf("two" to 2)) }
        }
                .hasMessage("expected [subject] to have key:<\"wrong\">")
    }

    @Test
    fun it_should_fail_an_unsuccessful_nested_prop_test() {
        Assertions.assertThatThrownBy {
            assert(subject, name = "subject").key("one") { it.key("two") { it.isEqualTo(0) } }
        }.hasMessage("expected [subject[\"one\"][\"two\"]]:<[0]> but was:<[2]> ({\"one\"={\"two\"=2}})")
    }
}


class LensAssertSpec {

    data class BasicDataObject(val arg1: String, val arg2: Nested)

    data class Nested(val arg1: Int)
}

