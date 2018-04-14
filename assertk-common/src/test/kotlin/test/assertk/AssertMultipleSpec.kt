package test.assertk

import assertk.all
import assertk.assert
import assertk.assertAll
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.toStringFun
import test.assertk.AssertMultipleSpec.BasicObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class AssertMultipleSpec_On_an_assert_block_with_a_value {
    private val subject = BasicObject("test", 1)

    @Test
    fun it_should_pass_multiple_successful_assertions() {
        assert(subject).all {
            isInstanceOf(BasicObject::class)
            toStringFun().isEqualTo("BasicObject(arg1=test, arg2=1)")
        }
    }

    @Test
    fun it_should_fail_the_first_assertion() {
        val error = assertFails {
            assert(subject).all {
                isInstanceOf(String::class)
                toStringFun().isEqualTo("BasicObject(arg1=test, arg2=1)")
            }
        }
        assertEquals(
            "expected to be instance of:<${String::class}> but had class:<${BasicObject::class}>",
            error.message
        )
    }

    @Test
    fun it_should_fail_the_second_assertion() {
        val error = assertFails {
            assert(subject).all {
                isInstanceOf(BasicObject::class)
                toStringFun().isEqualTo("wrong")
            }
        }
        assertEquals(
            "expected [toString]:<\"[wrong]\"> but was:<\"[BasicObject(arg1=test, arg2=1)]\"> (BasicObject(arg1=test, arg2=1))",
            error.message
        )
    }

    @Test
    fun it_should_fail_both_assertions() {
        val error = assertFails {
            assert<Any>(subject).all {
                isInstanceOf(String::class)
                toStringFun().isEqualTo("wrong")
            }
        }

        assertEquals(
            """The following 2 assertions failed:
- expected to be instance of:<${String::class}> but had class:<${BasicObject::class}>
- expected [toString]:<"[wrong]"> but was:<"[BasicObject(arg1=test, arg2=1)]"> (BasicObject(arg1=test, arg2=1))""",
            error.message
        )
    }
}

class AssertMultipleSpec_On_an_assert_all_block {
    val subject1 = BasicObject("test1", 1)
    val subject2 = BasicObject("test2", 2)

    @Test
    fun it_should_pass_multiple_successful_assertions() {
        assertAll {
            assert(subject1).toStringFun().isEqualTo("BasicObject(arg1=test1, arg2=1)")
            assert(subject2).toStringFun().isEqualTo("BasicObject(arg1=test2, arg2=2)")
        }
    }

    @Test
    fun it_should_fail_the_first_assertion() {
        val error = assertFails {
            assertAll {
                assert(subject1).toStringFun().isEqualTo("wrong1")
                assert(subject2).toStringFun().isEqualTo("BasicObject(arg1=test2, arg2=2)")
            }
        }
        assertEquals(
            "expected [toString]:<\"[wrong1]\"> but was:<\"[BasicObject(arg1=test1, arg2=1)]\"> (BasicObject(arg1=test1, arg2=1))",
            error.message
        )
    }

    @Test
    fun it_should_fail_the_second_assertion() {
        val error = assertFails {
            assertAll {
                assert(subject1).toStringFun().isEqualTo("BasicObject(arg1=test1, arg2=1)")
                assert(subject2).toStringFun().isEqualTo("wrong2")
            }
        }
        assertEquals(
            "expected [toString]:<\"[wrong2]\"> but was:<\"[BasicObject(arg1=test2, arg2=2)]\"> (BasicObject(arg1=test2, arg2=2))",
            error.message
        )
    }

    @Test
    fun it_should_fail_both_assertions() {
        val error = assertFails {
            assertAll {
                assert(subject1).toStringFun().isEqualTo("wrong1")
                assert(subject2).toStringFun().isEqualTo("wrong2")
            }
        }

        assertEquals(
            """The following 2 assertions failed:
- expected [toString]:<"[wrong1]"> but was:<"[BasicObject(arg1=test1, arg2=1)]"> (BasicObject(arg1=test1, arg2=1))
- expected [toString]:<"[wrong2]"> but was:<"[BasicObject(arg1=test2, arg2=2)]"> (BasicObject(arg1=test2, arg2=2))""",
            error.message
        )
    }
}

class AssertMultipleSpec {

    data class BasicObject(val arg1: String, val arg2: Int)
}

