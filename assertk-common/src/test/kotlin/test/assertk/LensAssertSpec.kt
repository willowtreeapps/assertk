package test.assertk

import assertk.assert
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.key
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails


class LensAssertSpec_a_list_of_lists_On_index() {
    val subject = listOf(listOf("one"), listOf("two"))

    @Test
    fun it_should_pass_a_successful_test() {
        assert(subject, name = "subject").index(0) { it.isEqualTo(listOf("one")) }
    }

    @Test
    fun it_should_fail_an_unsuccessful_test() {
        val error = assertFails {
            assert(subject, name = "subject").index(0) { it.isEqualTo(listOf("wrong")) }
        }
        assertEquals(
            "expected [subject[0]]:<[\"[wrong]\"]> but was:<[\"[one]\"]> ([[\"one\"], [\"two\"]])",
            error.message
        )
    }

    @Test
    fun it_should_pass_a_successful_nested_list_test() {
        assert(subject).index(1) { it.index(0) { it.isEqualTo("two") } }
    }

    @Test
    fun it_should_fail_an_unsuccessful_nested_prop_test() {
        val error = assertFails {
            assert(subject, name = "subject").index(1) { it.index(0) { it.isEqualTo("wrong") } }
        }
        assertEquals(
            "expected [subject[1][0]]:<\"[wrong]\"> but was:<\"[two]\"> ([[\"one\"], [\"two\"]])",
            error.message
        )
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
        val error = assertFails {
            assert(subject, name = "subject").key("one") { it.isEqualTo(mapOf("wrong" to 2)) }
        }
        assertEquals(
            "expected [subject[\"one\"]]:<{\"[wrong]\"=2}> but was:<{\"[two]\"=2}> ({\"one\"={\"two\"=2}})",
            error.message
        )
    }

    @Test
    fun it_should_pass_a_successful_nested_map_test() {
        assert(subject).key("one") { it.key("two") { it.isEqualTo(2) } }
    }

    @Test
    fun it_should_fail_an_unsuccessful_nested_prop_test() {
        val error = assertFails {
            assert(subject, name = "subject").key("one") { it.key("two") { it.isEqualTo(0) } }
        }
        assertEquals("expected [subject[\"one\"][\"two\"]]:<[0]> but was:<[2]> ({\"one\"={\"two\"=2}})", error.message)
    }
}


class LensAssertSpec {

    data class BasicDataObject(val arg1: String, val arg2: Nested)

    data class Nested(val arg1: Int)
}

